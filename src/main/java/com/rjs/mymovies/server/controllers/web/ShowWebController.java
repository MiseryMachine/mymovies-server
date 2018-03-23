package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.controllers.ShowController;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.form.show.ShowSearch;
import com.rjs.mymovies.server.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@SessionAttributes("showSearchFilter")
@RequestMapping("/shows")
public class ShowWebController extends ShowController {
    @Autowired
    public ShowWebController(ShowService showService) {
        super(showService);
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
}
