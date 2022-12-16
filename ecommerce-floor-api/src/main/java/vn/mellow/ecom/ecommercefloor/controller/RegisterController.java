package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.controller.controller.CreateCartController;
import vn.mellow.ecom.ecommercefloor.controller.controller.UserCreateController;
import vn.mellow.ecom.ecommercefloor.enums.BasicStatus;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.cart.Cart;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.base.model.ResponseBody;
import vn.mellow.ecom.ecommercefloor.utils.GeneralIdUtils;
import vn.mellow.ecom.ecommercefloor.utils.SendMailUtils;

import javax.mail.Session;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user/1.0.0/register")
public class RegisterController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserCreateController userCreateController;

    @Value("${ecommerce_floor.store.name}")
    private String storeName;
    @Value("${ecommerce_floor.store.mail}")
    private String emailEcommerce;
    @Value("${mellow.password}")
    private String passwordEcommerce;
    @Autowired
    private CreateCartController createCartController;

    @ApiOperation(value = "create new  user")
    @PostMapping("/user")
    public ResponseBody registerUser(@RequestBody CreateUserInput createInput) throws ServiceException {
        User result = userCreateController.createUser(createInput);
        if (null != result) {
            String code = GeneralIdUtils.generateId();
            String subject = "Ma xac Thuc";
            String messSendMail = code + " la ma xac thuc OTP dang ky tai khoan tren san thuong mai MELLOW. " +
                    "De tranh bi mat tien, tuyet doi KHONG chia se ma nay voi bat ky ai.";
            //Send mail login
            Session session = SendMailUtils.loginMail(emailEcommerce, passwordEcommerce);
            //Send mail to customer
            SendMailUtils.sendMailTo(session, emailEcommerce, storeName, result.getEmail(), subject, messSendMail);
            //Send mail to store

            return new ResponseBody(BasicStatus.success, code, result);
        } else {
            Cart cart = new Cart();
            cart.setUserId(result.getId());
            createCartController.createCart(cart, null);

            return new ResponseBody(BasicStatus.failure, "Đăng ký tài khoản không thành công", "Register failed");
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
