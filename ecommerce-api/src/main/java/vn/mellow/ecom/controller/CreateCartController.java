package vn.mellow.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.manager.CartManager;
import vn.mellow.ecom.manager.ProductManager;
import vn.mellow.ecom.model.cart.Cart;
import vn.mellow.ecom.model.cart.CartItem;
import vn.mellow.ecom.model.product.Product;
import vn.mellow.ecom.model.product.ProductVariant;

import java.util.List;

@Component
public class CreateCartController {

    @Autowired
    private ProductManager productManager;
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
        if (null == cartItem.getProductVariant().getPrice()) {
            throw new ServiceException("not_found", "Sản phẩm chưa có giá", "product variant.price is null");
        }
        double total = cartItem.getQuantity() * cartItem.getProductVariant().getSalePrice().getAmount();
        cartItem.setTotalPrice(total);
    }

    public CartItem createCartItem(CartItem cartItem) throws ServiceException {
        //validate
        validateCartItemInput(cartItem);
        if (null == cartItem.getCartId()) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin mã giỏ hàng.", "cart id is not available");

        }
        ProductVariant variant = cartItem.getProductVariant();
        Product product = productManager.getProduct(variant.getProductId());
        Integer shopId = product.getShopId();
        cartItem.setShopId(shopId);
        Cart cart = cartManager.getCartById(cartItem.getCartId());
        CartItem cartItemExist = cartManager.getCartItem(cartItem.getCartId(), cartItem.getProductVariant().getId());
        //tổng tiền giảm
        double totalDiscount = cart.getTotalDiscount() + (cartItem.getQuantity() * (
                cartItem.getProductVariant().getPrice().getAmount() -
                        cartItem.getProductVariant().getSalePrice().getAmount()));
        //tổng tiền ban đầu chưa giảm
        double totalCurrentPrice = cart.getTotalCurrentPrice() + (cartItem.getQuantity() * (
                cartItem.getProductVariant().getPrice().getAmount()));
        if (null != cartItemExist) {
            cartManager.updateCartQuantity(cartItem.getCartId(),
                    //số lượng
                    cart.getTotalQuantity() + cartItem.getQuantity(),
                    totalDiscount
                    , totalCurrentPrice,
                    //tổng tiền
                    cart.getTotalPrice() + cartItem.getTotalPrice());
            return cartManager.updateQuantityCartItem(cartItemExist.getId(),
                    cartItem.getQuantity() + cartItemExist.getQuantity()
                    , cartItem.getTotalPrice() + cartItemExist.getTotalPrice());
        }

        cartManager.updateCartQuantity(cartItem.getCartId(),
                //Số lượng
                cart.getTotalQuantity() + cartItem.getQuantity(),
                totalDiscount,
                totalCurrentPrice,
                //Tổng tiền
                cart.getTotalPrice() + cartItem.getTotalPrice());

        return cartManager.createCartItem(cartItem);
    }
}
