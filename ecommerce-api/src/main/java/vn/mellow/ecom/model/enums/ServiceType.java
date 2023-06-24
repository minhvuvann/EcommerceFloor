package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum ServiceType {
    GOOGLE("Tài khoản GOOGLE"),
    FACEBOOK("Tài khoản FACEBOOK"),
    NORMALLY("Tài khoản Thường");

    private final String description;


    private ServiceType(String description) {
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
        for (ServiceType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);
    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }


}
