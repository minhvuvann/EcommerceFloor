package vn.mellow.ecom.ecommercefloor.model.product;

import lombok.Data;
import org.springframework.lang.NonNull;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.IndustrialType;

import java.awt.*;
import java.util.List;

@Data
public class Product extends BaseModel {
    private String name;
    private String shopId;
    private IndustrialType industrialType;
    private String industrialTypeName;
    private String description;
    private String featuredImageUrl;
    private List<String> imageUrls;
    private String title;

}
