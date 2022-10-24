package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import lombok.NonNull;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.enums.GenderType;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.enums.UserStatus;

import java.util.Date;

@Data
public class UserInput {
    private String username;
    private String fullName;
    private String email;
    private String telephone;
    private Date birthday;
    private GenderType gender;
    private String imageUrl;
    @NonNull
    private ServiceType serviceType;
    private String description;
    private UserStatus userStatus;
    private ActivityUser byUser;

    public UserInput() {

    }
}
