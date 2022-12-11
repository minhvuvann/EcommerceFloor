package vn.mellow.ecom.ecommercefloor.enums;

import vn.mellow.ecom.ecommercefloor.enums.utils.BaseEnum;

public enum IndustrialType {
    FASHION("Thời trang"),ELECTRON("Điện tử"),
    BEAUTY("Sắc đẹp"), HEALTH("Sức khoẻ"),CAR("Ô tô"),
    TRAVEL_LUGGAGE("Du lịch và hành lý");
    private final String description;

    private IndustrialType(String description) {
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
        for (IndustrialType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName;
    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
