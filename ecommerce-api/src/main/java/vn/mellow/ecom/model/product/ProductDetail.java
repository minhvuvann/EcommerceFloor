package vn.mellow.ecom.model.product;

import lombok.Data;
import vn.mellow.ecom.model.shop.Shop;

import java.util.List;

@Data
public class ProductDetail {
    private Product product;
    private Shop shop;
    private List<ProductVariant> variants;
}
