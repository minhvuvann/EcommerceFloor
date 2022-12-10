package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.client.GHNClient;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateShopInput;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.ResultGHN;
import vn.mellow.ecom.ecommercefloor.model.shipment.convert.ShopGHN;
import vn.mellow.ecom.ecommercefloor.model.shop.Shop;
import vn.mellow.ecom.ecommercefloor.model.user.User;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/shop")
public class ShopController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ShopController.class);
    @Value("${GHN.url}")
    private String url;
    @Value("${GHN.token}")
    private String token;
    @Autowired
    private UserManager userManager;

    private void validateShopInput(String userId, CreateShopInput shopInput) throws ServiceException {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("not_found", "Vui lòng truyền mã tài khoản", "userId is empty");
        }
        User user = userManager.getUser(userId);
        if (user == null) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin tài khoản :" + userId, "user is empty");
        }
        if (shopInput == null) {
            throw new ServiceException("not_found", "Vui lòng truyền thông tin cửa hàng của bạn", "shop is empty");
        }

        if (null == shopInput.getAddress() || 0 == shopInput.getDistrict_id()
                || null == shopInput.getWardCode()) {
            throw new ServiceException("not_found", "Vui lòng truyền địa chỉ của cửa hàng bạn", "shopId is empty");
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
    public User createShop(@RequestParam("user-id") String userId, @RequestBody CreateShopInput shopInput) throws ServiceException {
        //validate shop
        validateShopInput(userId, shopInput);
        Shop shop = new Shop();
        ShopGHN shopGHN = createShopShipmentGHN(shopInput);
        String shopId = shopGHN.getShop_id();
        shop.setShopId(shopId);
        shop.setImageUrl(shopInput.getImageUrl());
        shop.setStatus(ActiveStatus.ACTIVE);
        shop.setName(shopInput.getName());
        shop.setDescription(shopInput.getDescription());

        return userManager.createShop(userId, shop);
    }

    private ShopGHN createShopShipmentGHN(CreateShopInput shopInput) throws ServiceException {
        ShopGHN shopGHN = null;
        try {
            shopGHN = getGHNClient().createShop(token, shopInput.getDistrict_id(),
                    shopInput.getWardCode(), shopInput.getName(), shopInput.getPhone(), shopInput.getAddress()).getData().get(0);


        } catch (ClientException e) {
            throw new ServiceException(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
        }
        return shopGHN;
    }


}
