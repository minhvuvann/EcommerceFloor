package vn.mellow.ecom.model.websocket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 06/05/2023, Thứ Bảy
 **/
@Data
public class Message {
    @Id
    @BsonId
    private String id;
    private String userId;
    private String senderId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date fromAt;
}
