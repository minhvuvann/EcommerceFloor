package vn.mellow.ecom.model.product;

import lombok.Data;
import vn.mellow.ecom.base.filter.BaseFilter;

@Data
public class ProductFilter extends BaseFilter {
    private String name;
    private Double priceTo;
    private Double priceFrom;
    private String productId;
    private Integer shopId;
    private String industrialId;
    private String tradeMarkId;
}
