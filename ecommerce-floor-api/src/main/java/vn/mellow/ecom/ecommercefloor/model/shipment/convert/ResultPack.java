package vn.mellow.ecom.ecommercefloor.model.shipment.convert;

import lombok.Data;

import java.util.List;
@Data
public class ResultPack <T> {
    private int code;
    private String message;
    private List<T> data;

}
