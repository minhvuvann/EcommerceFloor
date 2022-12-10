package vn.mellow.ecom.ecommercefloor.enums;

public enum GeoType {
    WARD("Phường, Xã"), DISTRICT("Quận, Huyện"), PROVINCE("Thành phố");
    private final String description;

    private GeoType(String description) {
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
        for (GeoType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }
}
