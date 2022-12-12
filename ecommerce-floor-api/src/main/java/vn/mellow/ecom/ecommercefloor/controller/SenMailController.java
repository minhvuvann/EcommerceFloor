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
import vn.mellow.ecom.ecommercefloor.base.model.ResponseBody;
import vn.mellow.ecom.ecommercefloor.controller.controller.UserCreateController;
import vn.mellow.ecom.ecommercefloor.enums.BasicStatus;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateUserInput;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.model.user.UserFilter;
import vn.mellow.ecom.ecommercefloor.utils.GeneralIdUtils;
import vn.mellow.ecom.ecommercefloor.utils.SendMailUtils;

import javax.mail.Session;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mail/1.0.0/")
public class SenMailController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SenMailController.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserController userController;

    @Value("${ecommerce_floor.store.name}")
    private String storeName;
    @Value("${ecommerce_floor.store.mail}")
    private String emailEcommerce;
    @Value("${mellow.password}")
    private String passwordEcommerce;


    @ApiOperation(value = "send email")
    @PostMapping("/send")
    public ResponseBody sendMail(@RequestParam("email") String email) throws ServiceException {

        UserFilter filterUser = new UserFilter();
        filterUser.setEmail(email);
        filterUser.setServiceType(ServiceType.NORMALLY);
        User result = userController.searchUser(filterUser).getResultList().get(0);
        if (null != result) {
            String code = GeneralIdUtils.generateId();
            String subject = "Ma xac Thuc";
            String messSendMail = code + " la ma xac thuc OTP tai khoan tren san thuong mai MELLOW. " +
                    "De tranh bi mat tien, tuyet doi KHONG chia se ma nay voi bat ky ai.";
            //Send mail login
            Session session = SendMailUtils.loginMail(emailEcommerce, passwordEcommerce);
            //Send mail to customer
            SendMailUtils.sendMailTo(session, emailEcommerce, storeName, result.getEmail(), subject, messSendMail);
            //Send mail to store

            return new ResponseBody(BasicStatus.success, "Gửi mã xác thực thành công.", code);
        }
        else {
            return new ResponseBody(BasicStatus.failure,"Gửi mã xác thực không thành công.Không tìm thấy thông tin tài khoản","Register failed");
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
