package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.controller.controller.UserCreateController;
import vn.mellow.ecom.ecommercefloor.controller.controller.UserProfileController;
import vn.mellow.ecom.ecommercefloor.enums.UserStatus;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.UpdateInfoUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.UpdateStatusInput;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.model.user.UserFilter;
import vn.mellow.ecom.ecommercefloor.model.user.UserProfile;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/")
public class UserController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
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
    public User createUser(@RequestBody CreateUserInput createInput) throws ServiceException {
        return userCreateController.createUser(createInput);
    }

    @ApiOperation(value = "get user by user id")
    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable String userId) throws ServiceException {
        User data = userManager.getUser(userId);
        if (null == data || !data.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin user", "Not found data user by id: " + userId);
        }
        return data;
    }

    @ApiOperation(value = "get user profile by user id")
    @GetMapping("/user/{userId}/profile")
    public UserProfile getUserProfile(@PathVariable String userId) throws ServiceException {
        return userProfileController.getUserProfile(userId);
    }

    @ApiOperation(value = "update user status by user Id")
    @PutMapping("/user/{userId}/status")
    public User updateUserStatus(@PathVariable String userId, @RequestBody UpdateStatusInput statusBody) throws ServiceException {
        return userManager.updateUserStatus(userId, statusBody);
    }

    @ApiOperation(value = "update info user by user Id")
    @PutMapping("/user/{userId}/info-basic")
    public User updateUserInfo(@PathVariable String userId, @RequestBody UpdateInfoUserInput statusBody) throws ServiceException {
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
            @RequestBody UserFilter userFilter) {
        return userManager.filterUser(userFilter);
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
