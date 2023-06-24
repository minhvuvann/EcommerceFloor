package vn.mellow.ecom.model.enums;


import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum OrderType {
     PURCHASE("Mua"), SELL("Bán");
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
        StringBuilder listName = new StringBuilder();
        for (OrderType type : values()) {
            listName.append(type.toString()).append(", ");
        }
        return listName.substring(0, listName.length() - 2);

    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
