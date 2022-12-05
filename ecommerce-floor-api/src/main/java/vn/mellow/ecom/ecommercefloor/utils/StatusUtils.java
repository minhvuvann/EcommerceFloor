package vn.mellow.ecom.ecommercefloor.utils;

import vn.mellow.ecom.ecommercefloor.enums.PasswordStatus;
import vn.mellow.ecom.ecommercefloor.enums.RoleStatus;
import vn.mellow.ecom.ecommercefloor.enums.ServiceStatus;
import vn.mellow.ecom.ecommercefloor.enums.UserStatus;

public class StatusUtils {
    public static boolean isUserStatus(String statusUpdate) {
        try {
            UserStatus.valueOf(statusUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isPasswordStatus(String statusUpdate) {
        try {
            PasswordStatus.valueOf(statusUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isServiceStatus(String statusUpdate) {
        try {
           ServiceStatus.valueOf(statusUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isRoleStatus(String statusUpdate) {
        try {
            RoleStatus.valueOf(statusUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
