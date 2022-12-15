package vn.mellow.ecom.ecommercefloor.model.product;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.filter.BaseFilter;
import vn.mellow.ecom.ecommercefloor.enums.IndustrialType;

@Data
public class ProductFilter extends BaseFilter {
    private String name;
    private Double priceTo;
    private Double priceFrom;
    private String productId;
    private String shopId;
    private String industrialId;
}
