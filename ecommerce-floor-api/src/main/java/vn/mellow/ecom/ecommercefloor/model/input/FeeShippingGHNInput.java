package vn.mellow.ecom.ecommercefloor.model.input;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class FeeShippingGHNInput {
    private Integer from_district_id;
    private Integer service_id;
    private Integer service_type_id;
    private Integer to_district_id;
    private Integer to_ward_code;
    private Integer height;
    private Integer width;
    private Integer length;
    private Integer weight;
    private Integer insurance_value;
    private Integer cod_value;
    private Integer coupon;

}
