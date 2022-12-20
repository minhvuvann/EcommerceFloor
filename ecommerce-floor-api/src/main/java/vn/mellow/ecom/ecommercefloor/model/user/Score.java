package vn.mellow.ecom.ecommercefloor.model.user;

import lombok.Data;
import vn.mellow.ecom.ecommercefloor.base.model.BaseModel;
import vn.mellow.ecom.ecommercefloor.enums.ScoreType;

@Data
public class Score extends BaseModel {
    private String userId;
    private ScoreType type;
    private long score;
}
