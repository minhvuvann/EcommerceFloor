package vn.mellow.ecom.ecommercefloor.enums;

public enum TrademarkTypeBeauty {
    INNISFREE("Innisfree Official Store","https://cf.shopee.vn/file/72c005285900078530d2c05f32dcca82")
    ,ANESSA("Anessa Official Store","https://cf.shopee.vn/file/419d8cb6766040e5dae7730ea2dfa349"),
    VICHY("Vichy - Gian Hàng Chính Hãng","https://cf.shopee.vn/file/f1bba1c8c18640e45f5d7d3226226df6")
    ,UNILIVER("Unilever Việt Nam _ Health & Beauty","https://cf.shopee.vn/file/9adcc344099cf09399ac22f0d0b3a6ea");

    private final String description, url;


    private TrademarkTypeBeauty(String description, String url) {
        this.description = description;
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDescription() {
        return this.description;
    }
}
