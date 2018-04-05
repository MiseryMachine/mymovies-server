package com.rjs.mymovies.server.model.dto;

import java.util.LinkedHashSet;
import java.util.Set;

public class ShowFilterDto {
    private String showTypeName = "";
    private String title = "";
    private int starRating = 0;
    private String format = "";
    private Set<String> genres = new LinkedHashSet<>();

    public ShowFilterDto() {
    }

    public String getShowTypeName() {
        return showTypeName;
    }

    public void setShowTypeName(String showTypeName) {
        this.showTypeName = showTypeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
