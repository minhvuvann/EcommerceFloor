package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.enums.OrderCancelReason;

@Data
public class CancelOrderDTO {
    private String note;
    private OrderCancelReason cancelReason;
}
