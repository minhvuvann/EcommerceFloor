package vn.mellow.ecom.base.filter;

import lombok.Data;

import java.util.List;

@Data
public class ResultList<T> {
    private long total;
    private List<T> resultList;
    private long index;
    private long maxResult;

}
