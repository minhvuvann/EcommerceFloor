package vn.mellow.ecom.model.user;

import lombok.Data;
import vn.mellow.ecom.base.filter.BaseFilter;
import vn.mellow.ecom.model.enums.GenderType;
import vn.mellow.ecom.model.enums.RoleType;
import vn.mellow.ecom.model.enums.ServiceType;
import vn.mellow.ecom.model.enums.UserStatus;


@Data
public class UserFilter extends BaseFilter {
    private String userId;
    private String fullName;
    private String email;
    private String telephone;
    private RoleType roleType;
    private GenderType gender;
    private ServiceType serviceType;
    private UserStatus userStatus;


}
