package vn.mellow.ecom.ecommercefloor.enums;


import vn.mellow.ecom.ecommercefloor.enums.utils.BaseEnum;

public enum OrderType {
    BUY("Mua"), RETURN("Trả lại"), SELL("Bán");
    private final String description;

    private OrderType(String description) {
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
        for (OrderType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
