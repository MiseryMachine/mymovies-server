package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.form.show.ShowSearch;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public ModelAndView searchShows(@ModelAttribute ShowSearch showSearch) {
        ShowType showType = showSearch.getShowType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", showSearch.getTitle());
        paramMap.put("rating", showSearch.getRating());
        paramMap.put("mediaFormat", showSearch.getFormat());
        paramMap.put("genres", showSearch.getGenres());

        List<Show> results = showService.searchShows(showType.getName(), paramMap);
        ModelAndView mav = new ModelAndView("/shows/search");

        mav.getModel().putAll(buildSearchModel(showSearch, results));

        return mav;
    }

    private Map<String, Object> buildInitialModel() {
        List<ShowType> showTypes = showTypeService.getAll();
        ShowSearch showSearch = new ShowSearch();

        showSearch.setShowType(showTypes.get(0));
        showSearch.setFormat(DataConstants.MEDIA_FORMATS[0]);
        showSearch.setRating(DataConstants.STAR_RATINGS[DataConstants.STAR_RATINGS.length - 1]);
        showSearch.setTitle("");

        Map<String, Object> model = new HashMap<>();

        model.put("showTypes", showTypes);
        model.put("ratings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("searchParams", showSearch);
        model.put("searchResults", new ArrayList<>());

        return model;
    }

    private Map<String, Object> buildSearchModel(ShowSearch showSearch, List<Show> searchResults) {
        Map<String, Object> model = new HashMap<>();

        model.put("showTypes", showTypeService.getAll());
        model.put("ratings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("searchParams", showSearch);
        model.put("searchResults", searchResults);

        return model;
    }
}
