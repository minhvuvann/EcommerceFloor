package vn.mellow.ecom.ecommercefloor.model.product;

import java.util.List;
import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.Product;
import vn.mellow.ecom.ecommercefloor.base.model.ProductVariant;
@Data
public class ProductDetail {
    private Product product;
    private List<ProductVariant> variants;
}
