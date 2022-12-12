package vn.mellow.ecom.ecommercefloor.model.shipment.convert;

import lombok.Data;

@Data
public class ResultFeeShipping {
    private long total;
    private long service_fee;
    private long insurance_fee;
    private long pick_station_fee;
    private long coupon_value;
    private long r2s_fee;
}
