package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.cart.Cart;
import vn.mellow.ecom.model.cart.CartItem;

import java.util.List;

@Data
public class CreateCartDTO {
    private Cart cart;
    private List<CartItem> cartItemList;
}
