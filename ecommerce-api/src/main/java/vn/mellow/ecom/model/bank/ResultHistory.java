package vn.mellow.ecom.model.bank;

import lombok.Data;

import java.util.List;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 28/03/2023, Thứ Ba
 **/
@Data
public class ResultHistory {
    private int error;
    private String message;
    private DataHistory data;
}