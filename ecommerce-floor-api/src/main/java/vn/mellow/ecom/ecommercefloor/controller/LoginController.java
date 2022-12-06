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
import sun.security.util.Password;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.model.ResponseBody;
import vn.mellow.ecom.ecommercefloor.controller.controller.UserCreateController;
import vn.mellow.ecom.ecommercefloor.enums.BasicStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.input.KeyPasswordInput;
import vn.mellow.ecom.ecommercefloor.model.input.RoleInput;
import vn.mellow.ecom.ecommercefloor.model.input.UserInput;
import vn.mellow.ecom.ecommercefloor.model.user.Role;
import vn.mellow.ecom.ecommercefloor.model.user.SocialConnect;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.model.user.UserFilter;
import vn.mellow.ecom.ecommercefloor.utils.JwtUtils;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;
import vn.mellow.ecom.ecommercefloor.utils.TypeUtils;

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

    private void validateLoginInput(String email, String password, boolean admin, ServiceType serviceType, String fullName) throws ServiceException {
        if (null == email) {
            throw new ServiceException("invalid_data", "Chưa nhập thông tin Email", "Email is null");

        }
        if (null == password) {
            throw new ServiceException("invalid_data", "Chưa nhập mật khẩu", "Password is null");

        }
        if (!admin)
            if (null == serviceType ||
                    !TypeUtils.isServiceType(serviceType.toString())) {
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
            User user = null;
            for (User u : users) {
                List<SocialConnect> socialConnectList = userManager.getAllSocialConnect(user.getId());
                for (SocialConnect socialConnect : socialConnectList) {
                    if (socialConnect.getServiceType().equals(serviceType)) {
                        user = null;
                        break;
                    }
                }
            }
            if (ServiceType.NORMALLY.equals(serviceType)) {
                if (null == user) {
                    return new ResponseBody(BasicStatus.failure,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");
                }
                RoleType roleType = null;
                List<Role> roleList = userManager.getAllRole(user.getId());
                for (Role role : roleList) {
                    if (RoleStatus.ACTIVE.equals(role.getRoleStatus()))
                        if (role.getRoleType().equals(RoleType.PERSONAL) ||
                                roleType.equals(RoleType.STORE) || roleType.equals(RoleType.PERSONAL_STORE)) {
                            roleType = role.getRoleType();
                            break;
                        }

                }
                if (null == roleType) {
                    return new ResponseBody(BasicStatus.failure,
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");
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
                            "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");

                }

            } else {
                if (user == null) {
                    CreateUserInput createUserInput = new CreateUserInput();
                    UserInput userInput = new UserInput();
                    userInput.setImageUrl(imageUrl);
                    userInput.setEmail(email);
                    userInput.setFullName(fullName);

                    KeyPasswordInput keyPasswordInput = new KeyPasswordInput();
                    keyPasswordInput.setPassword(password);

                    RoleInput role = new RoleInput();
                    role.setRoleType(RoleType.PERSONAL);

                    createUserInput.setUser(userInput);
                    createUserInput.setPassword(keyPasswordInput);
                    createUserInput.setRole(role);
                    userCreateController.createUser(createUserInput);
                }else {
                    return new ResponseBody(BasicStatus.success,
                            "Đăng nhập thành công", user);

                }

            }
        } catch (ServiceException e) {
            return new ResponseBody(BasicStatus.failure, e.getErrorMessage(), e.getErrorDetail());
        }
        return new ResponseBody(BasicStatus.failure,
                "Đăng nhập thất bại. Không tìm thấy thông tin tài khoản", "Account is not personal");


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
