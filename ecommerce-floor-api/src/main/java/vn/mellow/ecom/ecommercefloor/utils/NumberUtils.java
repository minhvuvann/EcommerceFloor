package vn.mellow.ecom.ecommercefloor.utils;

public class NumberUtils {
    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray())
            if (c < '0' || c > '9')
                return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(NumberUtils.isNumeric("0"));
        System.out.println(NumberUtils.isNumeric("1"));
        System.out.println(NumberUtils.isNumeric("2202b51"));
    }
}
