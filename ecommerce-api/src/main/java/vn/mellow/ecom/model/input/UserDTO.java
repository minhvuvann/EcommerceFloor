package vn.mellow.ecom.model.input;

import lombok.Data;
import lombok.NonNull;
import vn.mellow.ecom.base.logs.ActivityUser;
import vn.mellow.ecom.model.enums.GenderType;
import vn.mellow.ecom.model.enums.ServiceType;
import vn.mellow.ecom.model.enums.UserStatus;
import vn.mellow.ecom.model.geo.Address;

import java.util.Date;

@Data
public class UserDTO {
    private String username;
    private String fullName;
    private String email;
    private String telephone;
    private Date birthday;
    private Address address;
    private GenderType gender;
    private String imageUrl;
    @NonNull
    private ServiceType serviceType;
    private String description;
    private UserStatus userStatus;
    private ActivityUser byUser;

    public UserDTO() {

    }
}
