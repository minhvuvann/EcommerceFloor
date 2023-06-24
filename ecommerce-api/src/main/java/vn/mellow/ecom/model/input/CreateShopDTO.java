package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.enums.ActiveStatus;
import vn.mellow.ecom.model.geo.Address;

@Data
public class CreateShopDTO {
    private String name;
    private String imageUrl;
    private String description;
    private String wardCode;
    private int district_id;
    private String phone;
    private String address;
    private Address addressShop;
    private ActiveStatus status;

}
