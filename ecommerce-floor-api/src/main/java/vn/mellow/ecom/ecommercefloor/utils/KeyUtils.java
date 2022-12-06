package vn.mellow.ecom.ecommercefloor.utils;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
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
    //MTIzNDU2 = 123456
    public static String hashBase64Encoder(String key)  {
       return new String(Base64.encodeBase64String(key.getBytes()));
    }
    public static String decodeBase64Encoder(String key){
        return new String(Base64.decodeBase64(key.getBytes()));
    }
    public static String SHA256(String key){
        return DigestUtils.sha256Hex(key);
    }


    public static boolean isBCryptEncoder(String key, String encode) {
        return new BCryptPasswordEncoder().matches(key, encode);
    }


}
