package vn.mellow.ecom.ecommercefloor.enums;

public enum PasswordStatus {
    NEW("Mới nhất"), OLD("Đã thay đổi"), CANCELLED("Đã hủy");
    private final String description;

    private PasswordStatus(String description) {
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
        for (PasswordStatus status : values()) {
            listName += status.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);

    }


}
