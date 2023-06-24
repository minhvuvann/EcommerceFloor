package vn.mellow.ecom.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.NonNull;
import vn.mellow.ecom.base.logs.ActivityUser;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.base.model.MoneyV2;
import vn.mellow.ecom.model.enums.OrderCancelReason;
import vn.mellow.ecom.model.enums.OrderStatus;
import vn.mellow.ecom.model.enums.OrderType;
import vn.mellow.ecom.model.geo.Address;

import java.util.Date;

@Data
public class Order extends BaseModel {
    private OrderCancelReason cancelReason;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date canceledAt;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date processedAt;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date completedAt;
    private MoneyV2 discountTotalPrice;
    private MoneyV2 totalShippingPrice;
    private MoneyV2 totalPrice;
    private MoneyV2 totalTax;
    private MoneyV2 cod;
    @NonNull
    private int shopId;
    private String shopName;
    private String fromOrderId;
    @NonNull
    private String userId;
    private String nameCustomer;
    private String emailCustomer;
    private String phoneCustomer;
    private Address shippingAddress;
    @NonNull
    private OrderType type;
    private OrderStatus status;
    private String note;
    private ActivityUser createBy;
    private String shippingServiceId;
    private String carrierId;
    private boolean payment;

    public Order() {
    }
}
