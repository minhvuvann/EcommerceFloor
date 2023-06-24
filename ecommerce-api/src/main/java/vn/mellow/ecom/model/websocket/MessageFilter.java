package vn.mellow.ecom.model.websocket;

import lombok.Data;
import vn.mellow.ecom.base.filter.BaseFilter;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 06/05/2023, Thứ Bảy
 **/
@Data
public class MessageFilter extends BaseFilter {
    private String userId;
    private String senderId;

}
