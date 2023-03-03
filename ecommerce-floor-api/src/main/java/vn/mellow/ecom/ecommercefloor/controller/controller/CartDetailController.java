package vn.mellow.ecom.ecommercefloor.controller.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.client.model.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityLog;
import vn.mellow.ecom.ecommercefloor.manager.CartManager;
import vn.mellow.ecom.ecommercefloor.manager.ProductManager;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.cart.Cart;
import vn.mellow.ecom.ecommercefloor.model.cart.CartDetail;
import vn.mellow.ecom.ecommercefloor.model.cart.CartItem;
import vn.mellow.ecom.ecommercefloor.model.product.Product;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;
import vn.mellow.ecom.ecommercefloor.model.shop.Shop;

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
        cartDetail.setItemToShops(cartItems);
//        List<ActivityLog> activityLogs = cartManager.getActivityLogs(cart.getId());
//        cartDetail.setActivityLogs(activityLogs);
        cartDetail.setCart(cart);
        return cartDetail;

    }

    public CartDetail deleteCartItem(String cartItemId) throws ServiceException {
        CartItem cartItem = cartManager.getCartItem(cartItemId);
        if (null == cartItem) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin cart item :" + cartItemId, "Not found cart item :" + cartItemId);
        }
        String cartId = cartItem.getCartId();
        cartManager.deleteCartItem(cartItemId);

        // tính lại thông tin tổng giá và số lượng của cart
        Cart cart = cartManager.getCartById(cartId);
        long totalQuantity = cart.getTotalQuantity() - cartItem.getQuantity();
        double totalPrice = cart.getTotalPrice() - cartItem.getTotalPrice();
        cartManager.updateCartQuantity(cartId, totalQuantity, totalPrice);

        return getCartDetail(cart.getUserId());

    }

    public CartDetail updateQuantityCartItem(String cartItemId, long quantity) throws ServiceException {
        CartItem cartItem = cartManager.getCartItem(cartItemId);
        if (null == cartItem) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin cart item :" + cartItemId, "Not found cart item :" + cartItemId);
        }
        Cart cart = cartManager.getCartById(cartItem.getCartId());


        if (quantity == 0) {
            deleteCartItem(cartItemId);
            return getCartDetail(cart.getUserId());
        }

        long quantityCurrent = cartItem.getQuantity();
        double priceCurrent = cartItem.getTotalPrice();
        double price = cartItem.getProductVariant().getPrice().getAmount() * quantity;
        cartManager.updateQuantityCartItem(cartItemId, quantity, price);
        //update total price and quantity
        long quantityReload = (cart.getTotalQuantity() - quantityCurrent) + quantity;
        double priceReload = (cart.getTotalPrice() - priceCurrent) + price;
        cartManager.updateCartQuantity(cartItem.getCartId(), quantityReload, priceReload);

        return getCartDetail(cart.getUserId());
    }
}
