package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.enums.RoleStatus;
import vn.mellow.ecom.model.enums.RoleType;

@Data
public class RoleDTO {
    private String note;
    private String description;
    private RoleType roleType;
    private RoleStatus roleStatus;
}
