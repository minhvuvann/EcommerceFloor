package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.model.ResponseBody;
import vn.mellow.ecom.ecommercefloor.controller.controller.CreateCartController;
import vn.mellow.ecom.ecommercefloor.controller.controller.UserCreateController;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.cart.Cart;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.KeyPasswordInput;
import vn.mellow.ecom.ecommercefloor.model.input.RoleInput;
import vn.mellow.ecom.ecommercefloor.model.input.UserInput;
import vn.mellow.ecom.ecommercefloor.model.user.Role;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.model.user.UserFilter;
import vn.mellow.ecom.ecommercefloor.utils.JwtUtils;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;

import java.util.Base64;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/login")
public class LoginController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserCreateController userCreateController;

    @Autowired
    private CreateCartController createCartController;

    private void validateLoginInput(String email, String password, boolean admin, ServiceType serviceType, String fullName) throws ServiceException {
        if (null == email) {
            throw new ServiceException("invalid_data", "Chưa nhập thông tin Email", "Email is null");

        }
        if (ServiceType.NORMALLY.equals(serviceType))
            if (null == password) {
                throw new ServiceException("invalid_data", "Chưa nhập mật khẩu", "Password is null");

            }
        if (!admin)
            if (null == serviceType ||
                    !ServiceType.isExist(serviceType.toString())) {
                throw new ServiceException("exists_type", "Loại dịch vụ không tồn tại. ( " + ServiceType.getListName() + " )", "service type is not exists");
            }
    }

    @ApiOperation(value = "Login account admin")
    @PostMapping("/admin")
    public ResponseBody loginAdmin(
            @RequestParam("email") String email, @RequestParam("password") String password) {
        // validateLoginInput
        try {
            validateLoginInput(email, password, true, null, null);
            UserFilter userFilter = new UserFilter();
            userFilter.setEmail(email);
            User user = userManager.filterUser(userFilter).getResultList().get(0);
            RoleType roleType = null;
            List<Role> roleList = userManager.getAllRole(user.getId());
            for (Role role : roleList) {
                if (RoleStatus.ACTIVE.equals(role.getRoleStatus()))
                    if (role.getRoleType().equals(RoleType.ADMIN)) {
                        roleType = role.getRoleType();
                        break;
                    }

            }
            if (null == roleType) {
                return new ResponseBody(BasicStatus.failure,
                        "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not admin");
            }
            String decodePassword = KeyUtils.decodeBase64Encoder(password) + KeyUtils.getToken();
            Authentication auth = null;
            try {
                auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, KeyUtils.SHA256(decodePassword)));
            } catch (Exception e) {
                return new ResponseBody(BasicStatus.failure, e.getMessage(), e.getLocalizedMessage());

            }
            if (auth != null && auth.isAuthenticated()) {
                return new ResponseBody(BasicStatus.success,
                        "Đăng nhập thành công", jwtUtils.generateToken(email));

            } else {
                return new ResponseBody(BasicStatus.failure,
                        "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not admin");

            }
        } catch (ServiceException e) {
            return new ResponseBody(BasicStatus.failure, e.getErrorMessage(), e.getErrorDetail());
        }
    }

    @ApiOperation(value = "Login account customer")
    @PostMapping("/customer")
    public ResponseBody loginCustomer(
            @RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("service-type") ServiceType serviceType,
            @RequestParam("full-name") String fullName,
            @RequestParam("image") String imageUrl) {
        // validateLoginInput
        try {
            validateLoginInput(email, password, false, serviceType, fullName);
            UserFilter userFilter = new UserFilter();
            userFilter.setEmail(email);
            List<User> users = userManager.filterUser(userFilter).getResultList();
            User userUpdate = null;
            for (User user : users) {
                if (user.getServiceType().equals(serviceType)) {
                    userUpdate = user;
                    break;
                }
            }
            if (ServiceType.NORMALLY.equals(serviceType)) {
                if (null == userUpdate) {
                    return new ResponseBody(BasicStatus.failure,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");
                }
                if (UserStatus.INACTIVE.equals(userUpdate.getUserStatus())) {
                    return new ResponseBody(BasicStatus.failure,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");

                }
                RoleType roleType = null;
                List<Role> roleList = userManager.getAllRole(userUpdate.getId());
                for (Role role : roleList) {
                    if (RoleStatus.ACTIVE.equals(role.getRoleStatus()))
                        if (role.getRoleType().equals(RoleType.PERSONAL) ||
                                role.equals(RoleType.STORE) || role.equals(RoleType.PERSONAL_STORE)) {
                            roleType = role.getRoleType();
                            break;
                        }

                }
                if (null == roleType) {
                    return new ResponseBody(BasicStatus.failure,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");
                }
                if (ServiceType.NORMALLY.equals(serviceType)) {
                    String decodePassword = KeyUtils.decodeBase64Encoder(password) + KeyUtils.getToken();

                    Authentication auth = null;
                    try {
                        auth = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(email, KeyUtils.SHA256(decodePassword)));
                    } catch (Exception e) {
                        return new ResponseBody(BasicStatus.failure, e.getMessage(), e.getLocalizedMessage());

                    }
                    if (auth != null && auth.isAuthenticated()) {

                        return new ResponseBody(BasicStatus.success,
                                "Đăng nhập thành công", jwtUtils.generateToken(email));

                    } else {
                        return new ResponseBody(BasicStatus.failure,
                                "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");

                    }
                }

            } else {
                if (userUpdate == null) {
                    CreateUserInput createUserInput = new CreateUserInput();
                    UserInput userInput = new UserInput();
                    userInput.setImageUrl(imageUrl);
                    userInput.setEmail(email);
                    userInput.setServiceType(serviceType);
                    userInput.setFullName(fullName);

                    KeyPasswordInput keyPasswordInput = new KeyPasswordInput();
                    keyPasswordInput.setPassword(String.valueOf(System.currentTimeMillis()));
                    keyPasswordInput.setPasswordStatus(PasswordStatus.NEW);

                    RoleInput role = new RoleInput();
                    role.setRoleType(RoleType.PERSONAL);

                    createUserInput.setUser(userInput);
                    createUserInput.setPassword(keyPasswordInput);
                    createUserInput.setRole(role);
                    User result = userCreateController.createUser(createUserInput);

                    Cart cart = new Cart();
                    cart.setUserId(result.getId());
                    createCartController.createCart(cart, null);
                } else {
                    return new ResponseBody(BasicStatus.success,
                            "Đăng nhập thành công", userUpdate);

                }

            }
        } catch (ServiceException e) {
            return new ResponseBody(BasicStatus.failure, e.getErrorMessage(), e.getErrorDetail());
        }
        return new ResponseBody(BasicStatus.failure,
                "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");


    }

    @ApiOperation(value = "get info account login by token")
    @GetMapping("/info")
    public User getUser(@RequestParam("code-token") String token, @RequestParam("service-type") ServiceType serviceType) throws ServiceException {
        if (null == serviceType ||
                !ServiceType.isExist(serviceType.toString())) {
            throw new ServiceException("exists_type", "Loại dịch vụ không tồn tại. ( " + ServiceType.getListName() + " )", "service type is not exists");
        }
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        String sub = payload.split("\\,")[0];
        String email = sub.substring(8, sub.length() - 1);
        UserFilter filter = new UserFilter();
        filter.setEmail(email);
        filter.setServiceType(serviceType);
        List<User> users = userManager.filterUser(filter).getResultList();
        if (users.isEmpty() || null == users || users.size() == 0) {
            throw new ServiceException("exists_account", "Không tìm thấy tài khoản", "user is not exists");

        }
        return users.get(0);
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
