package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.base.filter.ResultList;
import vn.mellow.ecom.controller.UserCreateController;
import vn.mellow.ecom.controller.UserProfileController;
import vn.mellow.ecom.model.enums.RoleStatus;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.geo.Address;
import vn.mellow.ecom.model.input.CreateUserDTO;
import vn.mellow.ecom.model.input.UpdateInfoUserDTO;
import vn.mellow.ecom.model.input.UpdateStatusDTO;
import vn.mellow.ecom.model.user.Role;
import vn.mellow.ecom.model.user.User;
import vn.mellow.ecom.model.user.UserFilter;
import vn.mellow.ecom.model.user.UserProfile;
import vn.mellow.ecom.utils.KeyUtils;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/")
public class UserRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    @Autowired
    private UserManager userManager;

    @Autowired
    private UserCreateController userCreateController;

    @Autowired
    private UserProfileController userProfileController;

    @GetMapping()
    public ResponseEntity checkAPI() {
        return ResponseEntity.ok().body("User api is successfully authenticated");
    }

    @ApiOperation(value = "create new  user")
    @PostMapping("/user")
    public User createUser(@RequestBody CreateUserDTO createInput) throws ServiceException {
        return userCreateController.createUser(createInput);
    }

    @ApiOperation(value = "get user by user id")
    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable String userId) throws ServiceException {
        User data = userManager.getUser(userId);
        if (null == data) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin user", "Not found data user by id: " + userId);
        }
        return data;
    }

    @ApiOperation(value = "get user list")
    @GetMapping("/user/all")
    public List<User> getUserAll() {
        return userManager.getAlls();
    }

    @ApiOperation(value = "get user profile by user id")
    @GetMapping("/user/{userId}/profile")
    public UserProfile getUserProfile(@PathVariable String userId) throws ServiceException {
        return userProfileController.getUserProfile(userId);
    }

    @ApiOperation(value = "update user status by user Id")
    @PutMapping("/user/{userId}/status")
    public User updateUserStatus(@PathVariable String userId, @RequestBody UpdateStatusDTO statusBody) throws ServiceException {
        return userManager.updateUserStatus(userId, statusBody);
    }

    @ApiOperation(value = "Cập nhật trạng thái đang hoạt động cho tài khoản")
    @PutMapping("user/{userId}/active/{activeBy}")
    public User activeUser(@PathVariable String userId, @PathVariable String activeBy) throws ServiceException {
        return userManager.updateActiveUser(userId, activeBy);
    }

    @ApiOperation(value = "update info user by user Id")
    @PutMapping("/user/{userId}/info-basic")
    public User updateUserInfo(@PathVariable String userId, @RequestBody UpdateInfoUserDTO statusBody) throws ServiceException {
        getUser(userId);
        return userManager.updateInfoUser(userId, statusBody);
    }

    @ApiOperation(value = "update address user by user Id")
    @PutMapping("/user/{userId}/info-address")
    public User updateUserAddress(@PathVariable String userId, @RequestBody Address address) throws ServiceException {
        getUser(userId);
        return userManager.updateAddress(userId, address);
    }


    @ApiOperation(value = "update user password by user Id")
    @PutMapping("/user/{userId}/info-password")
    public User updatePassword(@PathVariable String userId, @RequestParam("pwd") String password) throws ServiceException {
        User user = getUser(userId);
        String token = KeyUtils.getToken();
        password = KeyUtils.SHA256(KeyUtils.decodeBase64Encoder(password) + token);
        userManager.updatePassword(userId, password);

        return user;
    }


    @ApiOperation(value = "find user")
    @PostMapping("/user/filter")
    public ResultList<User> searchUser(
            @RequestBody UserFilter userFilter) throws ServiceException {
        ResultList<User> resultList = userManager.filterUser(userFilter);
        ResultList<User> result = new ResultList<User>();
        List<User> userList = new ArrayList<User>();
        for (User user : resultList.getResultList()) {
            UserProfile userDetail = getUserProfile(user.getId());
            for (Role role : userDetail.getRoles()) {
                if (role.getRoleStatus().equals(RoleStatus.ACTIVE)) {
                    user.setRoleType(role.getRoleType());
                }
            }
            if (userFilter.getRoleType() != null) {
                for (Role role : userDetail.getRoles()) {
                    if (role.getRoleType().equals(userFilter.getRoleType())) {
                        userList.add(user);
                        break;
                    }
                }
            } else {
                userList.add(user);

            }
        }
        result.setResultList(userList);
        result.setTotal(userList.size());
        result.setIndex(resultList.getIndex());
        result.setMaxResult(resultList.getMaxResult());


        return result;
    }


    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllServiceException(ServiceException e) {
        LOGGER.error("ServiceException error.", e);
        return error(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return error("internal_server_error", "Có lỗi trong quá trình xử lý", e.getMessage());
    }
}
