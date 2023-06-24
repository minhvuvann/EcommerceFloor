package vn.mellow.ecom.base.logs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import vn.mellow.ecom.model.enums.ActivityLogType;

import java.util.Date;

@Data
public class ActivityLog {
    @Id
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdAt;
    private ActivityUser user;
    private String requestId;
    private String requestType;
    private ActivityLogType type;
    private String description;
}
