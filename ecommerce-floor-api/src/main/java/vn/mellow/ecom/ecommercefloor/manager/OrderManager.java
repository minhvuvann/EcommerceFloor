package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityLog;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.enums.ActivityLogType;
import vn.mellow.ecom.ecommercefloor.enums.OrderCancelReason;
import vn.mellow.ecom.ecommercefloor.enums.OrderStatus;
import vn.mellow.ecom.ecommercefloor.enums.OrderType;
import vn.mellow.ecom.ecommercefloor.model.input.UpdateStatusInput;
import vn.mellow.ecom.ecommercefloor.model.order.Order;
import vn.mellow.ecom.ecommercefloor.model.order.OrderDetail;
import vn.mellow.ecom.ecommercefloor.model.order.OrderFilter;
import vn.mellow.ecom.ecommercefloor.model.order.OrderItem;
import vn.mellow.ecom.ecommercefloor.model.user.Score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static com.mongodb.client.model.ReturnDocument.AFTER;

@Repository
public class OrderManager extends BaseManager {
    @Autowired
    private ScoreManager scoreManager;

    public OrderManager(MongoClient mongoClient) {
        super(mongoClient);
    }

    private MongoCollection<Order> orderCollection;

    public MongoCollection<Order> getOrderCollection() {
        if (null == orderCollection) {
            orderCollection = getCollection(Order.class);
        }
        return orderCollection;
    }

    private MongoCollection<OrderItem> orderItemCollection;

    public MongoCollection<OrderItem> getOrderItemCollection() {
        if (null == orderItemCollection) {
            orderItemCollection = getCollection(OrderItem.class);
        }
        return orderItemCollection;
    }

    public Order createOrder(Order order, List<OrderItem> orderItems) {
        order.setCreatedAt(new Date());
        order.setId(generateId());
        getOrderCollection().insertOne(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setId(generateId());
            orderItem.setCreatedAt(orderItem.getCreatedAt());
            orderItem.setOrderId(order.getId());
            getOrderItemCollection().insertOne(orderItem);

        }
        return order;

    }

    public Order getOrder(String orderId) {
        Order item = getOrderCollection().find(Filters.eq("_id", orderId)).first();
        if (null != item) {
            if (null == item.getType()) {
                item.setType(OrderType.SELL);
            }
        }
        return null;
    }

    public OrderDetail getOrderDetail(String orderId) {
        OrderDetail orderDetail = new OrderDetail();
        Order order = getOrder(orderId);
        List<OrderItem> orderLineItems = getItems(orderId);
        List<ActivityLog> logList = getActivityLog(orderId);
        orderDetail.setOrder(order);
        orderDetail.setOrderItems(orderLineItems);
        orderDetail.setActivityLogs(logList);

        return orderDetail;
    }


