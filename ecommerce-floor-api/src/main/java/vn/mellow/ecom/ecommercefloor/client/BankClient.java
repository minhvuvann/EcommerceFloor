package vn.mellow.ecom.ecommercefloor.client;

import vn.mellow.ecom.ecommercefloor.base.client.BaseClient;
import vn.mellow.ecom.ecommercefloor.base.exception.ClientException;
import vn.mellow.ecom.ecommercefloor.model.bank.Bank;
import vn.mellow.ecom.ecommercefloor.model.bank.ResultBank;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class BankClient extends BaseClient {

    public BankClient(String service) {
        super(service);
    }

    public ResultBank<Bank> getBankList() throws ClientException {
        return getResponseBankList("/v2/banks", Bank.class);
    }

    public URL getQRImage(String bankID, String accountNo, String template
            , double amount, String addInfo, String accountName) throws ClientException, MalformedURLException {

        return new URL(service + "/image/" + bankID + "-" + accountNo + "-" + template + ".png?amount=" + amount + "&addInfo=" + addInfo + "&accountName=" + accountName);
    }
}
