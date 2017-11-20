package com.rjs.mymovies.server.controllers;

import com.rjs.mymovies.server.model.Genre;
import com.rjs.mymovies.server.model.Medium;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import com.rjs.mymovies.server.repos.GenreRepository;
import com.rjs.mymovies.server.repos.MDBRepository;
import com.rjs.mymovies.server.repos.MediaFormatRepository;
import com.rjs.mymovies.server.repos.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 14:42<br>
 */
@RestController
@RequestMapping(path = "/shows")
public class ShowController {
	@Autowired
	private ShowRepository showRepository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private MDBRepository mdbRepository;
	@Autowired
	private MediaFormatRepository mediaFormatRepository;

	public ShowController() {
	}

	@GetMapping("/genres")
	public EnumMap<Medium, List<Genre>> getGenres() {
		EnumMap<Medium, List<Genre>> genreMap = new EnumMap<>(Medium.class);

		for (Medium medium : Medium.values()) {
			List<Genre> genres = genreRepository.findByMedium(medium);

			genreMap.put(medium, genres);
		}

		return genreMap;
	}

	@PostMapping("/search/{medium}")
	public Iterable<Show> searchMyShows(@PathVariable Medium medium, @RequestBody Map<String, Object> params) {
		List<Genre> genres = (List<Genre>) params.get("genres");
		String title = (String) params.get("title");

		if (genres == null || genres.isEmpty()) {
			return new ArrayList<>();
		}

		return StringUtils.isEmpty(title) ? showRepository.findByGenresIn(genres) : showRepository.findByGenresInAndTitleLike(genres, title);
	}

	@GetMapping("/add/{medium}/{mdbId}")
	public Show addMovieFromMdb(@PathVariable Medium medium, @PathVariable String mdbId) {
		return mdbRepository.addShow(medium, mdbId);
	}

	@GetMapping("/deleteAll")
	public void removeAllShows() {
		showRepository.deleteAll();
	}

	@GetMapping(value = "/mdb/search/{medium}/{title}")
	public Iterable<MdbShow> searchMdbShows(@PathVariable Medium medium, @PathVariable String title) {
		return mdbRepository.searchShows(medium, title);
	}

	@GetMapping(value = "/mdb/genres/refresh/{medium}")
	public Iterable<Genre> refreshGenres(@PathVariable Medium medium) {
		return mdbRepository.getGenres(medium);
	}
}
