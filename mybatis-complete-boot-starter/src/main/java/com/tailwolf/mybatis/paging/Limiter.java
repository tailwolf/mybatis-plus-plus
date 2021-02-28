package com.tailwolf.mybatis.paging;

import java.io.Serializable;

/**
 * 分页对象
 * @author tailwolf
 * @date 2020-03-26
 */
public class Limiter implements Serializable {
    private Integer currentPage;
    private Integer pageSize;

    public Limiter(){}

    public Limiter (Integer currentPage, Integer pageSize){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
