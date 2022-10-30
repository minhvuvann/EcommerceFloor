package vn.mellow.ecom.ecommercefloor.enums;

public enum CurrencyCode {
    VND("Việt Nam đồng"), USD("Đô la Mỹ"),
    EUR("Đồng Euro"), GBP("Bảng Anh");

    private final String description;

    private CurrencyCode(String description) {
        this.description = description;
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
        for (CurrencyCode code : values()) {
            listName += code.toString() + ", ";
        }
        return listName;
    }
}
