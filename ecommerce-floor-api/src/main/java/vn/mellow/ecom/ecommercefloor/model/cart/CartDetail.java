package vn.mellow.ecom.ecommercefloor.model.cart;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityLog;
import vn.mellow.ecom.ecommercefloor.enums.ActivityLogType;
import vn.mellow.ecom.ecommercefloor.model.shop.Shop;

import java.util.HashMap;
import java.util.List;
@Data
public class CartDetail {
    private Cart cart;
    private HashMap<Integer,List<CartItem>> itemToShops ;
    private List<ActivityLog> activityLogs;

}
