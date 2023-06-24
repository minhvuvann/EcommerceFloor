package vn.mellow.ecom.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class BaseController {

    protected Object error(String errorCode, String errorMessage, String errorDetail) {
        HashMap<String, String> result = new HashMap<>();
        result.put("errorCode", errorCode);
        result.put("errorMessage", errorMessage);
        result.put("errorDetail", errorDetail);
        return result;
    }

    protected Object success(Object data) {
        return data;
    }
}
