package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.client.BankClient;
import vn.mellow.ecom.ecommercefloor.model.bank.Bank;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/bank/1.0.0/")
public class BankController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(BankController.class);
    @Value("${BankClient.url}")
    private String bankService;
    @Value("${qr.url}")
    private String qrService;
    @Value("${BankID}")
    private String bankID;
    @Value("${account.number.store.bank}")
    private String accountNumber;
    @Value("${template}")
    private String template;
    @Value("${add.info}")
    private String addInfo;
    @Value("${account.name}")
    private String accountName;

    @ApiOperation(value = "Get list bank")
    @GetMapping("banks")
    public List<Bank> getBanks() throws ServiceException {
        BankClient bankClient = null;
        List<Bank> banks = new ArrayList<>();
        try {
            bankClient = new BankClient(bankService);
            banks = bankClient.getBankList().getData();

        } catch (ClientException e) {
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e.getErrorDetail());
        }
        return banks;
    }

    @ApiOperation(value = "Get qr code information bank store")
    @PostMapping("qr-code-info/{bank-id}/account-no/{account-no}/template/{template}")
    public URL getQrImage(@PathVariable("bank-id") String bankId, @PathVariable("account-no") String accountNo,
                          @PathVariable("template") String template, @RequestParam("amount") double amount,
                          @RequestParam("addInfo") String addInfo, @RequestParam("account-name") String accountName) throws ServiceException {
        URL urlImageQr = null;
        BankClient bankClient = null;

        try {
            bankClient = new BankClient(qrService);
            if (null == bankId || bankId.length() == 0 ||
                    "String".equalsIgnoreCase(bankId) || "null".equalsIgnoreCase(bankId)) {
                bankId = this.bankID;

            }
            if (null == accountNo || accountNo.length() == 0 ||
                    "String".equalsIgnoreCase(accountNo) || "null".equalsIgnoreCase(accountNo)) {
                accountNo = this.accountNumber;

            }
            if (null == template || template.length() == 0 ||
                    "String".equalsIgnoreCase(template) || "null".equalsIgnoreCase(template)) {
                template = this.template;

            }
            if (null == addInfo || addInfo.length() == 0 ||
                    "String".equalsIgnoreCase(addInfo) || "null".equalsIgnoreCase(addInfo)) {
                addInfo = this.addInfo;

            }
            if (null == accountName || accountName.length() == 0 ||
                    "String".equalsIgnoreCase(accountName) || "null".equalsIgnoreCase(accountName)) {
                accountName = this.accountName;

            }
            if (amount <= 0) {
                throw new ServiceException("not_found", "Chưa nhập số tiền cần thanh toán", "amoun > 0 ");

            }

            urlImageQr = bankClient.getQRImage(bankId, accountNo, template, amount, addInfo, accountName);

        } catch (ClientException | MalformedURLException e) {
            e.printStackTrace();

        }
        return urlImageQr;
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
