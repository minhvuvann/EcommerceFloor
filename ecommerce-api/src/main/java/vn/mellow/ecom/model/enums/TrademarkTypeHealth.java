package vn.mellow.ecom.model.enums;

public enum TrademarkTypeHealth {
    DHC("DHC Vietnam Official", "https://cf.shopee.vn/file/6bb8c805d197aa1662654f903ef1e294"),
    COCOLUX("Cocolux Official", "https://cf.shopee.vn/file/91f8cb3351a856cdde37bf7b4b049f85"),
    UNICHAIM("Unicharm - Gian hàng Chính hãng", "https://cf.shopee.vn/file/0326201e08a3e883437f85122466c632"),
    MEDICAL("MEDICAL", "https://cf.shopee.vn/file/30cc3014bc0b8dd0ca953ab12d6380da");

    private final String description, url;


    private TrademarkTypeHealth(String description, String url) {
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
