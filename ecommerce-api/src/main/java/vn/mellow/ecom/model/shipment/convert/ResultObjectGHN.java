package vn.mellow.ecom.model.shipment.convert;

import lombok.Data;

@Data
public class ResultObjectGHN {
    private int code;
    private String message;
    private Object data;
}
