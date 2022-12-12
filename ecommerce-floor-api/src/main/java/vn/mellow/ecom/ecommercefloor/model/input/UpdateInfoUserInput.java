package vn.mellow.ecom.ecommercefloor.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateInfoUserInput {
    private String fullName;
    private String telephone;
    private String email;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date birthday;
    private String imageUrl;


}
