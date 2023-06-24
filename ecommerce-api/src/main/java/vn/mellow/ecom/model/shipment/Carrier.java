package vn.mellow.ecom.model.shipment;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.model.enums.ActiveStatus;
import vn.mellow.ecom.model.enums.CarrierType;

@Data
public class Carrier extends BaseModel {
    private String name;
    private ActiveStatus status;
    private String description;
    private CarrierType type;
    private String gatewayCode;
}
