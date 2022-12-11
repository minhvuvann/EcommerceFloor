package vn.mellow.ecom.ecommercefloor.model.shop;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;

@Data
public class Shop {
    private int shopId;
    private String name;
    private String imageUrl;
    private String description;
    private Address address;
    private ActiveStatus status;
}
