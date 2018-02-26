package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.form.PageWrapper;
import com.rjs.mymovies.server.model.form.show.ShowSearch;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shows")
public class ShowWebController {
    @Autowired
    private ShowTypeService showTypeService;
    private ShowService showService;

    @Autowired
    public ShowWebController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/search")
    public ModelAndView searchShowForm() {
        ModelAndView mav = new ModelAndView("/shows/search");

        mav.getModel().putAll(buildInitialModel());

        return mav;
    }

    @PostMapping("/search")
    public ModelAndView searchShows(@ModelAttribute ShowSearch showSearch, Pageable pageable) {
        ShowType showType = showSearch.getShowType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", showSearch.getTitle());
        paramMap.put("rating", showSearch.getRating());
        paramMap.put("mediaFormat", showSearch.getFormat());
        paramMap.put("genres", showSearch.getGenres());

        if (pageable == null) {
            pageable = new PageRequest(0, 20);
        }

        Page<Show> results = showService.searchShowsPageable(pageable, showType.getName(), paramMap);
//        List<Show> results = showService.searchShows(showType.getName(), paramMap);
        ModelAndView mav = new ModelAndView("/shows/search");

        mav.getModel().putAll(buildSearchModel(showSearch, results));

        return mav;
    }

    @GetMapping(value = "/poster-thumb/{showId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getPosterImage(@PathVariable Long showId) {
        return showService.getShowPosterData(showId, true);
    }

    private Map<String, Object> buildInitialModel() {
        ShowSearch showSearch = new ShowSearch();

        showSearch.setShowType(showTypeService.get("Movie"));
        showSearch.setFormat(DataConstants.MEDIA_FORMATS[0]);
        showSearch.setRating(DataConstants.STAR_RATINGS[DataConstants.STAR_RATINGS.length - 1]);
        showSearch.setTitle("");

        return buildSearchModel(showSearch, null);
    }

    private Map<String, Object> buildSearchModel(ShowSearch showSearch, Page<Show> searchResults) {
        Map<String, Object> model = new HashMap<>();

        model.put("showTypes", showTypeService.getAll());
        model.put("ratings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("searchParams", showSearch);
//        model.put("searchResults", searchResults);
        model.put("searchResults", new PageWrapper<>(searchResults));

        return model;
    }
}
