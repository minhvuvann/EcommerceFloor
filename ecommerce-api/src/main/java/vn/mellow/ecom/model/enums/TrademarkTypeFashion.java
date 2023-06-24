package vn.mellow.ecom.model.enums;

public enum TrademarkTypeFashion {
    A("Levents Store","https://cf.shopee.vn/file/f22e36c8526d9a2224cd8bdb4fc2fb60"),
    B("Aviano Menswear","https://cf.shopee.vn/file/513f589c10254512669d3d8b50a6dea7"),
    C("SadBoiz Store","https://cf.shopee.vn/file/5b3525f8ce0718a600b212d8518b5102_tn"),
    D("Owen Store","https://cf.shopee.vn/file/57342653dc9b639f5848dc52c99d94b0_tn");

    private final String description, url;


    private TrademarkTypeFashion(String description, String url) {
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
