package com.rjs.mymovies.server.model.form.show;

import com.rjs.mymovies.server.model.ShowType;

import java.util.LinkedHashSet;
import java.util.Set;

public class ShowSearch {
    private ShowType showType = null;
    private String title = "";
    private int rating = 0;
    private String format = "";
//    private List<String> genres = new ArrayList<>();
    private Set<String> genres = new LinkedHashSet<>();
//    private String[] genres = new String[0];

    public ShowSearch() {
    }

    public ShowType getShowType() {
        return showType;
    }

    public void setShowType(ShowType showType) {
        this.showType = showType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

/*
    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
*/

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
/*
    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }
*/
}
