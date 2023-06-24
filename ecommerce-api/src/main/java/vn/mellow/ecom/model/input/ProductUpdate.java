package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.base.model.MoneyV2;

import java.util.List;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 21/02/2023, Thứ Ba
 **/
@Data
public class ProductUpdate {
    private String name;
    private Integer shopId;
    private String industrialId;
    private String industrialTypeName;
    private String description;
    private String featuredImageUrl;
    private List<String> imageUrls;
    private double mediumPrice;
    private String title;
    //thương hiệu
    private String tradeMarkId;
}
