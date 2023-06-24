package vn.mellow.ecom.model.user;

import lombok.Data;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.model.enums.ScoreType;

@Data
public class Score extends BaseModel {
    private String userId;
    private ScoreType type;
    private long score;
}
