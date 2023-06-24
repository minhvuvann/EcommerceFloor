package vn.mellow.ecom.model.cart;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;

@Data
public class Cart extends BaseModel {
    private String userId;
    private long totalQuantity;
    //giá trị đơn hàng khi chưa giảm giá
    private double totalCurrentPrice;
    //giá trị giảm giá
    private double totalDiscount;
    //tổng tiền hàng sau khi giảm giá
    private double totalPrice;

}
