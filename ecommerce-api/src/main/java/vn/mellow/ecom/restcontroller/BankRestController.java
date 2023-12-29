package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ClientException;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.base.model.ResponseResult;
import vn.mellow.ecom.base.client.BankClient;
import vn.mellow.ecom.model.bank.Bank;
import vn.mellow.ecom.model.bank.RecordHistory;
import vn.mellow.ecom.model.bank.ResultHistory;
import vn.mellow.ecom.model.bank.SyncBank;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/bank/1.0.0/")
public class BankRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(BankRestController.class);
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
    @Value("${casso.token}")
    private String cassoToken;
    @Value("${casso.service}")
    private String cassoService;

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

    @ApiOperation(value = "Get list bank")
    @GetMapping("bank/history")
    @Caching(
            put = {@CachePut(value = "ecommerce_floor", condition = "#clearCache==@environment.getProperty('app.cache.clearKey')")},
            cacheable = {@Cacheable(value = "ecommerce_floor", condition = "#clearCache!=@environment.getProperty('app.cache.clearKey')")})
    public ResultHistory getHistoryBank() throws ServiceException {
        BankClient bankClient = null;
        ResultHistory resultHistory = null;
        try {
            bankClient = new BankClient(cassoService);
            resultHistory = bankClient.getHistoryBank(cassoToken);

        } catch (ClientException e) {
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e.getErrorDetail());
        }
        return resultHistory;
    }

    @ApiOperation(value = "Kiểm tra đã thanh toán chưa")
    @GetMapping("bank/history/check-payment")
    @Caching(
            put = {@CachePut(value = "ecommerce_floor", condition = "#clearCache==@environment.getProperty('app.cache.clearKey')")},
            cacheable = {@Cacheable(value = "ecommerce_floor", condition = "#clearCache!=@environment.getProperty('app.cache.clearKey')")})
    public ResponseResult checkPayment(@RequestParam double amount) throws ServiceException {
        BankClient bankClient = null;
        ResultHistory resultHistory = null;
        try {
            bankClient = new BankClient(cassoService);
              SyncBank syncBank = bankClient.syncBankNow(accountNumber, cassoToken);
            resultHistory = bankClient.getHistoryBank(cassoToken);
                if (resultHistory != null) {
                    RecordHistory recordHistory = resultHistory.getData().getRecords().get(0);
                    if (recordHistory.getAmount() - amount == 0) {
                        return new ResponseResult(1, "Đã được thanh toán", true);
                    } else
                        return new ResponseResult(1, "Chưa được thanh toán", false);

                }

        } catch (ClientException e) {
            return new ResponseResult(0, e.getErrorCode(), e.getMessage());
        }
        return new ResponseResult(0, "Chưa được thanh toán", false);

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
            if (null == bankId || bankId.isEmpty() ||
                    "String".equalsIgnoreCase(bankId) || "null".equalsIgnoreCase(bankId)) {
                bankId = this.bankID;

            }
            if (null == accountNo || accountNo.isEmpty() ||
                    "String".equalsIgnoreCase(accountNo) || "null".equalsIgnoreCase(accountNo)) {
                accountNo = this.accountNumber;

            }
            if (null == template || template.isEmpty() ||
                    "String".equalsIgnoreCase(template) || "null".equalsIgnoreCase(template)) {
                template = this.template;

            }
            if (null == addInfo || addInfo.isEmpty() ||
                    "String".equalsIgnoreCase(addInfo) || "null".equalsIgnoreCase(addInfo)) {
                addInfo = this.addInfo;

            }
            if (null == accountName || accountName.isEmpty() ||
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
