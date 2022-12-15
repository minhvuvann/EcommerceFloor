package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.model.cart.Cart;
import vn.mellow.ecom.ecommercefloor.model.cart.CartItem;
import java.util.List;
@Data
public class CreateCartInput {
    private Cart cart;
    private List<CartItem> cartItemList;
}
