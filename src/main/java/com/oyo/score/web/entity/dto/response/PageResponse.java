package com.oyo.score.web.entity.dto.response;

import java.util.List;

public class PageResponse<T> {
    private List<T> data;
    private Integer currentPage;
    private Integer totalPage;
    private Long totalElements;

    public PageResponse() {
    }

    public PageResponse(List<T> data, Integer currentPage, Integer totalPage, Long totalElements) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        this.totalElements = totalElements;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
