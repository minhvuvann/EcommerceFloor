package vn.mellow.ecom.model.product;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;

@Data
public class Trademark extends BaseModel {
    private String industrialId;
    private String name;
    private String description;
    private String iconUrl;
}
