package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.manager.CartManager;
import vn.mellow.ecom.ecommercefloor.model.cart.Cart;
import vn.mellow.ecom.ecommercefloor.model.cart.CartDetail;
import vn.mellow.ecom.ecommercefloor.model.cart.CartItem;

import java.util.List;

@Component
public class CreateCartController {
    @Autowired
    private CartManager cartManager;

    public Cart createCart(Cart cart, List<CartItem> cartItems) throws ServiceException {
        //validate
        validateCartInput(cart, cartItems);

        return cartManager.createCart(cart, cartItems);
    }

    private void validateCartInput(Cart cart, List<CartItem> cartItems) throws ServiceException {
        if (null == cart) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin giỏ hàng", "Cart is not available");
        }
        if (null == cart.getUserId()) {
            throw new ServiceException("not_found", "Vui lòng nhập mã khách hàng của giỏ hàng này.", "User id is not available");
        }
        if (null != cartItems && cartItems.size() != 0) {
            for (CartItem cartItem : cartItems) {
                //validate
                validateCartItemInput(cartItem);
            }

        }
    }

    private void validateCartItemInput(CartItem cartItem) throws ServiceException {
        if (null == cartItem.getProductVariant()) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin của sản phẩm.", "Product variant is not available");
        }
        if (0 == cartItem.getQuantity()) {
            throw new ServiceException("not_found", "Số lượng của sản phầm :" + cartItem.getProductVariant().getProductName() + " nhỏ hơn hoặc bằng không.", "Quantity is not available");
        }
        double total = cartItem.getQuantity() + cartItem.getProductVariant().getPrice().getAmount();
        cartItem.setTotalPrice(total);
    }

    public CartItem createCartItem(CartItem cartItem) throws ServiceException {
        //validate
        validateCartItemInput(cartItem);
        if (null == cartItem.getCartId()) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin mã giỏ hàng.", "cart id is not available");

        }
        return cartManager.createCartItem(cartItem);
    }
}
