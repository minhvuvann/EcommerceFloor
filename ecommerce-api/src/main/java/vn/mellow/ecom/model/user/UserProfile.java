package vn.mellow.ecom.model.user;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
    private User user;
    private List<KeyPassword> keyPasswords;
    private List<Role> roles;
    private Score score;

}
