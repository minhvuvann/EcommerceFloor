package vn.mellow.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.model.enums.OrderStatus;
import vn.mellow.ecom.model.enums.OrderType;
import vn.mellow.ecom.manager.CartManager;
import vn.mellow.ecom.manager.OrderManager;
import vn.mellow.ecom.manager.UserManager;
import vn.mellow.ecom.model.cart.CartItem;
import vn.mellow.ecom.model.input.CreateOrderDTO;
import vn.mellow.ecom.model.order.Order;
import vn.mellow.ecom.model.order.OrderItem;
import vn.mellow.ecom.model.shop.Shop;
import vn.mellow.ecom.model.user.User;
import vn.mellow.ecom.model.user.UserProfile;
import vn.mellow.ecom.utils.MoneyCalculateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class OrderCreateController {
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private UserProfileController userController;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CartManager cartManager;

    private void validateOrderInputCreate(CreateOrderDTO item) throws ServiceException {
        if (null == item) {
            throw new ServiceException("invalid_data", "Thông tin không hợp lệ", "Order Input Create is required");
        }
        if (null == item.getUserId()) {
            throw new ServiceException("invalid_data", "Vui lòng nhập mã user", "userId is required");
        }
        if (null == item.getCartItemInputs()) {
            throw new ServiceException("invalid_data", "Vui lòng truyền danh sách item giỏ hàng ", "cartItems is required");
        }

    }


    // create order
    public List<Order> createOrder(CreateOrderDTO orderCreateInput) throws ServiceException {
        //validate data
        validateOrderInputCreate(orderCreateInput);
        List<Order> orders = new ArrayList<>();
        UserProfile userProfile = userController.getUserProfile(orderCreateInput.getUserId());
        User user = userProfile.getUser();
        HashMap<Integer, List<CartItem>> mapShop = new HashMap<>();
        for (CartItem cartItem : orderCreateInput.getCartItemInputs()) {
            List<CartItem> sampleCartItems = mapShop.computeIfAbsent(cartItem.getShopId(), k -> new ArrayList<>());
            sampleCartItems.add(cartItem);
        }
        for (Integer shopId : mapShop.keySet()) {
            Shop shop = userManager.getInfoShop(shopId);
            List<OrderItem> orderItems = new ArrayList<>();
            double totalPrice = 0;
            double totalDiscount = 0;
            for (CartItem cartItem : mapShop.get(shopId)) {
                OrderItem orderItem = new OrderItem();
                orderItem.setVariant(cartItem.getProductVariant());
                orderItem.setShopId(cartItem.getShopId());
                orderItem.setShopName(shop.getName());
                orderItem.setOrderStatus(OrderStatus.READY);
                //Tổng số tiền giảm giá
                orderItem.setDiscountedTotalPrice(MoneyCalculateUtils.getMoney(cartItem.getProductVariant().getPrice().getAmount() -
                        cartItem.getProductVariant().getSalePrice().getAmount()));
                totalPrice += cartItem.getTotalPrice();
                totalDiscount += orderItem.getDiscountedTotalPrice().getAmount();
                orderItem.setQuantity(cartItem.getQuantity());
                //Tổng số tiền chưa giảm giá(giá gốc)
                orderItem.setOriginalTotalPrice(MoneyCalculateUtils.getMoney(
                        cartItem.getProductVariant().getPrice().getAmount()
                ));
                orderItems.add(orderItem);
                //remove cart item
                cartManager.deleteCartItem(cartItem.getId());

            }
            Order order = new Order();
            order.setUserId(user.getId());
            order.setType(OrderType.PURCHASE);
            order.setShopId(shopId);
            order.setShopName(shop.getName());
            order.setStatus(OrderStatus.READY);
            totalPrice = totalPrice + orderCreateInput.getShipping();
            order.setTotalPrice(MoneyCalculateUtils.getMoney(totalPrice));
            order.setDiscountTotalPrice(MoneyCalculateUtils.getMoney(totalDiscount));
            order.setShippingAddress(user.getAddress());
            order.setPhoneCustomer(user.getTelephone());
            order.setEmailCustomer(user.getEmail());
            order.setNameCustomer(user.getFullName() != null ? user.getFullName() : user.getUsername());
            order.setPayment(orderCreateInput.isPayment());
            //create đơn mua
            order = orderManager.createOrder(order, orderItems);
            orders.add(order);
            //create đơn bán
            order.setType(OrderType.SELL);
            order.setFromOrderId(order.getId());
            order = orderManager.createOrder(order, orderItems);
            orders.add(order);
        }
        return orders;
    }
}
