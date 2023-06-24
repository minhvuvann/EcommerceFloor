package vn.mellow.ecom.model.product;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.base.model.MoneyV2;
import vn.mellow.ecom.utils.FulltextIndex;

import java.util.List;

@Data
public class Product extends BaseModel {
    @FulltextIndex
    private String name;
    private Integer shopId;
    private String industrialId;
    private String industrialTypeName;
    private String description;
    private String featuredImageUrl;
    private List<String> imageUrls;
    private MoneyV2 mediumPrice;
    private MoneyV2 salePrice;
    private String title;
    private long quantityAvailable;
    private double discount;
    //thương hiệu
    private String tradeMarkId;

}
