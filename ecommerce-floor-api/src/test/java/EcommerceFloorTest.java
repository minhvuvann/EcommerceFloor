import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.ecommercefloor.EcommerceFloorApplication;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.controller.UserController;
import vn.mellow.ecom.ecommercefloor.enums.GenderType;
import vn.mellow.ecom.ecommercefloor.enums.PasswordStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.KeyPasswordInput;
import vn.mellow.ecom.ecommercefloor.model.input.RoleInput;
import vn.mellow.ecom.ecommercefloor.model.input.UserInput;
import vn.mellow.ecom.ecommercefloor.model.user.User;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = EcommerceFloorApplication.class)
@AutoConfigureMockMvc
class EcommerceFloorTest {
    @Autowired
    private UserController userController;

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
        user.setBirthday(new Date("11/02/2000"));
        user.setByUser(byUser);
        user.setEmail("nguyenvandat@gmail.com");
        user.setDescription("Tài khoản khách hàng");
        user.setFullName("Nguyễn Văn Đạt");
        user.setGender(GenderType.MAN);
        user.setServiceType(ServiceType.NORMALLY);
        user.setTelephone("0988883131");

        Address address = new Address();
        address.setAddress1("Dĩ An,Bình Dương");

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
            result = userController.createUser(createUserInput);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
