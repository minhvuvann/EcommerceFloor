package vn.mellow.ecom.ecommercefloor.model.user;

import lombok.Data;

import java.util.List;

@Data
public class UserProfile {
    private User user;
    private List<KeyPassword> keyPassword;
    private List<Role> role;
    private Score score;

}
