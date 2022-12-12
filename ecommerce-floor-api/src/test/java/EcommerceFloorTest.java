import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.ecommercefloor.EcommerceFloorApplication;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.base.model.MoneyV2;
import vn.mellow.ecom.ecommercefloor.controller.ProductController;
import vn.mellow.ecom.ecommercefloor.controller.RegisterController;
import vn.mellow.ecom.ecommercefloor.controller.ShopController;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.controller.UserController;
import vn.mellow.ecom.ecommercefloor.model.input.*;
import vn.mellow.ecom.ecommercefloor.model.product.Product;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;
import vn.mellow.ecom.ecommercefloor.model.size.DimensionMeasurement;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.utils.MoneyCalculateUtils;

import java.util.List;

import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = EcommerceFloorApplication.class)
@AutoConfigureMockMvc
class EcommerceFloorTest {
    @Autowired
    private UserController userController;

    @Autowired
    private RegisterController registerController;

    @Autowired
    private ShopController shopController;

    @Autowired
    private ProductController productController;



    private ActivityUser byUser;

    public EcommerceFloorTest() {
        this.byUser = new ActivityUser();
        byUser.setUserId("adminVVM");
        byUser.setUserName("adminTest001");
        byUser.setEmail("adminTest001@gmail.com");
        byUser.setPhone("0927382733");
    }

    @Test
    public void testUser() {
        CreateUserInput createUserInput = new CreateUserInput();
        UserInput user = new UserInput();
        user.setBirthday(new Date("11/02/2001"));
        user.setByUser(byUser);
        user.setEmail("19130137@st.hcmuaf.edu.vn");
        user.setDescription("Tài khoản khách hàng");
        user.setFullName("Nguyễn Văn Tú");
        user.setGender(GenderType.MAN);
        user.setServiceType(ServiceType.NORMALLY);
        user.setTelephone("0988883131");

        Address address = new Address();
        address.setAddress1("Phường Dĩ An, Thành Phố Dĩ An, Bình Dương");
        address.setWardCode(440504);
        address.setWard("Phường Dĩ An");
        address.setDistrict("Thành Phố Dĩ An");
        address.setDistrictCode(1540);
        address.setProvince("Bình Dương");
        address.setProvinceCode(205);
        user.setAddress(address);
        createUserInput.setUser(user);
        KeyPasswordInput password = new KeyPasswordInput();
        password.setPasswordStatus(PasswordStatus.NEW);
        password.setPassword("123456");
        createUserInput.setPassword(password);
        RoleInput roleInput = new RoleInput();
        roleInput.setRoleType(RoleType.PERSONAL);
        createUserInput.setRole(roleInput);
        User result = null;
        try {
            //Đăng kí tài khoản khách hàng (mua)
            result = (User) registerController.registerUser(createUserInput).getData();

            //Đăng kí tài khoản khách hàng (mua và bán)
            CreateShopInput shopInput = new CreateShopInput();
            shopInput.setWardCode("440504");
            shopInput.setDistrict_id(1540);
            shopInput.setAddress("Phường Dĩ An, Thành Phố Dĩ An, Bình Dương");
            shopInput.setPhone("0988883131");
            shopInput.setName("TuStore.IM");
            shopInput.setDescription("Cần cù bù siêng năng......");
            shopInput.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2F658862620477793791%2F&psig=AOvVaw0lxzNUGq18-gNWhWJhDmIi&ust=1670945680096000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLiBs--z9PsCFQAAAAAdAAAAABAE");

            result = shopController.createShop(result.getId(), shopInput);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct() {
        CreateProductInput createProductInput = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Máy chơi game");
        productInput.setShopId(562651);
        productInput.setIndustrialType(IndustrialType.ELECTRON);
        productInput.setIndustrialTypeName(IndustrialType.ELECTRON.getDescription());
        productInput.setFeaturedImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fgiobien.vn%2Fproducts%2Fmay-choi-game-cam-tay-steam-deck&psig=AOvVaw3dpXd4B8MBiX928V4dPcJf&ust=1670957295727000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLiOjZLf9PsCFQAAAAAdAAAAABAE");
productInput.setDescription("Như một vị bác học lỗi lạc đã từng nói: \" Học hành áp lực không tạo ra thiên tài, hãy cân bằng giữa việc học và giải trí. Đó mới là phương pháp thông minh \". Đừng để những đứa trẻ của chúng ta trở nên khô khan, máy móc khi xung quanh 24 giờ là sách vở, hãy biến các con thành thiên tài khi đan xem giữa chơi và học...\n" +
        "\n" +
        "Máy chơi game cầm tay M8 Plus thông minh thế hệ mới\n" +
        "\n" +
        "* Thông số kỹ thuật máy chơi game cầm tay M8 plus\n" +
        "- Tên máy : Máy chơi game cầm tay M8\n" +
        "- Phân phối : D9 Shop\n" +
        "- Màn hình : 4.0 Inch, tinh xảo, sắc nét\n" +
        "- Loa : Âm thanh to, sống động\n" +
        "- Màu sắc : đỏ xanh, đỏ đen, xanh đen,..\n" +
        "- Chất liệu : Nhựa ABS\n" +
        "- Loại trò chơi : 1000 game khác nhau\n" +
        "- Pin : Tích hợp pin sạc 1200 mah\n" +
        "- Pin : có thể sạc lại, bạn có thể sử dụng để làm việc trong khoảng 3-4 giờ\n" +
        "- Có hỗ trợ nhiều cổng kết nối, kết nối với màn hình lớn, tivi, laptop để cuộc vui tốt hơn\n" +
        "\n" +
        "* Tính năng sản phẩm :\n" +
        "- Game nhập vai vô cùng hot, càng chơi càng thích\n" +
        "- Hỗ trợ gần 1000 game khác nhau, game tư duy thông minh\n" +
        "- Nhiều game thông minh, giúp trẻ phát triển tư duy, sáng tạo\n" +
        "- Giảm bớt căng thẳng sau những ngày học hành vất vả\n" +
        "- Có thể chơi 2 người trên màn hình lớn như tivi, laptop ...\n" +
        "- Âm thanh, kết hợp với màn hình HD sắc nét giúp trẻ chơi không bị mỏi mắt");
        createProductInput.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://www.google.com/url?sa=i&url=https%3A%2F%2Fvnexpress.net%2Fmay-choi-game-man-hinh-oled-gia-hon-10-trieu-dong-4376168.html&psig=AOvVaw3dpXd4B8MBiX928V4dPcJf&ust=1670957295727000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLiOjZLf9PsCFQAAAAAdAAAAABAQ");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);

        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(39);
        dimensionMeasurement.setHeight(20);
        dimensionMeasurement.setLength(10);
        variant_1.setDimension(dimensionMeasurement);

        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(9000));

        productVariantList.add(variant_1);
        createProductInput.setProductVariants(productVariantList);
        try {
            productController.createProductVariant(createProductInput);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


}
