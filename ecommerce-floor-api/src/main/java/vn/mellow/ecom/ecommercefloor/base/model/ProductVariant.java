package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;

import java.awt.*;

@Data
public class ProductVariant {
    private String id;
    private String shopId;
    private boolean availableForSale;
    private String barcode;
    private boolean currentlyNotInStock;
    private Image image;
    private MoneyV2 price;
    private Product product;
    private Integer quantityAvailable;
    private boolean requiresShipping;
    private String sku;
    private String title;
    private String unit;
    private Double weight;
    private WeightUnit weightUnit;
    private DimensionMeasurement dimension;
}
