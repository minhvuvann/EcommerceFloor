package vn.mellow.ecom.ecommercefloor.enums;

import vn.mellow.ecom.ecommercefloor.enums.utils.BaseEnum;

public enum TrademarkTypeElectron {
    LG("LG Official Store", "https://cf.shopee.vn/file/4124bc67c3bd8d7f2c6713e4f33aea1e&quot"),
    APPLE("Apple Flagship Store", "https://cf.shopee.vn/file/9833088fd62135c66eac59ef0f3be192&quot"),
    SONY("Sony Authorized Store", "https://cf.shopee.vn/file/8d07eba70c67ed285d116a88abbba7ee&quot"),
    SAMSUNG("SAMSUNG Authorized Store", "https://cf.shopee.vn/file/73d2178d186105221a1fe9f2196a1581&quot");
    private final String description, url;


    private TrademarkTypeElectron(String description, String url) {
        this.description = description;
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static String getListName() {
        String listName = "";
        for (TrademarkTypeElectron type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }

    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

    }
