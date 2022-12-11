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

}
