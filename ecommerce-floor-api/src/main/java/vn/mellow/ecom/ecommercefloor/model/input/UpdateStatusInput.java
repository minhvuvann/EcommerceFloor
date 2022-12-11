package vn.mellow.ecom.ecommercefloor.model.input;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
@Data
public class UpdateStatusInput {
    private String note;
    private String status;
    private ActivityUser byUser;
}
