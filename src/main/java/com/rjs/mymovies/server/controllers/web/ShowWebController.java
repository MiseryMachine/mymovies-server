package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.dto.ShowDto;
import com.rjs.mymovies.server.model.form.show.ShowSearch;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("showSearchFilter")
@RequestMapping("/shows")
public class ShowWebController {
    private static final Sort DEFAULT_SORT = new Sort(
        new Sort.Order(Sort.Direction.DESC, "starRating"),
        new Sort.Order(Sort.Direction.ASC, "title"));

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SimpleDateFormat dateFormat;
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
    public @ResponseBody byte[] getPosterThumb(@PathVariable Long showId) {
        return showService.getShowPosterData(showId, true);
    }

    @GetMapping(value = "/poster/{showId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getPosterImage(@PathVariable Long showId) {
        return showService.getShowPosterData(showId, false);
    }

    @GetMapping(value = "/details/{showId}")
    public String getShowDetails(@PathVariable Long showId, ModelMap modelMap) {
        Show show = showService.get(showId);

        if (show != null && show.getReleaseDate() != null) {
            show.setReleaseDateText(dateFormat.format(show.getReleaseDate()));
        }

        modelMap.addAttribute("showDetails", show);

        return "shows/showModal :: modalContents";
    }

    @ModelAttribute("showSearchFilter")
    public ShowSearch setupShowSearchFilter() {
        return new ShowSearch();
    }

    private Map<String, Object> buildInitialModel() {
        ShowSearch showSearch = new ShowSearch();

        showSearch.setShowType(showTypeService.get("Movie"));
        showSearch.setFormat(DataConstants.MEDIA_FORMATS[0]);
        showSearch.setStarRating(DataConstants.STAR_RATINGS[DataConstants.STAR_RATINGS.length - 1]);
        showSearch.setTitle("");

        Map<String, Object> model = new HashMap<>();

        model.put("showTypes", showTypeService.getAll());
        model.put("starRatings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("showSearchFilter", showSearch);
        model.put("shows", null);

        return model;
    }

    private List<Show> getShowData(ShowSearch showSearch) {
        ShowType showType = showSearch.getShowType();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", showSearch.getTitle());
        paramMap.put("starRating", showSearch.getStarRating());
        paramMap.put("mediaFormat", showSearch.getFormat());
        paramMap.put("genres", showSearch.getGenres());

        return showService.searchShows(showType.getName(), paramMap, DEFAULT_SORT);
    }

    private Map<String, Object> buildSearchModel(ShowSearch showSearch, List<Show> searchResults) {
        Map<String, Object> model = new HashMap<>();
        List<ShowDto> showDtos = null;

        if (searchResults != null && !searchResults.isEmpty()) {
            showDtos = searchResults.stream().map(s -> convertToShowDto(s)).collect(Collectors.toList());
        }

        model.put("showTypes", showTypeService.getAll());
        model.put("starRatings", DataConstants.STAR_RATINGS);
        model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
        model.put("showSearchFilter", showSearch);
        model.put("shows", showDtos);

        return model;
    }

    private ShowDto convertToShowDto(Show show) {
        ShowDto showDto = modelMapper.map(show, ShowDto.class);

        return showDto;
    }
}
