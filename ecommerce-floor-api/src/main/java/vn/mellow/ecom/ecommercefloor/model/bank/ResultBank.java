package vn.mellow.ecom.ecommercefloor.model.bank;

import lombok.Data;

import java.util.List;
@Data
public class ResponseBank<T> {
    private String code;
    private String desc;
    private List<T> data;
}
