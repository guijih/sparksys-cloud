package com.sparksys.commons.mybatis.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sparksys.commons.core.api.result.ApiPageResult;

import java.util.List;

/**
 * description: mybatis分页
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:22:54
 */
public class PageResult extends ApiPageResult {


    public static <T> ApiPageResult resetPage(IPage<T> tPage) {
        PageResult pageResult = new PageResult();
        processPage(tPage, pageResult);
        pageResult.setList(tPage.getRecords());
        return pageResult;
    }

    /**
     * 分页数据组装处理
     *
     * @param tPage
     * @return PageBean<T>
     * @author zhouxinlei
     * @date 2019-09-09 18:08:40
     */
    public static <T> ApiPageResult resetPage(IPage<T> tPage, List<T> doList) {
        PageResult pageResult = new PageResult();
        processPage(tPage, pageResult);
        pageResult.setList(doList);
        return pageResult;
    }

    private static <T> void processPage(IPage<T> tPage, PageResult pageResult) {
        long pageNum = tPage.getCurrent();
        long pageSize = tPage.getSize();
        long totalNum = new Long(tPage.getTotal()).intValue();
        long totalPage = tPage.getPages();
        pageResult.setPageNum((int) pageNum);
        pageResult.setTotalNum((int) totalNum);
        pageResult.setTotalPage((int) totalPage);
        pageResult.setPageSize((int) pageSize);
    }

}
