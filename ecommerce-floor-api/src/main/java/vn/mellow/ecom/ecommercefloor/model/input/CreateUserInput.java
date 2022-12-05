package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.model.user.KeyPassword;
import vn.mellow.ecom.ecommercefloor.model.user.User;

@Data
public class CreateUserInput {
    private UserInput user;
    private KeyPasswordInput password;
    private RoleInput role;
}
