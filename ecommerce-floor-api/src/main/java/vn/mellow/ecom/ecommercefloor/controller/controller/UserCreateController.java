package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.KeyPasswordInput;
import vn.mellow.ecom.ecommercefloor.model.input.RoleInput;
import vn.mellow.ecom.ecommercefloor.model.input.UserInput;
import vn.mellow.ecom.ecommercefloor.model.user.KeyPassword;
import vn.mellow.ecom.ecommercefloor.model.user.Role;
import vn.mellow.ecom.ecommercefloor.model.user.SocialConnect;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;

@Component
public class UserCreateController {
    @Autowired
    private UserManager userManager;

    public User createUser(CreateUserInput createUserInput) throws ServiceException {
        //validateCreateUserInput
        validateCreateUserInput(createUserInput);
        SocialConnect socialConnect = null;
        UserInput userInput = createUserInput.getUser();
        User user = new User();
        user.setByUser(userInput.getByUser());
        user.setUsername(userInput.getUsername());
        user.setTelephone(userInput.getTelephone());
        UserStatus userStatus = UserStatus.ACTIVE;
        if (null != userInput.getUserStatus()) {
            userStatus = userInput.getUserStatus();

        }
        user.setUserStatus(userStatus);
        user.setGender(userInput.getGender());
        user.setEmail(userInput.getEmail());
        user.setFullName(userInput.getFullName());
        user.setBirthday(userInput.getBirthday());
        user.setDescription(userInput.getDescription());
        user.setAddress(userInput.getAddress());
        user.setImageUrl(userInput.getImageUrl());
        ServiceType serviceType = ServiceType.NORMALLY;
        if (null != userInput.getServiceType()) {
            serviceType = userInput.getServiceType();
        }
        user.setServiceType(serviceType);
        //set key password
        KeyPasswordInput keyPasswordInput = createUserInput.getPassword();
        KeyPassword keyPassword = new KeyPassword();
        String token = KeyUtils.getToken();
        String password = KeyUtils.hashBCryptEncoder(keyPasswordInput.getPassword() + token);
        keyPassword.setPassword(password);
        keyPassword.setToken(token);
        keyPassword.setNote(keyPasswordInput.getNote());
        PasswordStatus passwordStatus = PasswordStatus.NEW;
        if (null != keyPasswordInput.getPasswordStatus()) {
            passwordStatus = keyPasswordInput.getPasswordStatus();

        }
        keyPassword.setPasswordStatus(passwordStatus);

        //set role for user
        RoleInput roleInput = createUserInput.getRole();
        Role role = new Role();
        role.setNote(roleInput.getNote());
        role.setDescription(roleInput.getDescription());
        role.setRoleType(roleInput.getRoleType());
        RoleStatus roleStatus = RoleStatus.ACTIVE;
        if (null != roleInput.getRoleStatus()) {
            roleStatus = roleInput.getRoleStatus();
        }
        role.setRoleStatus(roleStatus);
        if (null != user.getServiceType() && !ServiceType.NORMALLY.equals(user.getServiceType())) {
            socialConnect = new SocialConnect();
            socialConnect.setName(user.getFullName());
            socialConnect.setEmail(user.getEmail());
            socialConnect.setImageUrl(user.getImageUrl());
            socialConnect.setServiceType(user.getServiceType());
            String socialName = ServiceType.GOOGLE.equals(user.getServiceType()) ? "Google" : "Facebook";
            socialConnect.setDescription("Login " + socialName);
            socialConnect.setNote("Tạo dịch vụ mạng xã hội kết nối");


        }


        return userManager.createUser(user, keyPassword, socialConnect, role);
    }

    private void validateCreateUserInput(CreateUserInput createUserInput) throws ServiceException {
        if (null == createUserInput) {
            throw new ServiceException("invalid_data", "createUserInput is null", "createUserInput is null");
        }
        if (null == createUserInput.getUser()) {
            throw new ServiceException("invalid_data", "Chưa điền thông tin cho user", "user is null");
        }
        if (null == createUserInput.getPassword() || null == createUserInput.getPassword().getPassword()) {
            throw new ServiceException("invalid_data", "chưa tạo mật khẩu cho user", "password is null");
        }
        if (null == createUserInput.getRole() || null == createUserInput.getRole().getRoleType()) {
            throw new ServiceException("invalid_data", "Chưa phân quyền cho user", "role is null");
        }
    }
}
