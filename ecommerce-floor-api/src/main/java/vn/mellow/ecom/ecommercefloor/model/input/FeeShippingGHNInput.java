package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;

@Data
public class FeeShippingGHNInput {
    private int from_district_id;
    private int service_id;
    private int service_type_id;
    private int to_district_id;
    private int to_ward_code;
    private int height;
    private int width;
    private int length;
    private int weight;
    private int insurance_value;
    private int cod_value;
    private int coupon;

}
