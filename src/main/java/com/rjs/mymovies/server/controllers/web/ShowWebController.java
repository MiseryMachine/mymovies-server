package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.config.security.UserPrincipal;
import com.rjs.mymovies.server.controllers.ShowController;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.dto.ShowFilterDto;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.UserShowFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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
    private UserShowFilterService userShowFilterService;

    @Autowired
    public ShowWebController(ShowService showService) {
        super(showService);
    }


    @GetMapping("/search")
    public ModelAndView searchShowForm(@ModelAttribute("showSearchFilter")ShowFilterDto showFilterDto, Authentication authentication) {
        ModelAndView mav = new ModelAndView("/shows/search");
        List<Show> results = null;

/*
        if (!StringUtils.isEmpty(showFilterDto.getShowTypeName())) {
            results = getShowData(showFilterDto);
        }
*/

        mav.getModel().putAll(buildSearchModel(showFilterDto, results));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        mav.getModel().put("userFilters", principal.getUser().getUserShowFilters());

        return mav;
    }

    @PostMapping("/search")
    public ModelAndView searchShows(@ModelAttribute("showSearchFilter") ShowFilterDto showFilterDto) {
        List<Show> results = getShowData(showFilterDto);
        ModelAndView mav = new ModelAndView("/shows/search");

        mav.getModel().putAll(buildSearchModel(showFilterDto, results));

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
    public ShowFilterDto setupShowSearchFilter() {
        return initializeShowFilter();
    }
}
