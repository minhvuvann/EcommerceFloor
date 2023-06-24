package vn.mellow.ecom.model.input;


import lombok.Data;
import vn.mellow.ecom.model.product.Product;
import vn.mellow.ecom.model.product.ProductVariant;

import java.util.List;

@Data
public class CreateProductDTO {
    private Product product;
    private List<ProductVariant> productVariants ;
}
