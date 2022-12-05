package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.RoleStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;

@Data
public class RoleInput {
    private String note;
    private String description;
    private RoleType roleType;
    private RoleStatus roleStatus;
}
