package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.user.SocialConnect;
import vn.mellow.ecom.ecommercefloor.model.user.User;

@Component
public class UserCreateController {
    @Autowired
    private UserManager userManager;

    public User createUser(CreateUserInput createUserInput) throws ServiceException {
        //validateCreateUserInput
        validateCreateUserInput(createUserInput);
        SocialConnect socialConnect = null;
        User user = createUserInput.getUser();
        if (null != user.getServiceType() && !ServiceType.NORMALLY.equals(user.getServiceType())) {
            socialConnect = new SocialConnect();
            socialConnect.setServiceType(user.getServiceType());
            String socialName = ServiceType.GOOGLE.equals(user.getServiceType()) ? "Google" : "Facebook";
            socialConnect.setDescription("Login "+socialName);
            socialConnect.setNote("Tạo dịch vụ mạng xã hội kết nối");


        }


        return userManager.createUser(user, createUserInput.getPassword(), socialConnect);
    }

    private void validateCreateUserInput(CreateUserInput createUserInput) throws ServiceException {
        if (null == createUserInput) {
            throw new ServiceException("invalid_data", "createUserInput is null", "createUserInput is null");
        }
        if (null == createUserInput.getUser()) {
            throw new ServiceException("invalid_data", "user is null", "user is null");
        }
        if (null == createUserInput.getPassword()) {
            throw new ServiceException("invalid_data", "password is null", "password is null");
        }
    }
}
