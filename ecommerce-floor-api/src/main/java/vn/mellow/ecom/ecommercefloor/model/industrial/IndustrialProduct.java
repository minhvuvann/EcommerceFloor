package vn.mellow.ecom.ecommercefloor.model.industrial;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
//ngành hàng
@Data
public class IndustrialProduct extends BaseModel {
    private String name;
    private String iconUrl;
    private String description;
}
