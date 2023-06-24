package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.base.model.ResponseResult;
import vn.mellow.ecom.controller.CreateCartController;
import vn.mellow.ecom.controller.UserCreateController;
import vn.mellow.ecom.model.enums.*;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.cart.Cart;
import vn.mellow.ecom.model.input.CreateUserDTO;
import vn.mellow.ecom.model.input.KeyPasswordDTO;
import vn.mellow.ecom.model.input.RoleDTO;
import vn.mellow.ecom.model.input.UserDTO;
import vn.mellow.ecom.model.user.Role;
import vn.mellow.ecom.model.user.User;
import vn.mellow.ecom.model.user.UserFilter;
import vn.mellow.ecom.utils.JwtUtils;
import vn.mellow.ecom.utils.KeyUtils;

import java.util.Base64;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/login")
public class LoginRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);
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
    public ResponseResult loginAdmin(
            @RequestParam("email") String email, @RequestParam("password") String password) {
        // validateLoginInput
        try {
            validateLoginInput(email, password, true, null, null);
            UserFilter userFilter = new UserFilter();
            userFilter.setEmail(email);
            User user = userManager.getUserByMail(email);
            if (null == user) {
                return new ResponseResult(0,
                        "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not admin");
            }
            RoleType roleType = null;
            List<Role> roleList = userManager.getAllRole(user.getId());
            for (Role role : roleList) {
                    if (role.getRoleType().equals(RoleType.ADMIN)) {
                        roleType = role.getRoleType();
                        break;
                    }

            }
            if (null == roleType) {
                return new ResponseResult(0,
                        "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not admin");
            }
            String decodePassword = KeyUtils.decodeBase64Encoder(password) + KeyUtils.getToken();
            Authentication auth = null;
            try {
                auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, KeyUtils.SHA256(decodePassword)));
            } catch (Exception e) {
                return new ResponseResult(0, e.getMessage(), e.getLocalizedMessage());

            }
            if (auth != null && auth.isAuthenticated()) {
                return new ResponseResult(1,
                        "Đăng nhập thành công", jwtUtils.generateToken(email));

            } else {
                return new ResponseResult(0,
                        "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not admin");

            }
        } catch (ServiceException e) {
            return new ResponseResult(0, e.getErrorMessage(), e.getErrorDetail());
        }
    }

    @ApiOperation(value = "Login account customer")
    @PostMapping("/customer")
    public ResponseResult loginCustomer(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
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
            //kiểm tra khi tài khoản đăng nhập là tài khoản thường
            if (ServiceType.NORMALLY.equals(serviceType)) {
                if (null == userUpdate) {
                    return new ResponseResult(0,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");
                }
                if (!UserStatus.ACTIVE.equals(userUpdate.getUserStatus())) {
                    return new ResponseResult(0,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");

                }
                RoleType roleType = null;
                List<Role> roleList = userManager.getAllRole(userUpdate.getId());
                for (Role role : roleList) {
                    // quyền đang hoạt động
                    if (RoleStatus.ACTIVE.equals(role.getRoleStatus()))
                        if (role.getRoleType().equals(RoleType.PERSONAL) ||
                                role.getRoleType().equals(RoleType.STORE) ||
                                role.getRoleType().equals(RoleType.PERSONAL_STORE)) {
                            roleType = role.getRoleType();
                            break;
                        }

                }
                if (null == roleType) {
                    return new ResponseResult(0,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");
                }
                String decodePassword = KeyUtils.decodeBase64Encoder(password) + KeyUtils.getToken();

                Authentication auth = null;
                try {
                    auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, KeyUtils.SHA256(decodePassword)));
                } catch (Exception e) {
                    return new ResponseResult(0, e.getMessage(), e.getLocalizedMessage());

                }
                if (auth != null && auth.isAuthenticated()) {
                    return new ResponseResult(1,
                            "Đăng nhập thành công", jwtUtils.generateToken(email));


                }

            } else {
                if (userUpdate == null) {
                    CreateUserDTO createUserInput = new CreateUserDTO();
                    UserDTO userDTO = new UserDTO();
                    userDTO.setImageUrl(imageUrl);
                    userDTO.setEmail(email);
                    userDTO.setServiceType(serviceType);
                    userDTO.setFullName(fullName);

                    KeyPasswordDTO keyPasswordDTO = new KeyPasswordDTO();
                    keyPasswordDTO.setPassword(String.valueOf(System.currentTimeMillis()));
                    keyPasswordDTO.setPasswordStatus(PasswordStatus.NEW);

                    RoleDTO role = new RoleDTO();
                    role.setRoleType(RoleType.PERSONAL);

                    createUserInput.setUser(userDTO);
                    createUserInput.setPassword(keyPasswordDTO);
                    createUserInput.setRole(role);
                    User result = userCreateController.createUser(createUserInput);

                    Cart cart = new Cart();
                    cart.setUserId(result.getId());
                    createCartController.createCart(cart, null);
                    return new ResponseResult(1,
                            "Đăng nhập thành công", result);
                } else {
                    return new ResponseResult(1,
                            "Đăng nhập thành công", userUpdate);

                }

            }
        } catch (ServiceException e) {
            return new ResponseResult(0, e.getErrorMessage(), e.getErrorDetail());
        }
        return new ResponseResult(0,
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
        if (users.isEmpty()) {
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
