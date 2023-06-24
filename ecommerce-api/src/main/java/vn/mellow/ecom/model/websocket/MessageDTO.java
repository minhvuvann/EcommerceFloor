package vn.mellow.ecom.model.websocket;


import lombok.Data;
import org.modelmapper.ModelMapper;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 23/05/2023, Thứ Ba
 **/
@Data
public class MessageDTO {
    private String byFrom;
    private String byTo;
    private String message;

    public static Message fromMessage(MessageDTO message) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(message, Message.class);
    }

}
