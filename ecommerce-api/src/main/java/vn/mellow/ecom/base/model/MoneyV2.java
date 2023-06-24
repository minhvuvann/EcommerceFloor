package vn.mellow.ecom.base.model;


import lombok.Data;
import vn.mellow.ecom.model.enums.CurrencyCode;

@Data
public class MoneyV2 {
    private double amount;
    private CurrencyCode currencyCode;

    public MoneyV2() {
    }


}
