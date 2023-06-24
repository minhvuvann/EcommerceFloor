package vn.mellow.ecom.model.bank;

import lombok.Data;

import java.util.List;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 10/05/2023, Thứ Tư
 **/
@Data
public class DataHistory {
    private int page;
    private int pageSize;
    private int prevPage;
    private int totalPages;
    private int totalRecords;
    private List<RecordHistory> records ;
}
