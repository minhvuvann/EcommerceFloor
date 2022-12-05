package vn.mellow.ecom.ecommercefloor.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.PasswordStatus;

import java.util.Date;

@Data
public class KeyPassword extends BaseModel {
    private String userId;
    private String password;
    private String note;
    private String token;
    private PasswordStatus passwordStatus;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date cancelledAt;

}
