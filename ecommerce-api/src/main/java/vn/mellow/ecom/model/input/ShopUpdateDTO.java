package vn.mellow.ecom.model.input;

import lombok.Data;
import vn.mellow.ecom.model.enums.ActiveStatus;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 03/04/2023, Thứ Hai
 **/
@Data
public class ShopUpdateDTO {
    private String name;
    private String imageUrl;
    private String description;
    private ActiveStatus status;
}
