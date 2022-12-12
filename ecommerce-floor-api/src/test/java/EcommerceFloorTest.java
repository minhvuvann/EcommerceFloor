import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.ecommercefloor.EcommerceFloorApplication;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.controller.RegisterController;
import vn.mellow.ecom.ecommercefloor.controller.ShopController;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.controller.UserController;
import vn.mellow.ecom.ecommercefloor.enums.GenderType;
import vn.mellow.ecom.ecommercefloor.enums.PasswordStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.model.input.*;
import vn.mellow.ecom.ecommercefloor.model.user.User;

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

}
