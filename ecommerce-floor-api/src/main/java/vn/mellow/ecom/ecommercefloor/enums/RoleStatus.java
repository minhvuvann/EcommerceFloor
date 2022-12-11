package vn.mellow.ecom.ecommercefloor.enums;

import vn.mellow.ecom.ecommercefloor.enums.utils.BaseEnum;

public enum RoleStatus {
    ACTIVE("Đang hoạt động"),
    INACTIVE("Ngưng hoạt động"),
    CANCELLED("Đã hủy");
    private final String description;

    private RoleStatus(String description) {
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
        for (RoleStatus status : values()) {
            listName += status.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
