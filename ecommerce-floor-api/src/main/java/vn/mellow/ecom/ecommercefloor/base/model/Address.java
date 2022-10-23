package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;

@Data
public class Address {
    private String address1;
    private String address2;
    private String country;
    private String countryCode;
    private String province;
    private String provinceCode;
    private String district;
    private String districtCode;
    private String ward;
    private String wardCode;
}

