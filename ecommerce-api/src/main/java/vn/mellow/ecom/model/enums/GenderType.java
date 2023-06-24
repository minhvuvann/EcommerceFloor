package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum GenderType {
    MAN("Nam"), WOMEN("Nữ"), OTHER("Khác");
    private final String description;

    private GenderType(String description) {
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
        for (GenderType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }


}

