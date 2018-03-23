package com.rjs.mymovies.server.controllers.rest;

import com.rjs.mymovies.server.controllers.ShowController;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.dto.ShowDto;
import com.rjs.mymovies.server.model.form.show.ShowSearch;
import com.rjs.mymovies.server.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ws/shows")
public class ShowRestController extends ShowController {
    @Autowired
    public ShowRestController(ShowService showService) {
        super(showService);
    }

    @GetMapping("/search")
    public Map<String, Object> getShowSearchForm() {
        return buildInitialModel();
    }

    @PostMapping("/search")
    public List<ShowDto> searchShows(@RequestBody ShowSearch showSearch) {
        List<Show> shows = getShowData(showSearch);

        if (shows != null && !shows.isEmpty()) {
            return shows.stream().map(this::convertToShowDto).collect(Collectors.toList());
        }

        return new ArrayList<>();
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
    public Show getShowDetails(@PathVariable Long showId) {
        Show show = showService.get(showId);

        if (show != null && show.getReleaseDate() != null) {
            show.setReleaseDateText(dateFormat.format(show.getReleaseDate()));
        }

        return show;
    }
}
