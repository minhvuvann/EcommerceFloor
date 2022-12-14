package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.model.order.Order;
import vn.mellow.ecom.ecommercefloor.model.order.OrderItem;
import java.util.List;
@Data
public class CreateOrderInput {
    private Order order;
    private List<OrderItem> orderItems;

}
