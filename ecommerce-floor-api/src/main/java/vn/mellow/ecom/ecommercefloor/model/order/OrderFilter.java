package vn.mellow.ecom.ecommercefloor.model.order;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.filter.BaseFilter;
import vn.mellow.ecom.ecommercefloor.enums.OrderStatus;
import vn.mellow.ecom.ecommercefloor.enums.OrderType;

@Data
public class OrderFilter extends BaseFilter {
    private String orderId;
    private OrderStatus status;
    private OrderType type;
    private String carrierId;
    private String userId;
    private String shopId;
    private String shippingServiceId;

}
