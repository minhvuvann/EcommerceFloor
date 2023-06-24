package vn.mellow.ecom.model.input;

import lombok.Data;

@Data
public class CreateUserDTO {
    private UserDTO user;
    private KeyPasswordDTO password;
    private RoleDTO role;
}
