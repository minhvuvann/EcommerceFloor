package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.enums.PasswordStatus;

@Data
public class KeyPasswordDTO {
    private String password;
    private String note;
    private PasswordStatus passwordStatus;
}
