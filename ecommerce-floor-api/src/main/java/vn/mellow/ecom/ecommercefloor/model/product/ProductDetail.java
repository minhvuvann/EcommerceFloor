package vn.mellow.ecom.ecommercefloor.model.product;

import java.util.List;
import lombok.Data;

@Data
public class ProductDetail {
    private Product product;
    private List<ProductVariant> variants;
}
