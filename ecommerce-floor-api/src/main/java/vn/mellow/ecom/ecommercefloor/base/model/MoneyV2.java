package vn.mellow.ecom.ecommercefloor.base.model;


import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.CurrencyCode;

@Data
public class MoneyV2 {
    private double amount;
    private CurrencyCode currencyCode;

    public MoneyV2() {
    }


}
