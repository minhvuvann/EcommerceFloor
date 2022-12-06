package vn.mellow.ecom.ecommercefloor.controller.controller;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.KeyPasswordInput;
import vn.mellow.ecom.ecommercefloor.model.input.RoleInput;
import vn.mellow.ecom.ecommercefloor.model.input.UserInput;
import vn.mellow.ecom.ecommercefloor.model.user.*;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;
import vn.mellow.ecom.ecommercefloor.utils.RemoveAccentUtils;
import vn.mellow.ecom.ecommercefloor.utils.StatusUtils;
import vn.mellow.ecom.ecommercefloor.utils.TypeUtils;

import java.util.List;

@Component
public class UserCreateController {
    @Autowired
    private UserManager userManager;

    public User createUser(CreateUserInput createUserInput) throws ServiceException {
        //validateCreateUserInput
        validateCreateUserInput(createUserInput);
        UserInput userInput = createUserInput.getUser();
        User user = new User();
        user.setByUser(userInput.getByUser());
        user.setUsername(userInput.getUsername());
        if (null == userInput.getUsername()) {
            user.setUsername(RemoveAccentUtils.generateUserName(userInput.getFullName()));
        }
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
        user.setDescription("Login " + user.getServiceType().getDescription());
        user.setAddress(userInput.getAddress());
        user.setImageUrl(userInput.getImageUrl());
        if (null == userInput.getImageUrl()) {

            user.setImageUrl("https://ui-avatars.com/api/?name=" +
                    userInput.getFullName() == null ? user.getUsername() : userInput.getFullName());
        }
        ServiceType serviceType = ServiceType.NORMALLY;
        if (null != userInput.getServiceType()) {
            serviceType = userInput.getServiceType();
        }
        user.setServiceType(serviceType);
        //set key password
        KeyPasswordInput keyPasswordInput = createUserInput.getPassword();
        KeyPassword keyPassword = new KeyPassword();
        String token = KeyUtils.getToken();
        String password = KeyUtils.SHA256(keyPasswordInput.getPassword() + token);
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


        return userManager.createUser(user, keyPassword, role);
    }

    private void validateCreateUserInput(CreateUserInput createUserInput) throws ServiceException {
        if (null == createUserInput) {
            throw new ServiceException("invalid_data", "createUserInput is null", "createUserInput is null");
        }
        if (null == createUserInput.getUser()) {
            throw new ServiceException("invalid_data", "Chưa điền thông tin cho user", "user is null");
        }
        if (null == createUserInput.getUser().getEmail()) {
            throw new ServiceException("invalid_data", "Chưa điền thông tin email", "email is null");
        }

        if (null != createUserInput.getUser().getUserStatus() &&
                !StatusUtils.isUserStatus(createUserInput.getUser().getUserStatus().toString())) {
            throw new ServiceException("exists_status", "Trạng thái của user không tồn tại.( " + UserStatus.getListName() + " )", "Status user is not exists");
        }
        if (null != createUserInput.getUser().getGender() &&
                !TypeUtils.isGenderType(createUserInput.getUser().getGender().toString())) {
            throw new ServiceException("exists_type", "Loại giới tính không tồn tại.( " + GenderType.getListName() + " )", "gender type is not exists");
        }
        if (null == createUserInput.getUser().getServiceType() ||
                !TypeUtils.isServiceType(createUserInput.getUser().getServiceType().toString())) {
            throw new ServiceException("exists_type", "Loại dịch vụ không tồn tại. ( " + ServiceType.getListName() + " )", "service type is not exists");
        }
        UserFilter userFilter = new UserFilter();
        if (null != createUserInput.getUser().getEmail()) {
            userFilter.setEmail(createUserInput.getUser().getEmail());
            userFilter.setServiceType(createUserInput.getUser().getServiceType());
        }
        List<User> userList = userManager.filterUser(userFilter).getResultList();

        if (userList.size() != 0) {
            throw new ServiceException("exist_account", "Email của bạn đã được đăng ký.( " + createUserInput.getUser().getEmail() + " )", "Email user is exists");

        }
        if (null == createUserInput.getPassword() || null == createUserInput.getPassword().getPassword()
                || createUserInput.getPassword().getPassword().length() == 0
                || "null".equalsIgnoreCase(createUserInput.getPassword().getPassword())) {
            throw new ServiceException("invalid_data", "Chưa tạo mật khẩu cho user", "password is null");
        }
        if (null == createUserInput.getPassword().getPasswordStatus() &&
                !StatusUtils.isPasswordStatus(createUserInput.getPassword().getPasswordStatus().toString())) {
            throw new ServiceException("exists_status", "Trạng thái của password không tồn tại.( " + PasswordStatus.getListName() + " )", "Status password is not exists");

        }
        if (null == createUserInput.getRole() || null == createUserInput.getRole().getRoleType()) {
            throw new ServiceException("invalid_data", "Chưa phân quyền cho user", "role is null");
        }
        if (null != createUserInput.getRole().getRoleStatus()
                && !StatusUtils.isRoleStatus(createUserInput.getRole().getRoleStatus().toString())) {
            throw new ServiceException("exists_status", "Trạng thái của role không tồn tại.( " + RoleStatus.getListName() + " )", "Status role is not exists");

        }
        if (!TypeUtils.isRoleType(createUserInput.getRole().getRoleType().toString())) {
            throw new ServiceException("exists_type", "Loại phân quyền không tồn tại. ( " + RoleType.getListName() + " )", "Role type is not exists");

        }
    }
}
