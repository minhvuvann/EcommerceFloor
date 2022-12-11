package vn.mellow.ecom.ecommercefloor.model.geo;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.CountryCode;

@Data
public class Address {
    private String address1;
    private String address2;
    private String province;
    private int provinceCode;
    private String district;
    private int districtCode;
    private String ward;
    private int wardCode;
    private String city;
    private String company;
    private String country;
    private CountryCode countryCode;
    private Double latitude;
    private Double longitude;
    private String zip;
}

