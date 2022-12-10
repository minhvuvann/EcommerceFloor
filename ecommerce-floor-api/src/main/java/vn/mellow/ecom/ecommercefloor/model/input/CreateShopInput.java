package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.ActiveStatus;
@Data
public class CreateShopInput {
    private String name;
    private String imageUrl;
    private String description;
    private String wardCode;
    private int district_id;
    private String phone;
    private String address;
    private ActiveStatus status;

}
