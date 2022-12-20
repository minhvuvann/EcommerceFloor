package vn.mellow.ecom.ecommercefloor.enums;

public enum TrademarkTypeBook {
    THANH_HA("Thanh Hà Store","https://cf.shopee.vn/file/60269c467e394b5ccc0b1841fb92e4a5"),
    SACH_HAY_NHAT("sachhaynhat.vn","https://cf.shopee.vn/file/79f94879482616faf6aeadd4f9472bd5"),
    VADATABOOKS("VADATABOOKS","https://cf.shopee.vn/file/72fda983ee4e12ada9bec59cfd9fc7ce"),
    TU_SACH_UOM_MAM("Tủ Sách Ươm Mầm Store","https://cf.shopee.vn/file/558d8cea3e394701cb191a7217bb9be2");
    private final String description, url;


    private TrademarkTypeBook(String description, String url) {
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
