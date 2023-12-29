package vn.mellow.ecom.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 27/04/2023, Thứ Năm
 **/
public class DiscountUtils {
    public static double discount() {
        double number = (new Random().nextInt(100) + 1);
        return number / 100;
    }

    public static int calculateSalePrice(double priceIN, long discountIN) {
        BigDecimal price = new BigDecimal(priceIN);
        BigDecimal discount = new BigDecimal(discountIN);
        if (price.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.valueOf(100)) > 0) {
            System.out.println("Giá gốc và phần trăm giảm phải lớn hơn 0, và phần trăm giảm không được lớn hơn 100.");
            return 0;
        }
        BigDecimal phanTramChietKhau = discount.divide(BigDecimal.valueOf(100));
        BigDecimal chietKhau = price.multiply(phanTramChietKhau);
        return price.subtract(chietKhau).setScale(-3, RoundingMode.FLOOR).intValue();
    }



}
