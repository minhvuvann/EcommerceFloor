package vn.mellow.ecom.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.model.enums.RoleStatus;
import vn.mellow.ecom.model.enums.RoleType;

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
