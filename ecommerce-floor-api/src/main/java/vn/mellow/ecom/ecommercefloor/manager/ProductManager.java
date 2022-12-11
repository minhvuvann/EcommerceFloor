package vn.mellow.ecom.ecommercefloor.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import vn.mellow.ecom.ecommercefloor.base.filter.ResultList;
import vn.mellow.ecom.ecommercefloor.base.manager.BaseManager;
import vn.mellow.ecom.ecommercefloor.model.product.Product;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;
import vn.mellow.ecom.ecommercefloor.model.product.ProductDetail;
import vn.mellow.ecom.ecommercefloor.model.product.ProductFilter;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductManager extends BaseManager {
    public ProductManager(MongoClient mongoClient) {
        super(mongoClient);
    }

    private MongoCollection<Product> productMongoCollection;

    public MongoCollection<Product> getProductCollection() {
        if (null == productMongoCollection) {
            productMongoCollection = getCollection(Product.class);
        }
        return productMongoCollection;
    }

    private MongoCollection<ProductVariant> variantMongoCollection;

    public MongoCollection<ProductVariant> getProductVariantCollection() {
        if (null == variantMongoCollection) {
            variantMongoCollection = getCollection(ProductVariant.class);
        }
        return variantMongoCollection;
    }

    public Product getProduct(String productId) {
        return getProductCollection().find(Filters.eq("_id", productId)).first();
    }

    public ProductVariant getProductVariant(String variantId) {
        return getProductVariantCollection().find(Filters.eq("_id", variantId)).first();

    }

    public List<ProductVariant> getProductVariants(String productId) {
        return getProductVariantCollection().find(Filters.eq("_id", productId)).into(new ArrayList<>());
    }

    public ProductDetail getProductDetail(String productId) {
        ProductDetail productDetail = new ProductDetail();
        Product product = getProduct(productId);
        List<ProductVariant> variants = getProductVariants(productId);
        if (null != variants) {
            productDetail.setVariants(variants);
        }
        productDetail.setProduct(product);
        return productDetail;


    }

    public ProductDetail createProduct(Product product, List<ProductVariant> variantList) {
        getProductCollection().insertOne(product);
        getProductVariantCollection().insertMany(variantList);
        return getProductDetail(product.getId());

    }

    public ResultList<Product> filterProduct(ProductFilter productFilter) {
        List<Bson> filter = getFilters(productFilter);
        if (null != productFilter.getPriceFrom() &&
                null != productFilter.getPriceTo()) {
            betweenFilter(productFilter.getPriceFrom(), productFilter.getPriceTo(), filter);
        }
        appendFilter("name", productFilter.getName(),filter);
        appendFilter("shopId", productFilter.getShopId(),filter);
        appendFilter("industrialType", productFilter.getIndustrialType().toString(),filter);
        appendFilter("_id", productFilter.getId(),filter);

        return getResultList(getProductCollection(),filter,productFilter.getOffset(),productFilter.getMaxResult());
    }
}
