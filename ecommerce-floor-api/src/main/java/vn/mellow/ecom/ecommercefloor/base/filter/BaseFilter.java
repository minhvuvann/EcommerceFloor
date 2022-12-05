package vn.mellow.ecom.ecommercefloor.base.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.client.model.Filters;
import lombok.Data;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;
import vn.mellow.ecom.ecommercefloor.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BaseFilter {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdAtFrom;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdAtTo;

    private int maxResult = 20;
    private int offset = 0;
    private String id;
    private String search; // search fulltext

    public int getMaxResult() {
        if (maxResult <= 0) {
            maxResult = 20;
        }
        return maxResult;
    }

    public int getOffset() {
        if (offset < 0) {
            offset = 0;
        }
        return offset;
    }

    public List<Bson> getFilterList() {
        List<Bson> filter = new ArrayList<>();
        if (null != getId() && getId().length() > 0) {
            filter.add(Filters.eq("_id", getId()));
        }
        if (StringUtils.hasText(getSearch())) {
            filter.add(Filters.text(getSearch()));
        }
        if (null != getCreatedAtFrom()) {
            filter.add(Filters.gte("createdAt", DateUtils.getStartDay(getCreatedAtFrom())));
        }
        if (null != getCreatedAtTo()) {
            filter.add(Filters.lte("createdAt", DateUtils.getEndDay(getCreatedAtTo())));
        }
        return filter;
    }


}
