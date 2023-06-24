package vn.mellow.ecom.model.enums;

import vn.mellow.ecom.model.enums.utils.BaseEnum;

public enum ColorProduct {
    WHITE("Màu trắng"),BLUE("Màu xanh da trời"),GREEN("Màu xanh lá cây"),
    YELLOW("Màu vàng"),PURPLE("Màu tím"),ORANGE("Màu da cam"),
    PINK("Màu hồng"),GRAY("Màu xám"),RED("Màu đỏ"),BLACK("Màu đen"),
    BROWN("Màu nâu"),SILVER("Màu bạc");

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
        StringBuilder listName = new StringBuilder();
        for (ColorProduct type : values()) {
            listName.append(type.toString()).append(", ");
        }
        return listName.substring(0, listName.length() - 2);

    }
    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }

}
