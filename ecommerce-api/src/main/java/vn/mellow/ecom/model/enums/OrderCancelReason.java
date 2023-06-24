package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

/**
 * Represents the reason for the order's cancellation.
 */
public enum OrderCancelReason {

    CUSTOMER("Khách hàng"),
    FRAUD("Lừa đảo"),
    SHOP("Cửa hàng"),
    ADMIN("Quản trị viên"),
    OTHER("Khác");

    private final String description;

    private OrderCancelReason(String description) {
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
        for (OrderCancelReason type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }

    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }


}
