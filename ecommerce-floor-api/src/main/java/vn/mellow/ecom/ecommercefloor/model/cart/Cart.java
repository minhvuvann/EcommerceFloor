package vn.mellow.ecom.ecommercefloor.model.cart;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;

@Data
public class Cart extends BaseModel {
    private String userId;
    private long totalQuantity;
    private double totalCurrentPrice;
    private double totalDiscount;
    private double totalPrice;

}
