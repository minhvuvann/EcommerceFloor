package vn.mellow.ecom.model.enums.utils;

import java.util.HashMap;

public abstract class BaseEnum {
    private static HashMap<String, String> generateEnumMaps(Object[] objDad) {
        HashMap<String, String> enumMap = new HashMap<>();
        for (Object obj : objDad) {
            enumMap.put(obj.toString(), obj.toString());
        }
        return  enumMap;
    }

    public static boolean isExist(Object[] objDad, Object objChild) {
        return generateEnumMaps(objDad).containsKey(objChild.toString());
    }

}
