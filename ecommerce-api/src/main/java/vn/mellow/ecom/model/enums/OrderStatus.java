package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum OrderStatus {
    READY("Mới tạo"),
    DELIVERY_ONLY("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy");


    private final String description;

    private OrderStatus(String description) {
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
        for (OrderStatus status : values()) {
            listName += status.toString() + ", ";
        }
        return listName;
    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
