package vn.mellow.ecom.ecommercefloor.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.NonNull;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.base.model.Address;
import vn.mellow.ecom.ecommercefloor.base.model.MoneyV2;
import vn.mellow.ecom.ecommercefloor.enums.CurrencyCode;
import vn.mellow.ecom.ecommercefloor.enums.OrderCancelReason;
import vn.mellow.ecom.ecommercefloor.enums.OrderStatus;
import vn.mellow.ecom.ecommercefloor.enums.OrderType;

import java.util.Date;

@Data
public class Order {
    private OrderCancelReason cancelReason;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date canceledAt;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date approvedAt;
    private MoneyV2 discountTotalPrice;
    private CurrencyCode currencyCode;
    private int orderNumber;
    private String telephoneCustomer;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date processedAt;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date completedAt;
    private MoneyV2 totalShippingPrice;
    private MoneyV2 totalPrice;
    private MoneyV2 totalTax;
    private MoneyV2 cod;
    @NonNull
    private String nameCustomer;
    private String emailCustomer;
    private Address shippingAddress;
    @NonNull
    private String orderId;
    @NonNull
    private OrderType orderType;
    private OrderStatus orderStatus;
    private String note;
    private ActivityUser createBy;
    private String shippingServiceId;
    private String shopId;
    private String carrierId;

    public Order() {
    }
}
