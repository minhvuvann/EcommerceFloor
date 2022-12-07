package vn.mellow.ecom.ecommercefloor.model.user;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;

@Data
public class Shop {
    private String shopId;
    private String name;
    private String imageUrl;
    private String description;
    private ActiveStatus status;
}
