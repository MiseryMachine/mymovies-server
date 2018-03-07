package com.rjs.mymovies.server.model.form.mdb;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MdbSearchForm {
    @NotNull(message = "Show type must be selected.")
    private String showType;
    @NotNull(message = "Title must contain at least 3 characters.")
    @Size(min = 3, message = "Title must contain at least 3 characters.")
    private String title;

    public MdbSearchForm() {
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
