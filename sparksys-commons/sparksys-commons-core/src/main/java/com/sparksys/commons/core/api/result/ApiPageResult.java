package com.sparksys.commons.core.api.result;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * description: API接口分页响应结果
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:46:08
 */
@Getter
@Setter
public class ApiPageResult<T> implements Serializable {

    private static final long serialVersionUID = 1365552252832702673L;
    private long pageNum;
    private long pageSize;
    private long totalPage;
    private long totalNum;
    private List<T> list;

    public ApiPageResult() {

    }

    public ApiPageResult(long pageNum, long pageSize, long totalPage, long totalNum, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalNum = totalNum;
        this.list = list;
    }

    public static <T> ApiPageResult timeOut() {
        return new ApiPageResult<>();
    }
}
