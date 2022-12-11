package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.ColorProduct;

@Data
public class ProductVariant extends BaseModel{
    private String  imageUrl;
    private MoneyV2 price;
    private String productId;
    private String productName;
    private Integer quantityAvailable;
    private boolean requiresShipping;
    private String sku;
    private String title;
    private ColorProduct color;
    private Double weight;
    private WeightUnit weightUnit;
    private DimensionMeasurement dimension;
}
