package vn.mellow.ecom.ecommercefloor.enums;

public enum RoleType {
    ADMIN("Quản trị viên"), STORE("Cửa hàng"),
    PERSONAL("Cá nhân"), OTHER("Khác");
    private final String description;

    private RoleType(String description) {
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
        for (RoleType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }
}
