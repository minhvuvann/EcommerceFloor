package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.user.User;

@Component
public class UserCreateController {
    @Autowired
    private UserManager userManager;

//    public User createUser(CreateUserInput createUserInput) {
//
//    }
}
