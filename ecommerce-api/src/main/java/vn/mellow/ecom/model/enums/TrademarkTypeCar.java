package vn.mellow.ecom.model.enums;

public enum TrademarkTypeCar {
    HONDA("Sài Gòn Bike Mart","https://cf.shopee.vn/file/ba1c8ad7b5bda4a9db48a0f9051a5af5"),
    HOANG_VIET("Xe máy Hoàng Việt","https://cf.shopee.vn/file/2947f8bc408d75248f0f3360f59895c4"),
    VINFAST("VinFast Official","https://cf.shopee.vn/file/991c7afa9bfa4edd0c35ed4b18330540"),
    YAMAHA("Yamaha Official","https://cf.shopee.vn/file/d9f827426f74b1e4c9594115c245baa8");
    private final String description, url;


    private TrademarkTypeCar(String description, String url) {
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

}
