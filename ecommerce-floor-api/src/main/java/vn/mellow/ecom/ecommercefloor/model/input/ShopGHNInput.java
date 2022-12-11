package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;

@Data
public class ShopGHNInput {
    private int district_id;
    private String ward_code;
    private String name;
    private String phone;
    private String address;

}
