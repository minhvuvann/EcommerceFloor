package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.enums.ResponseStatus;


@Data
public class ResponseBody {
    private ResponseStatus status;
    private String message;
    private Object data;

}
