package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum IndustrialType {
    FASHION("Thời trang", "https://cf.shopee.vn/file/687f3967b7c2fe6a134a2c11894eea4b_tn&quot"),
    ELECTRON("Điện tử", "https://cf.shopee.vn/file/978b9e4cb61c611aaaf58664fae133c5_tn&quot"),
    BEAUTY("Sắc đẹp", "https://cf.shopee.vn/file/ef1f336ecc6f97b790d5aae9916dcb72_tn&quot"),
    HEALTH("Sức khoẻ", "https://cf.shopee.vn/file/49119e891a44fa135f5f6f5fd4cfc747_tn&quot"),
    CAR("Ô tô", "https://cf.shopee.vn/file/3fb459e3449905545701b418e8220334_tn&quot"),
    HOUSE_LIFE("Nhà cửa và đời sống", "https://cf.shopee.vn/file/24b194a695ea59d384768b7b471d563f_tn&quot"),
    BOOK_ONLINE("Nhà sách online", "https://cf.shopee.vn/file/36013311815c55d303b0e6c62d6a8139_tn&quot");
    private final String description, url;


    private IndustrialType(String description, String url) {
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
        for (IndustrialType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }

    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
