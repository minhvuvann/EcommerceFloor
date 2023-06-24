export const ConvertorStatus = {
    convert: function ( status) {
        let statusVN = "";
        switch (status) {
            case "READY":
                statusVN = 'MỚI TẠO';
                break;
            case "DELIVERY_ONLY":
                statusVN = 'ĐANG GIAO HÀNG';
                break;
            case  "DELIVERED":
                statusVN = 'ĐÃ GIAO HÀNG';
                break;
            case "CANCELLED":
                statusVN = 'ĐÃ HUỶ';
                break;
            default:
                statusVN = "Khác";
                break;
        }
        return statusVN;
    }

}

export default ConvertorStatus;