package com.rjs.mymovies.server.controllers;

import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.form.PageWrapper;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import com.rjs.mymovies.server.repos.MDBRepository;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 14:42<br>
 */
@RestController
@RequestMapping(path = "/rest/shows")
public class ShowController {
	@Autowired
	private ShowService showService;
	@Autowired
	private ShowTypeService showTypeService;
	@Autowired
	private MDBRepository mdbRepository;

	public ShowController() {
	}

	@GetMapping("/genres")
	public Map<String, Set<String>> getGenres() {
		return showTypeService.getAll().stream().collect(Collectors.toMap(ShowType::getName, ShowType::getGenres));
	}

	@PostMapping("/search/{showTypeName}")
	public Iterable<Show> searchMyShows(@PathVariable String showTypeName, @RequestBody Map<String, Object> params) {
		return showService.searchShows(showTypeName, params);
	}

	@PostMapping("/r-page/{showTypeName}")
	public PageWrapper<Show> searchMyShows(@PathVariable String showTypeName) {
//		return showService.searchShowsPageable(new PageRequest(0, 20), showTypeName, new HashMap<>());
		return new PageWrapper<>(showService.searchShowsPageable(new PageRequest(0, 20), showTypeName, new HashMap<>()));
	}

	@GetMapping("/add/{showTypeName}/{mdbId}")
	public Show addMovieFromMdb(@PathVariable String showTypeName, @PathVariable String mdbId) {
		return mdbRepository.addShow(showTypeName, mdbId);
	}

	@GetMapping("/deleteAll")
	public void removeAllShows() {
		showService.deleteAll();
	}

	@GetMapping(value = "/mdb/search/{showTypeName}/{title}")
	public Iterable<MdbShow> searchMdbShows(@PathVariable String showTypeName, @PathVariable String title) {
		return mdbRepository.searchShows(showTypeName, title);
	}

	@GetMapping(value = "/mdb/genres/refresh/{showTypeName}")
	public Iterable<String> refreshGenres(@PathVariable String showTypeName) {
		return mdbRepository.getGenres(showTypeName);
	}
}
