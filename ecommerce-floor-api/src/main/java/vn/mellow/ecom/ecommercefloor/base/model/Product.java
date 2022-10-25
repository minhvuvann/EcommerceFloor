package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.awt.*;
import java.util.List;

@Data
public class Product {
    private String description;
    private Image featuredImage;
    private String handle;
    private String id;
    private List<Image> images;
    private String onlineStoreUrl;
    private String productType;
    private List<String> tags;
    private String title;

}
