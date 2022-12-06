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
import vn.mellow.ecom.ecommercefloor.enums.BasicStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.user.Role;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.model.user.UserFilter;
import vn.mellow.ecom.ecommercefloor.utils.JwtUtils;
import vn.mellow.ecom.ecommercefloor.utils.KeyUtils;

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

    private void validateLoginInput(String email, String password) throws ServiceException {
        if (null == email) {
            throw new ServiceException("invalid_data", "Chưa nhập thông tin Email", "Email is null");

        }
        if (null == password) {
            throw new ServiceException("invalid_data", "Chưa nhập mật khẩu", "Password is null");

        }
    }

    @ApiOperation(value = "Login account admin")
    @PostMapping("/admin")
    public ResponseBody loginAdmin(
            @RequestParam("email") String email, @RequestParam("password") String password) {
        // validateLoginInput
        try {
            validateLoginInput(email, password);
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
            Authentication auth =null;
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
            @RequestParam("email") String email, @RequestParam("password") String password) {
        // validateLoginInput
        try {
            validateLoginInput(email, password);
            UserFilter userFilter = new UserFilter();
            userFilter.setEmail(email);
            User user = userManager.filterUser(userFilter).getResultList().get(0);
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

            Authentication auth =null;
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
        } catch (ServiceException e) {
            return new ResponseBody(BasicStatus.failure, e.getErrorMessage(), e.getErrorDetail());
        }
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
