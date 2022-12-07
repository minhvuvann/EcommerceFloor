package vn.mellow.ecom.ecommercefloor.model.shipment;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.base.model.MoneyV2;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;

import java.util.ArrayList;
import java.util.List;
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
