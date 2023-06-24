package vn.mellow.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.manager.CartManager;
import vn.mellow.ecom.manager.ProductManager;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.cart.Cart;
import vn.mellow.ecom.model.cart.CartDetail;
import vn.mellow.ecom.model.cart.CartItem;
import vn.mellow.ecom.model.product.Product;
import vn.mellow.ecom.model.product.ProductVariant;

import java.util.List;

@Component
public class CartDetailController {
    @Autowired
    private CartManager cartManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private UserManager userManager;


    public CartDetail getCartDetail(String userId) throws ServiceException {
        CartDetail cartDetail = new CartDetail();
        Cart cart = cartManager.getCart(userId);
        if (cart == null) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin giỏ hàng :" + userId, "Cart not found");
        }
        List<CartItem> cartItems = cartManager.getCartItems(cart.getId());
        if (null != cartItems || cartItems.size() != 0) {
            for (CartItem cartItem : cartItems) {
                ProductVariant variant = cartItem.getProductVariant();
                Product product = productManager.getProduct(variant.getProductId());
                Integer shopId = product.getShopId();
                if (null == shopId) {
                    continue;
                }
                cartItem.setShopId(shopId);

            }
        }
        cartDetail.setCartItems(cartItems);
//        List<ActivityLog> activityLogs = cartManager.getActivityLogs(cart.getId());
//        cartDetail.setActivityLogs(activityLogs);
        cartDetail.setCart(cart);
        return cartDetail;

    }

    public CartDetail deleteCartItem(String cartItemId, long quantity) throws ServiceException {
        CartItem cartItem = cartManager.getCartItem(cartItemId);
        if (null == cartItem) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin cart item :" + cartItemId, "Not found cart item :" + cartItemId);
        }
        Cart cart = cartManager.getCartById(cartItem.getCartId());
        if (quantity == 0) {
            cartManager.deleteCartItem(cartItemId);
            quantity = cartItem.getQuantity();
        } else {
            cartManager.updateQuantityCartItem(cartItem.getId(), cartItem.getQuantity() - quantity
                    , cartItem.getTotalPrice() - (quantity *
                            cartItem.getProductVariant().getSalePrice().getAmount()));
        }
        //tổng tiền giảm
        double totalDiscount = cart.getTotalDiscount() - (quantity * (
                cartItem.getProductVariant().getPrice().getAmount() -
                        cartItem.getProductVariant().getSalePrice().getAmount()));
        //tổng tiền ban đầu chưa giảm
        double totalCurrentPrice = cart.getTotalCurrentPrice() - (quantity *
                cartItem.getProductVariant().getPrice().getAmount());
        cartManager.updateCartQuantity(cartItem.getCartId(),
                cart.getTotalQuantity() - quantity,
                totalDiscount,
                totalCurrentPrice,
                cart.getTotalPrice() - (quantity *
                        cartItem.getProductVariant().getSalePrice().getAmount()));


        return getCartDetail(cart.getUserId());

    }

}
