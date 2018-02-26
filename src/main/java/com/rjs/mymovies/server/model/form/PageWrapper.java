package com.rjs.mymovies.server.model.form;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageWrapper<E> {
    private static final int VIEWABLE_PAGES = 5;
    private static final int PAGES_MIDPOINT = 2;

    private Page<E> page;
    private int[] pageNumbers = new int[0];

    public PageWrapper(Page<E> page) {
        this.page = page;

        if (page != null) {
            int viewablePages = Math.min(VIEWABLE_PAGES, page.getTotalPages());
            pageNumbers = new int[viewablePages];
            int firstPage = 1;

            if (viewablePages == VIEWABLE_PAGES) {
                if (page.getNumber() > PAGES_MIDPOINT || page.getNumber() < (page.getTotalPages() - PAGES_MIDPOINT - 1)) {
                    firstPage = page.getNumber() - (PAGES_MIDPOINT);
                }
                else if (page.getNumber() <= PAGES_MIDPOINT) {
                    firstPage = 1;
                }
                else {
                    firstPage = page.getTotalPages() - viewablePages;
                }
            }

            for (int i = 0; i < pageNumbers.length; i++) {
                pageNumbers[i] = firstPage++;
            }
        }
    }

    public List<E> getContent() {
        return page != null ? page.getContent() : new ArrayList<>();
    }

    public boolean isFirst() {
        return page == null || page.isFirst();
    }

    public boolean isLast() {
        return page == null || page.isLast();
    }

    public int getCurrentPage() {
        return page != null ? page.getNumber() : 0;
    }

    public int[] getPageNumbers() {
        return pageNumbers;
    }
}
