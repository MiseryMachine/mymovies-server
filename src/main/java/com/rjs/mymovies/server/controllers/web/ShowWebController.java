package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.form.show.ShowSearch;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("showSearchFilter")
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
    public ModelAndView searchShowForm(@ModelAttribute("showSearchFilter") ShowSearch showSearch) {
        ModelAndView mav = new ModelAndView("/shows/search");

        if (showSearch.getShowType() == null) {
            mav.getModel().putAll(buildInitialModel());
        }
        else {
            List<Show> results = getShowData(showSearch);
            mav.getModel().putAll(buildSearchModel(showSearch, results));
        }


        return mav;
    }

    @PostMapping("/search")
    public ModelAndView searchShows(@ModelAttribute("showSearchFilter") ShowSearch showSearch) {
        List<Show> results = getShowData(showSearch);
        ModelAndView mav = new ModelAndView("/shows/search");

        mav.getModel().putAll(buildSearchModel(showSearch, results));

        return mav;
    }

    @GetMapping(value = "/poster-thumb/{showId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getPosterImage(@PathVariable Long showId) {
        return showService.getShowPosterData(showId, true);
    }

    @ModelAttribute("showSearchFilter")
    public ShowSearch setupShowSearchFilter() {
        return new ShowSearch();
    }

    private Map<String, Object> buildInitialModel() {
        ShowSearch showSearch = new ShowSearch();

        showSearch.setShowType(showTypeService.get("Movie"));
        showSearch.setFormat(DataConstants.MEDIA_FORMATS[0]);
        showSearch.setRating(DataConstants.STAR_RATINGS[DataConstants.STAR_RATINGS.length - 1]);
        showSearch.setTitle("");

        Map<String, Object> model = new HashMap<>();

        model.put("showTypes", showTypeService.getAll());
        model.put("ratings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("showSearchFilter", showSearch);
        model.put("shows", null);

        return model;
    }

    private List<Show> getShowData(ShowSearch showSearch) {
        ShowType showType = showSearch.getShowType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", showSearch.getTitle());
        paramMap.put("rating", showSearch.getRating());
        paramMap.put("mediaFormat", showSearch.getFormat());
        paramMap.put("genres", showSearch.getGenres());

        return showService.searchShows(showType.getName(), paramMap);
    }

    private Map<String, Object> buildSearchModel(ShowSearch showSearch, List<Show> searchResults) {
        Map<String, Object> model = new HashMap<>();

        model.put("showTypes", showTypeService.getAll());
        model.put("ratings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("showSearchFilter", showSearch);
        model.put("shows", searchResults);

        return model;
    }
}
