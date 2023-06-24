export const ConvertorButtonOrderStatus = {
    convert: function (localeList, status) {
        let statusVN = "";
        switch (status) {
            case "READY":
                statusVN = localeList['status_order_delivering_capital'];
                break;
            case "DELIVERY_ONLY":
                statusVN =localeList['status_order_delivered_capital'];
                break;
            default:
                statusVN = "Khác";
                break;
        }
        return statusVN;
    }

}

export default ConvertorButtonOrderStatus;