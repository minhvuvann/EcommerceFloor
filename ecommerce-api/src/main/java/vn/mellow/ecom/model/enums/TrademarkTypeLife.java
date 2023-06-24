package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum TrademarkTypeLife {
    LOCK_LOCK("Lock&Lock Official Store","https://cf.shopee.vn/file/a4195e7056df7bd890a4e21a62442ead"),
    ĐQ("Điện Quang Offical Store","https://cf.shopee.vn/file/2939fff9818838fb3be48acc9303ac3d"),
    SUN_HOUSE("SUNHOUSE GROUP JSC","https://cf.shopee.vn/file/af8375dd3b0eef0e3783486fd01b5039"),
    RANG_DONG("Rạng Đông Store","https://cf.shopee.vn/file/8e1b0a80f31db7f9790adbbdeff64418");

    private final String description, url;


    private TrademarkTypeLife(String description, String url) {
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
        for (TrademarkTypeLife type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }

    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }
}
