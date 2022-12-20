package vn.mellow.ecom.ecommercefloor.controller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.manager.OrderManager;
import vn.mellow.ecom.ecommercefloor.model.input.CancelOrderInput;
import vn.mellow.ecom.ecommercefloor.model.order.Order;
import vn.mellow.ecom.ecommercefloor.model.order.OrderDetail;

@Component
public class OrderDetailController extends BaseController {
    @Autowired
    private OrderManager orderManager;

    // get order detail
    public Order getOrder(String orderId) throws ServiceException {
        Order data = orderManager.getOrder(orderId);
        if (null == data) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin đơn hàng", "Not found order by order id: " + orderId);
        }
        return data;
    }

    // get order detail
    public OrderDetail getOrderDetail(String orderId) throws ServiceException {
        OrderDetail data = orderManager.getOrderDetail(orderId);
        if (null == data) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin chi tiết đơn hàng", "Not found order by order id: " + orderId);
        }
        return data;
    }

    public Order cancelOrder(String orderId, CancelOrderInput cancelInput) throws
            ServiceException {
        //check order exists
        Order order = getOrder(orderId);
        if (null == order) {
            throw new ServiceException("cancel_failure", "Xác nhận hủy đơn hàng " + orderId + " không thành công. Đơn hàng không tồn tại.", "cancelled order failure");

        }
        orderManager.cancelOrder(orderId, cancelInput.getCancelReason(), cancelInput.getNote());

        order = getOrder(orderId);

        return order;
    }
}
