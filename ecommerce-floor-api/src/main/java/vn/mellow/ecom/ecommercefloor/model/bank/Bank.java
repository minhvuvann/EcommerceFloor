package vn.mellow.ecom.ecommercefloor.model.bank;

import lombok.Data;

@Data
public class Bank {
    private int id;
    private String name;
    private String code;
    private String bin;
    private String shortName;
    private String logo;
    private int transferSupported;
    private int lookupSupported;
    private String short_name;
    private int support;
    private int isTransfer;
    private String swift_code;

}
