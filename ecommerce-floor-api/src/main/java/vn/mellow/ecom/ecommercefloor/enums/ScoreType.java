package vn.mellow.ecom.ecommercefloor.enums;

import vn.mellow.ecom.ecommercefloor.enums.utils.BaseEnum;
import vn.mellow.ecom.ecommercefloor.model.user.Score;

public enum ScoreType {
    RANK_BRONZE("Hạng đồng"), RANK_SILVER("Tích điểm cho người bán"), RANK_GOLD("Hạng vàng");
    private final String description;

    private ScoreType(String description) {
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
        for (ScoreType type : values()) {
            listName += type.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }

    public static boolean isExist(Object current) {
        return BaseEnum.isExist(values(), current);
    }
}
