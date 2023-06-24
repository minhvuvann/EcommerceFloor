export const StatusColor = {
    convert: function (status) {
        let className = "";
        switch (status) {
            case "READY":
                className = "status-ready";
                break;
            case "DELIVERY_ONLY":
                className = "status-delivering";
                break;
            case  "DELIVERED":
                className = "status-delivered";
                break;
            case "CANCELLED":
                className = "status-cancel";
                break;
            default:
                className = "Khác";
                break;
        }
        return className;
    }

}

export default StatusColor;