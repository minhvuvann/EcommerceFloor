package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.manager.ScoreManager;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.KeyPasswordInput;
import vn.mellow.ecom.ecommercefloor.model.input.RoleInput;
import vn.mellow.ecom.ecommercefloor.model.input.UserInput;
import vn.mellow.ecom.ecommercefloor.model.user.*;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;
import vn.mellow.ecom.ecommercefloor.utils.RemoveAccentUtils;

import java.util.List;

@Component
public class UserCreateController {
    @Autowired
    private UserManager userManager;

    @Autowired
    private ScoreManager scoreManager;

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
        user.setAddress(userInput.getAddress());
        user.setImageUrl(userInput.getImageUrl());
        if (null == userInput.getImageUrl()) {
            String imageUrl = userInput.getFullName() == null ? user.getUsername() : userInput.getFullName();
            user.setImageUrl("https://ui-avatars.com/api/?name=" + imageUrl.replaceAll(" ", ""));
        }
        ServiceType serviceType = ServiceType.NORMALLY;
        if (null != userInput.getServiceType()) {
            serviceType = userInput.getServiceType();
        }
        user.setServiceType(serviceType);
        user.setDescription("Login " + user.getServiceType().getDescription());
        //set key password
        KeyPasswordInput keyPasswordInput = createUserInput.getPassword();
        KeyPassword keyPassword = new KeyPassword();
        String token = KeyUtils.getToken();
        String password = KeyUtils.SHA256(KeyUtils.decodeBase64Encoder(keyPasswordInput.getPassword()) + token);
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
        UserFilter userFilter = new UserFilter();
        if (null != createUserInput.getUser().getEmail()) {
            userFilter.setEmail(createUserInput.getUser().getEmail());
            userFilter.setServiceType(createUserInput.getUser().getServiceType());
        }
        List<User> userList = userManager.filterUser(userFilter).getResultList();

        if (userList.size() != 0) {
            if (UserStatus.ACTIVE.equals(userList.get(0).getUserStatus())) {
                throw new ServiceException("exist_account", "Email của bạn đã được đăng ký.( " + createUserInput.getUser().getEmail() + " )", "Email user is exists");
            }
            if (UserStatus.INACTIVE.equals(userList.get(0).getUserStatus())) {
                userManager.updatePassword(userList.get(0).getId(), password);
                return userManager.getUser(userList.get(0).getId());

            }
        }
        user = userManager.createUser(user, keyPassword, role);
        // create score
        Score score = new Score();
        score.setUserId(user.getId());
        score.setType(ScoreType.RANK_BRONZE);
        score.setScore(0);
        scoreManager.createScore(score);
        return user;
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
                !UserStatus.isExist(createUserInput.getUser().getUserStatus().toString())) {
            throw new ServiceException("exists_status", "Trạng thái của user không tồn tại.( " + UserStatus.getListName() + " )", "Status user is not exists");
        }
        if (null != createUserInput.getUser().getGender() &&
                !GenderType.isExist(createUserInput.getUser().getGender().toString())) {
            throw new ServiceException("exists_type", "Loại giới tính không tồn tại.( " + GenderType.getListName() + " )", "gender type is not exists");
        }
        if (null == createUserInput.getUser().getServiceType() ||
                !ServiceType.isExist(createUserInput.getUser().getServiceType().toString())) {
            throw new ServiceException("exists_type", "Loại dịch vụ không tồn tại. ( " + ServiceType.getListName() + " )", "service type is not exists");
        }


        if (null == createUserInput.getPassword() || null == createUserInput.getPassword().getPassword()
                || createUserInput.getPassword().getPassword().length() == 0
                || "null".equalsIgnoreCase(createUserInput.getPassword().getPassword())) {
            throw new ServiceException("invalid_data", "Chưa tạo mật khẩu cho user", "password is null");
        }
        if (null == createUserInput.getPassword().getPasswordStatus() &&
                !PasswordStatus.isExist(createUserInput.getPassword().getPasswordStatus().toString())) {
            throw new ServiceException("exists_status", "Trạng thái của password không tồn tại.( " + PasswordStatus.getListName() + " )", "Status password is not exists");

        }
        if (null == createUserInput.getRole() || null == createUserInput.getRole().getRoleType()) {
            throw new ServiceException("invalid_data", "Chưa phân quyền cho user", "role is null");
        }
        if (null != createUserInput.getRole().getRoleStatus()
                && !RoleStatus.isExist(createUserInput.getRole().getRoleStatus())) {
            throw new ServiceException("exists_status", "Trạng thái của role không tồn tại.( " + RoleStatus.getListName() + " )", "Status role is not exists");

        }
        if (!RoleType.isExist(createUserInput.getRole().getRoleType().toString())) {
            throw new ServiceException("exists_type", "Loại phân quyền không tồn tại. ( " + RoleType.getListName() + " )", "Role type is not exists");

        }
    }

}
