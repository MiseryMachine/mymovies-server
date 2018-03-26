package com.rjs.mymovies.server.controllers.rest;

import com.rjs.mymovies.server.controllers.AdminController;
import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.form.mdb.MdbSearchForm;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ws/admin")
public class AdminRestController extends AdminController {
    public AdminRestController() {
    }

    @GetMapping("/edit-show/{showId}")
    public Map<String, Object> getShowEdit(@PathVariable("showId") Long showId) {
        Map<String, Object> result = new HashMap<>();
        Show show = showService.get(showId);

        result.put("show", show);
        result.put("showForm", buildShowForm(show.getShowType()));

        return result;
    }

    @PostMapping("/edit-show")
    public Show saveShow(@RequestBody Show show) {
        if (show.getContentsArray() != null && show.getContentsArray().length > 0) {
            show.setContents(Arrays.stream(show.getContentsArray()).collect(Collectors.joining(DataConstants.VALUE_DELIMITER)));
        }
        else {
            show.setContents(null);
        }

        return showService.save(show);
    }

    @GetMapping("/mdb/search-mdb")
    public Map<String, Object> getMdbSearch() {
        Map<String, Object> result = new HashMap<>();
        MdbSearchForm searchForm = new MdbSearchForm();
        searchForm.setShowType(DataConstants.SHOW_TYPES[0]);

        result.put("searchForm", searchForm);
        result.put("showTypes", DataConstants.SHOW_TYPES);

        return result;
    }

    @PostMapping("/mdb/search-mdb")
    public List<MdbShow> searchMdb(@RequestBody MdbSearchForm mdbSearchForm) {
        List<MdbShow> shows = mdbService.searchShows(mdbSearchForm.getShowType(), mdbSearchForm.getTitle());

        return shows != null ? shows : new ArrayList<>();
    }

    @GetMapping("/mdb/add-show/{showType}/{mdbId}")
    public Show addMdbShow(@PathVariable("showType") String showType, @PathVariable("mdbId") String mdbId) {
        return mdbService.addShow(showType, mdbId);
    }
}
