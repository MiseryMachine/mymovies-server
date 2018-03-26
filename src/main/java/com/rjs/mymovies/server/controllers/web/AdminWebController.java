package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.controllers.AdminController;
import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.form.mdb.MdbSearchForm;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("show")
@RequestMapping("/admin")
public class AdminWebController extends AdminController {
    public AdminWebController() {
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
        String ctxPath = webRequest.getContextPath();
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
            List<MdbShow> mdbShows = mdbService.searchShows(searchForm.getShowType(), searchForm.getTitle());

            mav.getModel().put("mdbShows", mdbShows);
        }

        return mav;
    }

//    @PostMapping("/mdb/add-show/{mdbId}")
    @GetMapping("/mdb/add-show/{showType}/{mdbId}")
    public ModelAndView addMdbShow(@PathVariable String showType, @PathVariable String mdbId) {
        Show show = mdbService.addShow(showType, mdbId);
        ModelAndView mav = new ModelAndView("/admin/edit-show");

        mav.getModel().put("showForm", buildShowForm(show.getShowType()));
        mav.getModel().put("show", show);

        return mav;
    }
}
