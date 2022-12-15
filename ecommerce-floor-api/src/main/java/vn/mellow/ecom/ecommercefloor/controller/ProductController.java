package vn.mellow.ecom.ecommercefloor.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.mellow.ecom.ecommercefloor.base.controller.BaseController;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.model.industrial.IndustrialProduct;
import vn.mellow.ecom.ecommercefloor.model.size.DimensionUnit;
import vn.mellow.ecom.ecommercefloor.model.product.Product;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;
import vn.mellow.ecom.ecommercefloor.model.input.WeightUnit;
import vn.mellow.ecom.ecommercefloor.manager.ProductManager;
import vn.mellow.ecom.ecommercefloor.manager.UserManager;
import vn.mellow.ecom.ecommercefloor.model.input.CreateProductInput;
import vn.mellow.ecom.ecommercefloor.model.product.ProductDetail;
import vn.mellow.ecom.ecommercefloor.model.product.ProductFilter;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.utils.GeneralIdUtils;
import vn.mellow.ecom.ecommercefloor.utils.MoneyCalculateUtils;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/product/1.0.0/")
public class ProductController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductManager productManager;
    @Autowired
    private UserManager userManager;

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
        if (null == productInput.getProduct().getIndustrialId()) {
            throw new ServiceException("not_found", "Vui lòng chọn nghành hàng của sản phẩm", "Product industrial type is empty");

        }
        if (null == productInput.getProduct().getName()) {
            throw new ServiceException("not_found", "Vui lòng tên của sản phẩm", "Product name is empty");

        }
        if (null == productInput.getProduct().getShopId()) {
            throw new ServiceException("not_found", "Vui lòng truyền mã shop của sản phẩm", "Product shop Id is empty");
        }
        User user = userManager.getUserShop(productInput.getProduct().getShopId());
        if (null == user) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin shop", "User shop is not found");
        }
        if (null == productInput.getProductVariants()
                || productInput.getProductVariants().isEmpty()
                || productInput.getProductVariants().size() == 0) {
            throw new ServiceException("not_found", "Vui lòng truyền thông tin các biến thể của sản phầm", "Product variant list is empty");
        }
        validateVariants(productInput.getProductVariants());
    }

    private void validateVariants(List<ProductVariant> productVariantList) throws ServiceException {
        for (ProductVariant variant : productVariantList) {
            if (null == variant)
                throw new ServiceException("not_found", "Vui lòng truyền thông tin biến thể của sản phầm", "Product variant is empty");
            if (null == variant.getColor()) {
                throw new ServiceException("not_found", "Vui lòng chọn màu của sản phầm", "Product variant color is empty");
            }
            if (null == variant.getPrice()
                    || variant.getPrice().getAmount() <= 0) {
                throw new ServiceException("not_found", "Vui lòng nhập giá của sản phầm", "Product variant price is empty");
            }
            if (variant.getWeight() <= 0 || variant.getWeight() > 1600000) {
                throw new ServiceException("invalid_data", " Vui lòng nhập khối lượng trong khoảng 0 < và < 1.600.000 gram.", "ProductVariant.weight is null or <= 0");

            }
            if (variant.getImageUrl() == null) {
                throw new ServiceException("invalid_data", "Vui lòng nhập hình ảnh của biến thể sản phẩm.", "ProductVariant.imageUrl is null");
            }
            if (null == variant.getDimension()) {
                throw new ServiceException("invalid_data", " Vui lòng nhập thông tin  kích thước.", "ProductVariant.Dimension is null");
            }
            if (variant.getDimension().getLength() <= 0 ||
                    variant.getDimension().getLength() > 200) {
                throw new ServiceException("invalid_data", " Vui lòng nhập chiều dài trong thông tin  kích thước > 0 và < 200 cm.", "ProductVariant.Dimension.lenght is null or < 0");

            }
            if (variant.getDimension().getWidth() <= 0
                    || variant.getDimension().getWidth() > 200) {
                throw new ServiceException("invalid_data", " Vui lòng nhập chiều rộng trong thông tin  kích thước > 0 và < 200 cm.", "ProductVariant.Dimension.weight is null or < 0");

            }
            if (variant.getDimension().getHeight() <= 0
                    || variant.getDimension().getHeight() > 200) {
                throw new ServiceException("invalid_data", " Vui lòng nhập chiều cao trong thông tin  kích thước > 0 và < 200 cm.", "ProductVariant.Dimension.height is null or < 0");

            }
        }
    }

    @ApiOperation(value = "create a new product")
    @PostMapping("/product/create")
    public ProductDetail createProductVariant(@RequestBody  CreateProductInput productInput) throws ServiceException {
        //validate product input
        validateProductVariantInput(productInput);
        Product product = productInput.getProduct();
        product.setId(GeneralIdUtils.generateId());
        product.setCreatedAt(new Date());
        double medium = 0;
        List<ProductVariant> productVariant = productInput.getProductVariants();
        for (ProductVariant variant : productVariant) {
            variant.setId(GeneralIdUtils.generateId());
            variant.setProductId(product.getId());
            variant.setProductName(product.getName());
            variant.setCreatedAt(new Date());
            variant.setWeightUnit(WeightUnit.GRAMS);
            variant.getDimension().setDimensionUnit(DimensionUnit.CM);
            medium += variant.getPrice().getAmount();
        }
        medium = medium / productVariant.size();

        product.setMediumPrice(MoneyCalculateUtils.getMoney(medium));


        return productManager.createProduct(product, productVariant);
    }

    @ApiOperation(value = "get product by product id")
    @GetMapping("/product/{productId}")
    public Product getProduct(@PathVariable String productId) throws ServiceException {
        Product data = productManager.getProduct(productId);
        if (null == data) {
            throw new ServiceException("not_found", "Không tìm thấy thông tin sản phẩm", "Not found data product by id: " + productId);
        }
        return data;
    }

    @ApiOperation(value = "get product detail by product id")
    @GetMapping("/product/{productId}/detail")
    public ProductDetail getProductDetail(@PathVariable String productId) throws ServiceException {
        return productManager.getProductDetail(productId);
    }

    @ApiOperation(value = "get list industrial")
    @GetMapping("/product/industrials")
    public List<IndustrialProduct> getListIndustrial() throws ServiceException {
        return productManager.getIndustrialProducts();
    }

    @ApiOperation(value = "create industrial product")
    @PostMapping("/product/industrial/create")
    public IndustrialProduct createIndustrialProduct(
            @RequestBody IndustrialProduct industrialProduct) throws ServiceException {
        //validate industrialProduct
        validateIndustrialProductInput(industrialProduct);
        return productManager.createIndustrialProduct(industrialProduct);
    }

    private void validateIndustrialProductInput(IndustrialProduct industrialProduct) throws ServiceException {
        if (null == industrialProduct) {
            throw new ServiceException("invalid_data", "Vui lòng truyền thông tin nghành hàng", "Industrial product is null");
        }
        if (null == industrialProduct.getName()) {
            throw new ServiceException("invalid_data", "Vui lòng truyền tên nghành hàng", "Industrial product name is null");

        }

    }


    @ApiOperation(value = "find product")
    @PostMapping("/product/filter")
    public ResultList<Product> searchProduct(
            @RequestBody ProductFilter productFilter) {
        return productManager.filterProduct(productFilter);
    }


    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllServiceException(ServiceException e) {
        LOGGER.error("ServiceException error.", e);
        return error(e.getErrorCode(), e.getErrorMessage(), e.getErrorDetail());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Object handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return error("internal_server_error", "Có lỗi trong quá trình xử lý", e.getMessage());
    }

}
