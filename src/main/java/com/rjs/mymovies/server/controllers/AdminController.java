package com.rjs.mymovies.server.controllers;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.form.show.ShowForm;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import com.rjs.mymovies.server.service.mdb.MdbService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public abstract class AdminController {
    @Autowired
    protected MdbService mdbService;
    @Autowired
    protected ShowService showService;
    @Autowired
    protected ShowTypeService showTypeService;

    protected AdminController() {
    }

    protected ShowForm buildShowForm(String showTypeValue) {
        ShowForm showForm = new ShowForm();
        ShowType showType;

        showForm.setShowTypes(showTypeService.getAll());

        if (showTypeValue.equals("Movie")) {
            showForm.setShowRatings(DataConstants.MOVIE_RATINGS);
            showForm.setContents(DataConstants.MOVIE_RATING_COMPONENTS);
            showType = showTypeService.get("Movie");
        }
        else {
            showForm.setShowRatings(DataConstants.TV_RATINGS);
            showForm.setContents(DataConstants.TV_RATING_COMPONENTS);
            showType = showTypeService.get("TV");
        }

        if (showType != null && showType.getGenres() != null) {
            showForm.getAllGenres().addAll(showType.getGenres());
            Collections.sort(showForm.getAllGenres());
        }

        return showForm;
    }
}
