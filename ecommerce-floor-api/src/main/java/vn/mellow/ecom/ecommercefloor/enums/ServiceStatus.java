package vn.mellow.ecom.ecommercefloor.enums;

public enum ServiceStatus {
    IN_PROGRESS_CONNECT("Đang kết nối"), CANCELLED("Đã hủy");
    private final String description;


    private ServiceStatus(String description) {
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
        for (ServiceStatus type : values()) {
            listName += type.toString() + ", ";
        }
        return listName.substring(0, listName.length() - 2);
    }
}
