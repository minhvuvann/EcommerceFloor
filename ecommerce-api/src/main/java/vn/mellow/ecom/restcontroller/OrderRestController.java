package vn.mellow.ecom.restcontroller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.base.controller.BaseController;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.base.filter.ResultList;
import vn.mellow.ecom.base.logs.ActivityUser;
import vn.mellow.ecom.controller.OrderCreateController;
import vn.mellow.ecom.controller.OrderDetailController;
import vn.mellow.ecom.manager.OrderManager;
import vn.mellow.ecom.model.input.CancelOrderDTO;
import vn.mellow.ecom.model.input.CreateOrderDTO;
import vn.mellow.ecom.model.input.UpdateStatusDTO;
import vn.mellow.ecom.model.order.Order;
import vn.mellow.ecom.model.order.OrderDetail;
import vn.mellow.ecom.model.order.OrderFilter;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/order/1.0.0/")
public class OrderRestController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);

    @Autowired
    private OrderCreateController orderCreateController;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderDetailController orderDetailController;

    @GetMapping()
    public ResponseEntity checkAPI() {
        return ResponseEntity.ok().body("Order Service");
    }


    @ApiOperation(value = "get order by orderId")
    @GetMapping("/order/{orderId}")
    public Order getOrder(@PathVariable String orderId) throws ServiceException {
        Order data = orderManager.getOrder(orderId);
        if (null == data) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin order ", "Not found order by order id: " + orderId);
        }
        return data;
    }

    @ApiOperation(value = "get order detail by orderId")
    @GetMapping("/order-detail/{orderId}")
    public OrderDetail getOrderDetail(@PathVariable String orderId) throws ServiceException {
        return orderDetailController.getOrderDetail(orderId);
    }


    @ApiOperation(value = "create new  order")
    @PostMapping("/order")
    public List<Order> createOrder(@RequestBody CreateOrderDTO createInput) throws ServiceException {
        return orderCreateController.createOrder(createInput);
    }

    @ApiOperation(value = "cancel order by orderId")
    @PutMapping("/order/{orderId}/cancel")
    public Order cancelOrder(@PathVariable String orderId, @RequestBody CancelOrderDTO cancelInput) throws ServiceException {
        return orderDetailController.cancelOrder(orderId, cancelInput);
    }

    @ApiOperation(value = "confirm status order by orderId")
    @PutMapping("/order/{orderId}/confirm-sequence")
    public Order confirmSequenceStatus(@PathVariable String orderId, @RequestBody ActivityUser confirmInput) throws ServiceException {
        return orderDetailController.confirmSequenceStatus(orderId, confirmInput);
    }
    @ApiOperation(value = "update order status by orderId")
    @PutMapping("/order/{orderId}/status")
    public Order updateOrderStatus(@PathVariable String orderId, @RequestBody UpdateStatusDTO statusBody) throws ServiceException {
        return orderManager.updateOrderStatus(orderId, statusBody);
    }

    @ApiOperation(value = "find order request")
    @PostMapping("/order/filter")
    public ResultList<Order> filterOrder(
            @RequestBody OrderFilter filterData) {
        return orderManager.filterOrder(filterData);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllServiceException(ServiceException e) {
        LOGGER.error("ServiceException error.", e);
        return error(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return error("internal_server_error", "Có lỗi trong quá trình xử lý", e.getMessage());
    }
}
