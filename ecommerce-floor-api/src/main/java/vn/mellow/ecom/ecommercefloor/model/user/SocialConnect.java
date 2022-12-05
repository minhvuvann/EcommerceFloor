package vn.mellow.ecom.ecommercefloor.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.ServiceStatus;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;

import java.util.Date;

@Data
public class SocialConnect extends BaseModel {
    private String userId;
    private ServiceType serviceType;
    private ServiceStatus serviceStatus;
    private String note;
    private String email;
    private String name;
    private String imageUrl;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date cancelledAt;

}
