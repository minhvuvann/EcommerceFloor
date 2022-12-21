package vn.mellow.ecom.ecommercefloor.model.cart;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;

@Data
public class CartItem extends BaseModel {
    private String cartId;
    private Integer shopId;
    private ProductVariant productVariant;
    private long quantity;
    private double totalPrice;
}
