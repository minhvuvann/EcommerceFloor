package vn.mellow.ecom.ecommercefloor.enums;

import java.util.Locale;

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
}
