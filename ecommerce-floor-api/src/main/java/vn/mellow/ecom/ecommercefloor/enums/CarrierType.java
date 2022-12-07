package vn.mellow.ecom.ecommercefloor.enums;

public enum CarrierType {
    GHN("Giao hàng nhanh"), GHTK("Giao hàng tiết kiệm");
    private final String description;

    private CarrierType(String description) {
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
        for (CarrierType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }
}
