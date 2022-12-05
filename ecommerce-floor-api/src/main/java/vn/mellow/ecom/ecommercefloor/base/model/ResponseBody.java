package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import vn.mellow.ecom.ecommercefloor.enums.BasicStatus;


@Data
@AllArgsConstructor
public class ResponseBody {
    private BasicStatus status;
    private String message;
    private Object data;

}
