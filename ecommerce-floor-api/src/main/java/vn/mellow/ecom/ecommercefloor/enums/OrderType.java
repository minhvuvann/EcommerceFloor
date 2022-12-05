package vn.mellow.ecom.ecommercefloor.enums;


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
}
