package vn.mellow.ecom.ecommercefloor.model.shipment;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;

@Data
public class Carrier extends BaseModel {
    private String name;
    private ActiveStatus status;
    private String description;
    private String gatewayCode;
}
