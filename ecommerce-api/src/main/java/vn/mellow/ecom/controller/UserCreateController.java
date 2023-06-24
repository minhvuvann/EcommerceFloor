package vn.mellow.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.model.enums.*;
import vn.mellow.ecom.manager.ScoreManager;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.input.CreateUserDTO;
import vn.mellow.ecom.model.input.KeyPasswordDTO;
import vn.mellow.ecom.model.input.RoleDTO;
import vn.mellow.ecom.model.input.UserDTO;
import vn.mellow.ecom.model.user.*;
import vn.mellow.ecom.utils.KeyUtils;
import vn.mellow.ecom.utils.RemoveAccentUtils;

import java.util.List;

@Component
public class UserCreateController {
    @Autowired
    private UserManager userManager;

    @Autowired
    private ScoreManager scoreManager;

    public User createUser(CreateUserDTO createUserInput) throws ServiceException {
        //validateCreateUserInput
        validateCreateUserInput(createUserInput);
        UserDTO userDTO = createUserInput.getUser();
        User user = new User();
        user.setByUser(userDTO.getByUser());
        user.setUsername(userDTO.getUsername());
        if (null == userDTO.getUsername()) {
            user.setUsername(RemoveAccentUtils.generateUserName(userDTO.getFullName()));
        }
        user.setTelephone(userDTO.getTelephone());
        UserStatus userStatus = UserStatus.INACTIVE;
        if (null != userDTO.getUserStatus()) {
            userStatus = userDTO.getUserStatus();

        }
        user.setUserStatus(userStatus);
        user.setGender(userDTO.getGender());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setBirthday(userDTO.getBirthday());
        user.setAddress(userDTO.getAddress());
        user.setImageUrl(userDTO.getImageUrl());
        if (null == userDTO.getImageUrl()) {
            String imageUrl = userDTO.getFullName() == null ? user.getUsername() : userDTO.getFullName();
            user.setImageUrl("https://ui-avatars.com/api/?name=" + imageUrl.replaceAll(" ", ""));
        }
        ServiceType serviceType = ServiceType.NORMALLY;
        if (null != userDTO.getServiceType()) {
            serviceType = userDTO.getServiceType();
        }
        user.setServiceType(serviceType);
        user.setDescription("Login " + user.getServiceType().getDescription());
        //set key password
        KeyPasswordDTO keyPasswordDTO = createUserInput.getPassword();
        KeyPassword keyPassword = new KeyPassword();
        String token = KeyUtils.getToken();
        String password = KeyUtils.SHA256(KeyUtils.decodeBase64Encoder(keyPasswordDTO.getPassword()) + token);
        keyPassword.setPassword(password);
        keyPassword.setToken(token);
        keyPassword.setNote(keyPasswordDTO.getNote());
        PasswordStatus passwordStatus = PasswordStatus.NEW;
        if (null != keyPasswordDTO.getPasswordStatus()) {
            passwordStatus = keyPasswordDTO.getPasswordStatus();

        }
        keyPassword.setPasswordStatus(passwordStatus);

        //set role for user
        RoleDTO roleDTO = createUserInput.getRole();
        Role role = new Role();
        role.setNote(roleDTO.getNote());
        role.setDescription(roleDTO.getDescription());
        role.setRoleType(roleDTO.getRoleType());
        RoleStatus roleStatus = RoleStatus.ACTIVE;
        if (null != roleDTO.getRoleStatus()) {
            roleStatus = roleDTO.getRoleStatus();
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
                throw new ServiceException("exist_account", "Email của bạn đã tồn tại", "Email user is exists");
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

    private void validateCreateUserInput(CreateUserDTO createUserInput) throws ServiceException {
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
        if (!ServiceType.isExist(createUserInput.getUser().getServiceType().toString())) {
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
