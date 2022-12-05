package vn.mellow.ecom.ecommercefloor.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.RoleStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;

import java.util.Date;

@Data
public class Role extends BaseModel {
    private String userId;
    private String note;
    private String description;
    private RoleType roleType;
    private RoleStatus roleStatus;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date cancelledAt;

}
