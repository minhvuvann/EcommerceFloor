package vn.mellow.ecom.ecommercefloor.enums;

public enum ResponseStatus {
    success("Thành công"), failure("Thất bại"),
    error("Lỗi"), pending("Treo");
    private final String description;

    private ResponseStatus(String description) {
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
        for (ResponseStatus status : values()) {
            listName += status.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }

}
