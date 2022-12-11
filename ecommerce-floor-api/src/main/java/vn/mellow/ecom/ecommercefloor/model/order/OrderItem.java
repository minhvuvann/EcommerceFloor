package vn.mellow.ecom.ecommercefloor.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.NonNull;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.base.model.MoneyV2;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;
import vn.mellow.ecom.ecommercefloor.enums.OrderStatus;

import java.util.Date;

@Data
public class OrderItem extends BaseModel {
    private String shopId;
    private MoneyV2 discountedTotalPrice;
    private MoneyV2 originalTotalPrice;
    private Long quantity;
    private ProductVariant variant;
    @NonNull
    private String orderId;
    @NonNull
    private String orderItemId;
    private String note;
    @JsonFormat(
            shape = JsonFormat.Shape.NUMBER
    )
    private Date completedAt;
    private String orderItemName;
    private OrderStatus orderStatus;
}
