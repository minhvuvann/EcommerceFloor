package vn.mellow.ecom.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.mellow.ecom.model.enums.GenderType;

import java.util.Date;

@Data
public class UpdateInfoUserDTO {
    private String fullName;
    private String telephone;
    private String email;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date birthday;
    private String imageUrl;
    private GenderType gender;



}
