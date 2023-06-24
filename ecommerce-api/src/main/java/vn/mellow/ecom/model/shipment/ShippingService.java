package vn.mellow.ecom.model.shipment;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.base.model.MoneyV2;
import vn.mellow.ecom.model.enums.ActiveStatus;

@Data
public class ShippingService extends BaseModel {
    private String name;
    private String description;
    private ActiveStatus status;
    private String carrierId;
    private MoneyV2 shippingRate;
    private String serviceId;
    private String serviceCodeId;

}
