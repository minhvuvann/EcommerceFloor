package vn.mellow.ecom.ecommercefloor.model.user;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.filter.BaseFilter;
import vn.mellow.ecom.ecommercefloor.enums.GenderType;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.enums.UserStatus;


@Data
public class UserFilter extends BaseFilter {
    private  String userId;
    private String fullName;
    private GenderType gender;
    private ServiceType serviceType;
    private UserStatus userStatus;


}
