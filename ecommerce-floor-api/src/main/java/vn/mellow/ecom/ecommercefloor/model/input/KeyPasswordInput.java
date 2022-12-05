package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.PasswordStatus;

@Data
public class KeyPasswordInput {
    private String password;
    private String note;
    private PasswordStatus passwordStatus;
}
