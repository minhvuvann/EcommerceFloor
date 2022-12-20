package vn.mellow.ecom.ecommercefloor.model.product;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.SizeType;
import vn.mellow.ecom.ecommercefloor.model.size.DimensionMeasurement;
import vn.mellow.ecom.ecommercefloor.base.model.MoneyV2;
import vn.mellow.ecom.ecommercefloor.enums.ColorProduct;
import vn.mellow.ecom.ecommercefloor.model.input.WeightUnit;

@Data
public class ProductVariant extends BaseModel {
    private String  imageUrl;
    private MoneyV2 price;
    private String productId;
    private String productName;
    private Integer quantityAvailable;
    private boolean requiresShipping;
    private String sku;
    private String title;
    private ColorProduct color;
    private SizeType size;
    private Double weight;
    private WeightUnit weightUnit;
    private DimensionMeasurement dimension;
    private MoneyV2 discount;
}
