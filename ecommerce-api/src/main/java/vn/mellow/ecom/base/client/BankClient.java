package vn.mellow.ecom.base.client;

import vn.mellow.ecom.base.exception.ClientException;
import vn.mellow.ecom.model.bank.*;
import vn.mellow.ecom.utils.DateUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public SyncBank syncBankNow(String bankID, String token) throws ClientException {
        SyncBankDTO info = new SyncBankDTO();
        info.setBank_acc_id(bankID);
        return postCasso("/v2/sync", SyncBank.class, token,info);
    }

    public ResultHistory getHistoryBank(String token) throws ClientException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDay = dateFormat.format(DateUtils.augmentDay(new Date(), -1));
        System.out.println(startDay);
        String endDay = dateFormat.format(new Date());
        return getCasso("/v2/transactions?fromDate=" + startDay + "&toDate=" + endDay + "&page=1&pageSize=1&sort=DESC", ResultHistory.class, token);
    }
}
