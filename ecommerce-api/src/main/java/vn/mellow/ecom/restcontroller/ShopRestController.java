package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ClientException;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.base.client.GHNClient;
import vn.mellow.ecom.model.enums.ActiveStatus;
import vn.mellow.ecom.model.enums.RoleType;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.input.CreateShopDTO;
import vn.mellow.ecom.model.input.ShopGHNDTO;
import vn.mellow.ecom.model.input.ShopUpdateDTO;
import vn.mellow.ecom.model.shop.Shop;
import vn.mellow.ecom.model.user.User;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/shop")
public class ShopRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ShopRestController.class);
    @Value("${GHN.url}")
    private String url;
    @Value("${GHN.token}")
    private String token;
    @Autowired
    private UserManager userManager;

    private void validateShopInput(String userId, CreateShopDTO shopInput) throws ServiceException {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("not_found", "Vui lòng truyền mã tài khoản", "userId is empty");
        }

        if (shopInput == null) {
            throw new ServiceException("not_found", "Vui lòng truyền thông tin cửa hàng của bạn", "shop is empty");
        }

        if (null == shopInput.getAddress() || 0 == shopInput.getDistrict_id()
                || null == shopInput.getWardCode()) {
            throw new ServiceException("not_found", "Vui lòng truyền địa chỉ của cửa hàng bạn", "shopId is empty");
        }
        if (null == shopInput.getPhone()) {
            throw new ServiceException("not_found", "Vui lòng truyền số điện thoại của cửa hàng bạn", "phone is empty");
        }

        if (null == shopInput.getName()) {
            throw new ServiceException("not_found", "Vui lòng truyền tên cửa hàng của bạn", "shopName is empty");
        }
        if (null == shopInput.getImageUrl()) {
            throw new ServiceException("not_found", "Vui lòng truyền logo của cửa hàng bạn", "imageUrl is empty");
        }
    }

    private GHNClient getGHNClient() {
        return new GHNClient(url);
    }

    @ApiOperation("create shop from user")
    @PostMapping("/create")
    public User createShop(@RequestParam("user-id") String userId, @RequestBody CreateShopDTO shopInput) throws ServiceException {
        User user = userManager.getUser(userId);
        if (user == null) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin tài khoản :" + userId, "user is empty");
        }
        //validate shop
        validateShopInput(userId, shopInput);
        //update role
        userManager.updateRole(userId, RoleType.PERSONAL_STORE, user.getByUser());
        Shop shop = new Shop();
        int shopId = createShopShipmentGHN(shopInput);
        shop.setShopId(shopId);
        if (null == shopInput.getImageUrl()) {
            String imageUrl = shopInput.getName() == null ? user.getUsername() : shopInput.getName();
            shopInput.setImageUrl("https://ui-avatars.com/api/?name=" + imageUrl.replaceAll(" ", ""));
        }
        shop.setImageUrl(shopInput.getImageUrl());
        shop.setStatus(ActiveStatus.ACTIVE);
        shop.setName(shopInput.getName());
        shop.setPhone(shopInput.getPhone());
        shop.setAddress(shopInput.getAddressShop());
        shop.setDescription(shopInput.getDescription());

        return userManager.createShop(userId, shop);
    }

    @ApiOperation("update shop ")
    @PutMapping("/shop/update/info")
    public Shop updateShop(@RequestParam("user-id") String userId,
                           @RequestParam("shop-id") int shopId,
                           @RequestBody ShopUpdateDTO shop) {
        userManager.updateShop(userId, shopId, shop);
        return userManager.getUserShop(shopId).getShop();
    }

    @ApiOperation("update shop address")
    @PutMapping("/shop/update/address")
    public Shop updateShopAddress(@RequestParam("shop-id") int shopId,
                                  @RequestParam("province-code") int provinceCode,
                                  @RequestParam("district-code") int districtCode,
                                  @RequestParam("ward-code") int wardCode
            , @RequestParam("address") String address) throws ServiceException {
        userManager.updateAddressShop(shopId, provinceCode, districtCode, wardCode, address);
        return getInfoShop(shopId);
    }


    private Integer createShopShipmentGHN(CreateShopDTO shopInput) throws ServiceException {
        Integer shopId = null;
        try {
            ShopGHNDTO shopGHNDTO = new ShopGHNDTO();
            shopGHNDTO.setDistrict_id(shopInput.getDistrict_id());
            shopGHNDTO.setWard_code(shopInput.getWardCode());
            shopGHNDTO.setAddress(shopInput.getAddress());
            shopGHNDTO.setPhone(shopInput.getPhone());
            shopGHNDTO.setName(shopInput.getName());
            String current = getGHNClient().createShop(token, shopGHNDTO).getData().toString();
            shopId = Integer.valueOf(current.substring(10, current.length() - 3));

        } catch (ClientException e) {
            throw new ServiceException(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
        }
        return shopId;
    }

    @ApiOperation(value = "get shop by shop id")
    @GetMapping("/shop/{shopId}")
    public Shop getInfoShop(@PathVariable int shopId) throws ServiceException {
        return userManager.getInfoShop(shopId);
    }

    @ApiOperation(value = "get shop list")
    @GetMapping("/shop/all")
    public List<Shop> getShopAll() {
        List<Shop> shops = new ArrayList<Shop>();
        List<User> users = userManager.getShops();
        if (users != null) {
            users.forEach(user -> {
                Shop shop = user.getShop();
                shops.add(shop);
            });
        }
        return shops;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllServiceException(ServiceException e) {
        LOGGER.error("ServiceException error.", e);
        return error(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return error("internal_server_error", "Có lỗi trong quá trình xử lý", e.getMessage());
    }

}
