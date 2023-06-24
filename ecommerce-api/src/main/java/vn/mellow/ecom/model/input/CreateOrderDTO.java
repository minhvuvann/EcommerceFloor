package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.cart.CartItem;
import vn.mellow.ecom.model.order.Order;
import vn.mellow.ecom.model.order.OrderItem;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOrderDTO {
    @NotNull
    private String userId;
    private double shipping;
    private boolean payment;
    private List<CartItem> cartItemInputs;

}
