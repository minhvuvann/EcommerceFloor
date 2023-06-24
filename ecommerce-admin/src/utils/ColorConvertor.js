export const ColorConvertor = {
    convert: function (color) {
        let colorVN = "";
        switch (color) {
            case "WHITE":
                colorVN = "Trắng";
                break;
            case "BLUE":
                colorVN = "Xanh Da Trời";
                break;
            case  "GREEN":
                colorVN = "Xanh Lá Cây";
                break;
            case "YELLOW":
                colorVN = "vàng";
                break;
            case "PURPLE":
                colorVN = "Tím";
                break;
            case "ORANGE":
                colorVN = "Cam";
                break;
            case "PINK":
                colorVN = "Hồng";
                break;
            case "GRAY":
                colorVN = "Xám";
                break;
            case "RED":
                colorVN = "Đỏ";
                break;
            case "BLACK":
                colorVN = "Đen";
                break;
            case "BROWN":
                colorVN = "Nâu";
                break;
            case "SILVER":
                colorVN = "Bạc";
                break;
            default:
                colorVN = "Khác";
                break;
        }
        return colorVN;
    }

}

export default ColorConvertor;
