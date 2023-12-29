package vn.mellow.ecom.model.input;

import lombok.Data;

@Data
public class FeeShippingGHNDTO {
    private Integer from_district_id;
    private Integer service_id;
    private Integer service_type_id;
    private Integer to_district_id;
    private String to_ward_code;
    //cao<=200cm
    private Integer height;
    //rộng<=200cm
    private Integer width;
    //dài<=200cm
    private Integer length;
    private Integer weight;
    //tiền thu hộ
    private Integer insurance_value;
    private Integer cod_value;
    private Integer coupon;

}
