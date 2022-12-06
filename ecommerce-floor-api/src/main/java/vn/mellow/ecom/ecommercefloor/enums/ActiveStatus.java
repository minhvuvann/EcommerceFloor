package vn.mellow.ecom.ecommercefloor.enums;

public enum ActiveStatus {
    ACTIVE("Đang hoạt động"),
    INACTIVE("Ngưng hoạt động"),
    CANCELLED("Đã hủy");
    private final String description;

    private ActiveStatus(String description) {
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
        for (ActiveStatus status : values()) {
            listName += status.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);
    }

}
