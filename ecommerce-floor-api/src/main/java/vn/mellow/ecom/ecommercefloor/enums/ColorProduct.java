package vn.mellow.ecom.ecommercefloor.enums;

public enum ColorProduct {
    WHITE("Màu trắng"),BLUE("Màu xanh da trời"),GREEN("Màu xanh lá cây"),
    YELLOW("Màu vàng"),PURPLE("Màu tím"),ORANGE("Màu da cam"),
    PINK("Màu hồng"),GRAY("Màu xám"),RED("Màu đỏ"),BLACK("Màu đen"),
    BROWN("Màu nâu");

    private final String description;

    private ColorProduct(String description) {
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
        for (ColorProduct type : values()) {
            listName += type.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }
}
