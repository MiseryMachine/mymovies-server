package com.rjs.mymovies.server.model.form.show;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.ShowType;

import java.util.ArrayList;
import java.util.List;

public class ShowForm {
    private List<ShowType> showTypes = new ArrayList<>();
    private List<String> allGenres = new ArrayList<>();
    private String[] mediaFormats = DataConstants.MEDIA_FORMATS;
    private int[] starRatings = DataConstants.STAR_RATINGS;
    private String[] showRatings = new String[0];
    private String[] contents = new String[0];

    public ShowForm() {
    }

    public List<ShowType> getShowTypes() {
        return showTypes;
    }

    public void setShowTypes(List<ShowType> showTypes) {
        this.showTypes = showTypes;
    }

    public List<String> getAllGenres() {
        return allGenres;
    }

    public void setAllGenres(List<String> allGenres) {
        this.allGenres = allGenres;
    }

    public String[] getMediaFormats() {
        return mediaFormats;
    }

    public void setMediaFormats(String[] mediaFormats) {
        this.mediaFormats = mediaFormats;
    }

    public int[] getStarRatings() {
        return starRatings;
    }

    public void setStarRatings(int[] starRatings) {
        this.starRatings = starRatings;
    }

    public String[] getShowRatings() {
        return showRatings;
    }

    public void setShowRatings(String[] showRatings) {
        this.showRatings = showRatings;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }
}
