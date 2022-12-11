package vn.mellow.ecom.ecommercefloor.enums;

import vn.mellow.ecom.ecommercefloor.enums.utils.BaseEnum;

public enum RoleType {
    ADMIN("Quản trị viên"), STORE("Cửa hàng"),
    PERSONAL("Cá nhân"),PERSONAL_STORE("Cửa hàng và cá nhân"), OTHER("Khác");
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
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
