package vn.mellow.ecom.ecommercefloor.model.product;

import lombok.Data;

import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.base.model.MoneyV2;
import vn.mellow.ecom.ecommercefloor.enums.IndustrialType;

import java.util.List;

@Data
public class Product extends BaseModel {
    private String name;
    private Integer shopId;
    private String industrialId;
    private String industrialTypeName;
    private String description;
    private String featuredImageUrl;
    private List<String> imageUrls;
    private MoneyV2 mediumPrice;
    private String title;

}
