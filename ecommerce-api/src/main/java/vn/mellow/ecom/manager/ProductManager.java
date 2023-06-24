package vn.mellow.ecom.manager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import org.springframework.stereotype.Repository;
import vn.mellow.ecom.base.filter.ResultList;
import vn.mellow.ecom.base.manager.BaseManager;
import vn.mellow.ecom.model.industrial.IndustrialProduct;
import vn.mellow.ecom.model.input.ProductUpdate;
import vn.mellow.ecom.model.product.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import static com.mongodb.client.model.ReturnDocument.AFTER;

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

    public List<Product> getProductALLs() {
        return getProductCollection().find().into(new ArrayList<>());
    }


    public List<ProductVariant> getProductVariants(String productId) {
        return getProductVariantCollection().
                find(Filters.eq("productId", productId)).into(new ArrayList<>());
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

    //nghành hàng
    private MongoCollection<IndustrialProduct> industrialProductMongoCollection;

    public MongoCollection<IndustrialProduct> getIndustrialProductCollection() {
        if (null == industrialProductMongoCollection) {
            industrialProductMongoCollection = getCollection(IndustrialProduct.class);
        }
        return industrialProductMongoCollection;
    }

    public IndustrialProduct createIndustrialProduct(IndustrialProduct industrialProduct) {
        industrialProduct.setCreatedAt(new Date());
        industrialProduct.setId(generateId());
        industrialProduct.setUpdatedAt(null);
        getIndustrialProductCollection().insertOne(industrialProduct);
        return industrialProduct;

    }

    public void deleteProduct(String productId) {
        Document updateDocument = new Document();
        updateDocument.put("updatedAt", new Date());
        updateDocument.put("_id", productId + "_deleted");
        Document newDocument = new Document();
        newDocument.append("$set", updateDocument);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("_id", productId));
        getProductCollection().findOneAndUpdate(Filters.and(filters), newDocument, options);

    }

    public Product updateProduct(String productId, ProductUpdate productUpdate) {
        getLogger().log(Level.INFO, "Cập nhật thông tin của sản phẩm :" + productId);
        Document document = new Document();
        document.put("updatedAt", new Date());
        // check data before update
        if (null != productUpdate.getName())
            document.put("name", productUpdate.getName());
        if (null != productUpdate.getShopId())
            document.put("shopId", productUpdate.getShopId());
        if (null != productUpdate.getIndustrialId())
            document.put("industrialId", productUpdate.getIndustrialId());
        if (null != productUpdate.getIndustrialTypeName())
            document.put("industrialTypeName", productUpdate.getIndustrialTypeName());
        if (null != productUpdate.getDescription())
            document.put("description", productUpdate.getDescription());
        if (null != productUpdate.getFeaturedImageUrl())
            document.put("imageUrl", productUpdate.getImageUrls());
        if (0 != productUpdate.getMediumPrice())
            document.put("mediumPrice.amount", productUpdate.getMediumPrice());
        if (null != productUpdate.getTitle())
            document.put("title", productUpdate.getTitle());
        if (null != productUpdate.getTradeMarkId())
            document.put("tradeMarkId", productUpdate.getTradeMarkId());

        Document newDocument = new Document();
        newDocument.append("$set", document);
        // avoid update at the same time
        List<Bson> filter = getUpdateFilter();
        filter.add(Filters.eq("_id", productId));
        // display data after updated
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(AFTER);
        // update data
        return getProductCollection().findOneAndUpdate(Filters.eq(filter), newDocument, options);
    }

    public List<IndustrialProduct> getIndustrialProducts() {
        return getIndustrialProductCollection().find().into(new ArrayList<>());
    }

    public IndustrialProduct getIndustrialProduct(String name) {
        return getIndustrialProductCollection().find(Filters.eq("name", name)).first();

    }

    //Thương hiệu
    private MongoCollection<Trademark> trademarkMongoCollection;

    public MongoCollection<Trademark> getTrademarkCollection() {
        if (null == trademarkMongoCollection) {
            trademarkMongoCollection = getCollection(Trademark.class);
        }
        return trademarkMongoCollection;
    }

    public Trademark getTrademark(String trademarkId) {
        return getTrademarkCollection().find(Filters.eq("_id", trademarkId)).first();
    }

    public Trademark getTrademarkByIndustrialId(String industrialId) {
        return getTrademarkCollection().find(Filters.eq("industrialId", industrialId)).first();
    }

    public List<Trademark> getTrademarkByIndustrials(String industrialId) {
        return getTrademarkCollection().find(Filters.eq("industrialId", industrialId)).into(new ArrayList<>());
    }

    public List<Trademark> getTradeMarks() {
        return getTrademarkCollection().find().into(new ArrayList<>());
    }

    public Trademark createTrademark(Trademark trademark) {
        trademark.setId(generateId());
        trademark.setCreatedAt(new Date());
        trademark.setUpdatedAt(null);
        getTrademarkCollection().insertOne(trademark);
        return trademark;
    }

    public void updateVariantPrice(String productId, double price) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("price.amount", price);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductVariantCollection().updateOne(filter, newDocument);
    }

    public void updateVariantDisCount(String productId, double discount) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("discount", discount);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductVariantCollection().updateOne(filter, newDocument);
    }

    public void updateVariantSalePrice(String productId, double price) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("salePrice.amount", price);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductVariantCollection().updateOne(filter, newDocument);
    }

    public void updateDisCount(String productId, long discount) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("discount", discount);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductCollection().updateOne(filter, newDocument);
    }

    public void updateSalePrice(String productId, int price) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("salePrice.amount", price);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductCollection().updateOne(filter, newDocument);
    }

    public void updateMediumPrice(String productId, int price) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("mediumPrice.amount", price);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductCollection().updateOne(filter, newDocument);
    }

    public void updateQuantityAv(String productId, long quantity) {
        Bson filter = Filters.eq("_id", productId);
        Document document = new Document();
        document.put("quantityAvailable", quantity);
        Document newDocument = new Document();
        newDocument.append("$set", document);
        getProductCollection().updateOne(filter, newDocument);
    }

    public ResultList<Product> filterProduct(ProductFilter productFilter) {
        List<Bson> filter = getFilters(productFilter);
        if (null != productFilter.getPriceFrom() &&
                null != productFilter.getPriceTo()) {
            betweenFilter("mediumPrice.amount",
                    productFilter.getPriceFrom(),
                    productFilter.getPriceTo(), filter);
        }
        if (null != productFilter.getTradeMarkId())
            appendFilter(productFilter.getTradeMarkId(), "tradeMarkId", filter);
        if (null != productFilter.getName())
            appendFilter(productFilter.getName(), "name", filter);
        if (null != productFilter.getShopId())
            appendFilter(productFilter.getShopId(), "shopId", filter);
        if (null != productFilter.getIndustrialId())
            appendFilter(productFilter.getIndustrialId(), "industrialId", filter);
        if (null != productFilter.getProductId())
            appendFilter(productFilter.getProductId(), "_id", filter);

        return getResultList(getProductCollection(), filter, productFilter.getOffset(), productFilter.getMaxResult());
    }

}
