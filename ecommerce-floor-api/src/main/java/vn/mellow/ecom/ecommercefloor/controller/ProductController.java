package vn.mellow.ecom.ecommercefloor.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.model.ProductVariant;
import vn.mellow.ecom.ecommercefloor.model.input.CreateProductInput;
import vn.mellow.ecom.ecommercefloor.model.product.ProductDetail;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/product/1.0.0/")
public class ProductController extends BaseController {

    private void validateProductVariantInput(CreateProductInput productInput) throws ServiceException {
        if (null == productInput) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin của sản phẩm biến thể", "Product variant is empty");
        }
        if (null == productInput.getProduct()) {
            throw new ServiceException("not_found", "Vui lòng nhập thông tin của sản phẩm", "Product is empty");
        }
        if (null == productInput.getProduct().getDescription()) {
            throw new ServiceException("not_found", "Vui lòng nhập mô tả của sản phẩm", "Product description is empty");
        }
        if (null == productInput.getProduct().getFeaturedImageUrl()) {
            throw new ServiceException("not_found", "Vui lòng nhập hình ảnh mô tả của sản phẩm", "Product featured image url is empty");

        }
        if (null == productInput.getProduct().getIndustrialType()) {
            throw new ServiceException("not_found", "Vui lòng chọn ngành hàng của sản phẩm", "Product industrial type is empty");

        }
        if (null == productInput.getProduct().getName()) {
            throw new ServiceException("not_found", "Vui lòng tên của sản phẩm", "Product name is empty");

        }
        if (null == productInput.getProduct().getShopId()) {
            throw new ServiceException("not_found", "Vui lòng truyền mã shop của sản phẩm", "Product shop Id is empty");
        }
        if (null == productInput.getProductVariants()
                || productInput.getProductVariants().isEmpty()
                || productInput.getProductVariants().size() == 0) {
            throw new ServiceException("not_found", "Vui lòng truyền thông tin các biến thể của sản phầm", "Product variant list is empty");
        }
    }

    private void validateVariants(List<ProductVariant> productVariantList) throws ServiceException {
        for (ProductVariant variant : productVariantList) {
            if (null == variant)
                throw new ServiceException("not_found", "Vui lòng truyền thông tin biến thể của sản phầm", "Product variant is empty");
            if (null==variant.getColor()){
                throw new ServiceException("not_found", "Vui lòng chọn màu của sản phầm", "Product variant color is empty");
            }
            if (variant.getWeight() <= 0) {
                throw new ServiceException("invalid_data", " Vui lòng nhập khối lượng.", "ProductVariant.weight is null or <= 0");

            }
            if (null == variant.getDimension()) {
                throw new ServiceException("invalid_data", " Vui lòng nhập thông tin  kích thước.", "ProductVariant.Dimension is null");
            }
            if (variant.getDimension().getLength() <= 0) {
                throw new ServiceException("invalid_data", " Vui lòng nhập chiều dài trong thông tin  kích thước > 0.", "ProductVariant.Dimension.lenght is null or < 0");

            }
            if (variant.getDimension().getWidth() <= 0) {
                throw new ServiceException("invalid_data", " Vui lòng nhập chiều rộng trong thông tin  kích thước > 0.", "ProductVariant.Dimension.weight is null or < 0");

            }
            if (variant.getDimension().getHeight() <= 0) {
                throw new ServiceException("invalid_data", " Vui lòng nhập chiều cao trong thông tin  kích thước > 0.", "ProductVariant.Dimension.height is null or < 0");

            }
        }
    }

    public ProductDetail createProductVariant(CreateProductInput productInput) {
        ProductDetail productDetail = new ProductDetail();
        return productDetail;
    }

}
