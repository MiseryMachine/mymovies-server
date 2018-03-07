package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.form.mdb.MdbSearchForm;
import com.rjs.mymovies.server.model.form.show.ShowForm;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import com.rjs.mymovies.server.service.mdb.MdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("show")
@RequestMapping("/admin")
public class AdminWebController {
    @Autowired
    private MdbService mdbService;
    @Autowired
    private ShowService showService;
    @Autowired
    private ShowTypeService showTypeService;

    public AdminWebController() {
    }

//    @GetMapping("/adminUsers")
    public String userAdmin() {
        return "adminUsers";
    }

    @GetMapping("/edit-show/{showId}")
    public ModelAndView getShowToEdit(@PathVariable Long showId) {
        Show show = showService.get(showId);
        ModelAndView mav = new ModelAndView("/admin/edit-show");

        mav.getModel().put("showForm", buildShowForm(show.getShowType()));
        mav.getModel().put("show", show);

        return mav;
    }

    @PostMapping(value="/edit-show", params = {"action=save"})
    public ModelAndView saveShowEdit(@ModelAttribute("show") Show show, WebRequest webRequest, SessionStatus sessionStatus) {
        if (show.getContentsArray() != null && show.getContentsArray().length > 0) {
            show.setContents(Arrays.stream(show.getContentsArray()).collect(Collectors.joining(DataConstants.VALUE_DELIMITER)));
        }
        else {
            show.setContents(null);
        }

        showService.save(show);

        sessionStatus.setComplete();
        webRequest.removeAttribute("show", WebRequest.SCOPE_SESSION);

        return new ModelAndView("redirect://shows/search");
    }

    @PostMapping(value="/edit-show", params = {"action=cancel"})
    public ModelAndView cancelShowEdit(WebRequest webRequest, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        webRequest.removeAttribute("show", WebRequest.SCOPE_SESSION);

        return new ModelAndView("redirect://shows/search");
    }

    @GetMapping("/mdb/search-mdb")
    public ModelAndView getSearchMdb() {
        ModelAndView mav = new ModelAndView("/admin/mdb/search-mdb");
        MdbSearchForm searchForm = new MdbSearchForm();
        searchForm.setShowType(DataConstants.SHOW_TYPES[0]);

        mav.getModel().put("searchForm", searchForm);
        mav.getModel().put("showTypes", DataConstants.SHOW_TYPES);

        return mav;
    }

    @PostMapping("/mdb/search-mdb")
    public ModelAndView postSearchMdb(@ModelAttribute("searchForm") @Valid MdbSearchForm searchForm, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView("/admin/mdb/search-mdb");
        mav.getModel().put("searchForm", searchForm);
        mav.getModel().put("showTypes", DataConstants.SHOW_TYPES);

        if (!bindingResult.hasErrors()) {
            Iterable<MdbShow> mdbShows = mdbService.searchShows(searchForm.getShowType(), searchForm.getTitle());

            mav.getModel().put("mdbShows", mdbShows);
        }

        return mav;
    }

    @PostMapping("/mdb/add-show/{mdbId}")
    public ModelAndView addMdbShow(@PathVariable String mdbId, @ModelAttribute("searchForm") MdbSearchForm searchForm) {
        Show show = mdbService.addShow(searchForm.getShowType(), mdbId);
        ModelAndView mav = new ModelAndView("/admin/edit-show");

        mav.getModel().put("showForm", buildShowForm(show.getShowType()));
        mav.getModel().put("show", show);

        return mav;
    }

    private ShowForm buildShowForm(String showTypeValue) {
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
