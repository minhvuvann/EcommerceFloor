package vn.mellow.ecom.ecommercefloor.utils;


import vn.mellow.ecom.ecommercefloor.enums.GenderType;
import vn.mellow.ecom.ecommercefloor.enums.RoleType;
import vn.mellow.ecom.ecommercefloor.enums.ServiceType;
import vn.mellow.ecom.ecommercefloor.enums.UserStatus;

public class TypeUtils {
    public static boolean isGenderType(String typeUpdate) {
        try {
            GenderType.valueOf(typeUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isServiceType(String typeUpdate) {
        try {
            ServiceType.valueOf(typeUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isRoleType(String typeUpdate) {
        try {
           RoleType.valueOf(typeUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
