package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.OrderCancelReason;

@Data
public class CancelOrderInput {
    private String note;
    private OrderCancelReason cancelReason;
}