    public List<OrderItem> getItems(String orderId) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("orderId", orderId));
        return getOrderItemCollection().find(Filters.and(filter)).into(new ArrayList<>());
    }

    public OrderItem getOrderItem(String orderItemId) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("_id", orderItemId));
        return getOrderItemCollection().find(Filters.and(filter)).first();
    }

    public OrderItem getOrderItemId(String orderItemId) {
        List<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq("orderItemId", orderItemId));
        return getOrderItemCollection().find(Filters.and(filter)).first();
    }


    public Order cancelOrder(String orderId, OrderCancelReason cancelReason, String note) throws ServiceException {
        Order order = getOrder(orderId);
        if (null != order) {
            UpdateStatusInput statusInput = new UpdateStatusInput();
            statusInput.setNote(note);
            statusInput.setStatus(OrderStatus.CANCELLED.toString());
            updateOrderStatus(orderId, statusInput);
            //update order reason cancel
            updateOrderCancel(orderId,cancelReason);
            List<OrderItem> orderLineItems = getItems(orderId);
            for (OrderItem orderLineItem : orderLineItems) {
                updateItemCancel(orderLineItem.getId());
            }
            return getOrder(orderId);
        }
        return null;
    }

    public void updateItemCancel(String itemId) {
        Document updateDocument = new Document();
        updateDocument.put("updatedAt", new Date());
        updateDocument.put("status", OrderStatus.CANCELLED.toString());
        Document newDocument = new Document();
        newDocument.append("$set", updateDocument);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("_id", itemId));
        getOrderItemCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }
    public Order updateOrderCancel( String orderId, OrderCancelReason orderCancelReason) {
        getLogger().log(Level.INFO, "Update order cancel by id:" + orderId);
        Document document = new Document();
        document.put("updatedAt", new Date());
        document.put("canceledAt", new Date());
        if (null != orderCancelReason) {
            document.put("cancelReason", orderCancelReason.toString());
        }
        if (null != orderId) {
            document.put("_id", orderId);
        }
        Document newDocument = new Document();
        newDocument.append("$set", document);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = getFilters();
        filters.add(Filters.eq("_id", orderId));
        return getOrderCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);
    }

    public Order updateOrderStatus(String orderId, UpdateStatusInput statusBody) throws ServiceException {
        Order order = getOrder(orderId);

        //validate status
        validateUpdateStatus(statusBody, order);
        if (null != order) {
            // update order status
            Document document = new Document();
            String dateAt = "none";
            String cancelledAt = "cancelledAt";
            String processedAt = "processedAt";
            String completedAt = "completedAt";
            dateAt = statusBody.getStatus().equals(OrderStatus.CANCELLED.toString()) ? cancelledAt : dateAt;
            dateAt = statusBody.getStatus().equals(OrderStatus.DELIVERED.toString()) ? completedAt : dateAt;
            dateAt = statusBody.getStatus().equals(OrderStatus.PROCESSING) ||
                    statusBody.getStatus().equals(OrderStatus.DELIVERY_ONLY.toString()) ? processedAt : dateAt;

            if (!dateAt.equals("none")) {
                document.put(dateAt, new Date());
            }
            document.put("updatedAt", new Date());
            document.put("status", statusBody.getStatus().toString());
            Document newDocument = new Document();
            newDocument.append("$set", document);

            List<Bson> bsonList = new ArrayList<>();
            bsonList.add(Filters.eq("_id", orderId));
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
            order = getOrderCollection().findOneAndUpdate(Filters.and(bsonList), newDocument, options);
            //update session item in mongodb
            List<Bson> filterOrderItem = new ArrayList<>();
            filterOrderItem.add(Filters.eq("orderId", orderId));
            getOrderItemCollection().updateMany(Filters.and(filterOrderItem), newDocument);
            // add activity
            String description = "Cập nhật trạng thái" +
                    ": " + statusBody.getStatus();
            if (StringUtils.hasText(statusBody.getNote())) {
                description += ". " + statusBody.getNote();
            }
            if (OrderStatus.DELIVERED.toString().equals(statusBody.getStatus())) {

            }
            // add log update status
            addActivityLog(statusBody.getByUser(), description, orderId, ActivityLogType.UPDATE_STATUS, Order.class);

            return order;
        }
        return null;
    }

    private void validateUpdateStatus(UpdateStatusInput statusBody, Order order) throws ServiceException {

        if (null == statusBody) {
            throw new ServiceException("invalid_data", "Thông tin không hợp lệ", "update Status Body is required");
        }
        if (!OrderStatus.isExist(statusBody.getStatus())) {
            throw new ServiceException("status_error", "Trang thái đơn hàng không tồn tại", "status not exist");
        }
        if (OrderStatus.DELIVERED.equals(order.getStatus())) {
            throw new ServiceException("status_updated", "Yêu cầu trạng thái công việc đã hoàn thành", "Status is completed, can't update status");
        }
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new ServiceException("status_updated", "Yêu cầu trạng thái công việc đã bị hủy", "Status is cancelled, can't update status");
        }
    }

    public ResultList<Order> filterOrder(OrderFilter filterData) {
        List<Bson> filter = getFilters(filterData);
        // add filter
        if (filterData.getOrderId() != null) {
            appendFilter(filterData.getOrderId(), "_id", filter);
        }
        if (filterData.getStatus() != null) {
            appendFilter(filterData.getStatus().toString(), "status", filter);
        }
        if (filterData.getType() != null) {
            appendFilter(filterData.getType().toString(), "type", filter);
        }
        if (filterData.getCarrierId() != null) {
            appendFilter(filterData.getCarrierId(), "carrierId", filter);
        }
        if (filterData.getShippingServiceId() != null) {
            appendFilter(filterData.getShippingServiceId(), "shippingServiceId", filter);
        }
        if (filterData.getUserId() != null) {
            appendFilter(filterData.getUserId(), "userId", filter);
        }
        if (filterData.getShopId() != null) {
            appendFilter(filterData.getShopId(), "shopId", filter);
        }
        return getResultList(getOrderCollection(), filter, filterData.getOffset(), filterData.getMaxResult());
    }
}
