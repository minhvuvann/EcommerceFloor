package vn.mellow.ecom.model.shop;

import lombok.Data;
import vn.mellow.ecom.model.enums.ActiveStatus;
import vn.mellow.ecom.model.geo.Address;

@Data
public class Shop {
    private int shopId;
    private String phone;
    private String name;
    private String imageUrl;
    private String description;
    private Address address;
    private ActiveStatus status;
}
