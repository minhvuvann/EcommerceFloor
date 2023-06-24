package vn.mellow.ecom.model.product;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.base.model.MoneyV2;
import vn.mellow.ecom.model.enums.ColorProduct;
import vn.mellow.ecom.model.enums.SizeType;
import vn.mellow.ecom.model.enums.WeightUnit;
import vn.mellow.ecom.model.size.DimensionMeasurement;

@Data
public class ProductVariant extends BaseModel {
    private String imageUrl;
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
    private MoneyV2 salePrice;
    private double discount;
}
