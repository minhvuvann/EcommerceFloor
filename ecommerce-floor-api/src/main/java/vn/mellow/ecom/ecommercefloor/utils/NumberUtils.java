package vn.mellow.ecom.ecommercefloor.utils;

import java.util.Date;
import java.util.Random;

public class NumberUtils {
    private static final Random RANDOM = new Random();

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray())
            if (c < '0' || c > '9')
                return false;
        return true;
    }

    public static String generateTelePhone() {

        int num2 = RANDOM.nextInt(643) + 100;
        int num3 = RANDOM.nextInt(900) + 100;
        return "0988" +  String.valueOf(num2) + String.valueOf(num3);

    }

    public static Date generateDate() {
        Long ms = -946771200000L + (Math.abs(RANDOM.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        return new Date(ms);

    }

    public static void main(String[] args) {
        System.out.println(generateTelePhone());
    }
}
