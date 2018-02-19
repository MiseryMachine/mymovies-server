package com.rjs.mymovies.server.repos;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageSortBuilder {
    private Sort.Direction direction;
    private String field;
    private int pageNumber = 0;
    private int pageSize = 10;

    public PageSortBuilder(String field) {
        this.field = field;
    }

    public PageSortBuilder sortDirection(Sort.Direction direction) {
        this.direction = direction;

        return this;
    }

    public PageSortBuilder pageNumber(int pageNumber) {
        this.pageNumber = pageNumber;

        return this;
    }

    public PageSortBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    public Sort getSort() {
        return new Sort(direction, field);
    }

    public PageRequest getPageRequest() {
        return new PageRequest(pageNumber, pageSize, new Sort(direction, field));
    }
}
