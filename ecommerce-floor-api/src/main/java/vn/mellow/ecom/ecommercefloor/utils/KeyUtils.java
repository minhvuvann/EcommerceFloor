package vn.mellow.ecom.ecommercefloor.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class KeyUtils {
    private static final String TOKEN = "2604200119112001";

    public static String hashBCryptEncoder(String key) {
        return new BCryptPasswordEncoder().encode(key);
    }

    public static String getToken() {

        return DigestUtils.sha512Hex(TOKEN);
    }


    public static boolean isBCryptEncoder(String key, String encode) {
        return new BCryptPasswordEncoder().matches(key, encode);
    }

}
