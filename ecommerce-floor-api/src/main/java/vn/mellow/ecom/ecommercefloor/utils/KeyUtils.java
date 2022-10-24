package vn.mellow.ecom.ecommercefloor.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyUtils {
    private static String token = "26040119112001";

    public static String hashBCryptEncoder(String key) {
        return new BCryptPasswordEncoder().encode(key);
    }

    public static String getToken() {

        return DigestUtils.sha512Hex(token);
    }


    public static boolean isBCryptEncoder(String key, String encode) {
        return new BCryptPasswordEncoder().matches(key, encode);
    }

    public static void main(String[] args) {
        System.out.println(getToken());  }
    //463108ebb34848f24f03e0c6df08d757e2b855c9bfe6ec3fd38542fca3f6958fa7d5183feb7176b8c0f2ec91aaafb65adc50eaa314103f7c2031d2392b5861df

}
