package vn.mellow.ecom.ecommercefloor.model.input;


import lombok.Data;
import vn.mellow.ecom.ecommercefloor.model.product.Product;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;

import java.util.List;
@Data
public class CreateProductInput {
    private Product product;
    private List<ProductVariant> productVariants ;
}
