package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.GeoType;

@Data
public class Geo extends BaseModel{
    private String name;
    private int ghn_id;
    private int parent_id;
    private String code;
    private GeoType type;
    private String description;
}
