package vn.mellow.ecom.ecommercefloor.utils;

import com.github.javafaker.Faker;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class RemoveAccentUtils {
    public static String removeAccent(String text) {
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public static String generateUserName(String text) {
        if (null == text) {
            Faker faker = new Faker();
            text = faker.name().fullName();
        }
        String generateUserName = String.valueOf(System.currentTimeMillis());
        return removeAccent(text).replaceAll(" ", "").trim() +
                generateUserName.substring(generateUserName.length() - 4, generateUserName.length());
    }

}
