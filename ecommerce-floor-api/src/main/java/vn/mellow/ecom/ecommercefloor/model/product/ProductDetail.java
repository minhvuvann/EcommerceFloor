package vn.mellow.ecom.ecommercefloor.model.product;

import java.util.List;
import lombok.Data;
import vn.mellow.ecom.ecommercefloor.model.shop.Shop;

@Data
public class ProductDetail {
    private Product product;
    private Shop shop;
    private List<ProductVariant> variants;
}
