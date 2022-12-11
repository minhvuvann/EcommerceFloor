package vn.mellow.ecom.ecommercefloor.model.order;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityLog;

import java.util.List;
@Data
public class OrderDetail {
    private Order order;
    private List<OrderItem> orderItems;
    private List<ActivityLog> activityLogs;
}
