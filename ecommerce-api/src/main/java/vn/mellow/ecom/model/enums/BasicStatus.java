package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum BasicStatus {
    success("Thành công"), failure("Thất bại"),
    error("Lỗi"), pending("Treo");
    private final String description;

    private BasicStatus(String description) {
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
        for (BasicStatus status : values()) {
            listName += status.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }


}
