package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.manager.OrderManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateOrderInput;
import vn.mellow.ecom.ecommercefloor.model.order.Order;
import vn.mellow.ecom.ecommercefloor.model.order.OrderItem;
import vn.mellow.ecom.ecommercefloor.utils.MoneyCalculateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderCreateController {
    @Autowired
    private OrderManager orderManager;



    private void validateOrderInputCreate( CreateOrderInput item) throws ServiceException {
        if (null == item) {
            throw new ServiceException("invalid_data", "Thông tin không hợp lệ", "Order Input Create is required");
        }
        if (null == item.getOrder() ) {
            throw new ServiceException("invalid_data", "Chưa nhập mã đơn hàng", "partnerId is invalid");
        }
    }


    private void validateOrderItemInput(OrderItem item) throws ServiceException {
        if (null == item) {
            throw new ServiceException("invalid_data", "Thông tin không hợp lệ", "Line item is invalid");
        } else {
            if (null != item.getOrderItemId()) {
                OrderItem orderItem = orderManager.getOrderItemId( item.getOrderItemId());
                if (null != orderItem) {
                    throw new ServiceException("order_item_id_existed", "Mã order item " + item.getOrderItemId() + " đã tồn tại", "Order item ID is existed");
                }
            }
            if (null == item.getVariant()) {
                throw new ServiceException("invalid_data", "Chưa có thông tin sản phẩm", "Product variant is empty");
            }

            if (item.getQuantity() <= 0) {
                throw new ServiceException("invalid_data", "Số lượng sản phẩm không hợp lệ", "quantity must greater than 0 (" + item.getQuantity() + ")");
            }
        }
        item.setOrderItemName(item.getVariant().getTitle());
    }

    // create order
    public Order createOrder( CreateOrderInput orderCreateInput) throws ServiceException {
        //validate data
        validateOrderInputCreate(orderCreateInput);

        double total = 0;
        for (OrderItem item : orderCreateInput.getOrderItems()) {
            validateOrderItemInput(item);
            double price = MoneyCalculateUtils.getMoneyAmount(item.getVariant().getPrice());
            double itemTotal = item.getQuantity() * price;
            item.setOriginalTotalPrice(MoneyCalculateUtils.getMoney(itemTotal));
            total += itemTotal;
        }
        // tính tổng lại tiền đơn hàng
        orderCreateInput.getOrder().setTotalPrice(MoneyCalculateUtils.getMoney(total));


        // create order
        return null;

//        return orderManager.createOrder( order, orderLineItems);

    }
    }
