package vn.mellow.ecom.model.bank;

import lombok.Data;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 22/05/2023, Thứ Hai
 **/
@Data
public class SyncBank {
    private int error;
    private String message;
    private Object data;
}
