package com.rjs.mymovies.server.controllers;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 14:42<br>
 */
public abstract class ShowController {
	private static final Sort DEFAULT_SORT = new Sort(
		new Sort.Order(Sort.Direction.DESC, "starRating"),
		new Sort.Order(Sort.Direction.ASC, "title"));

	protected ShowService showService;
	@Autowired
	protected ShowTypeService showTypeService;
	@Autowired
	protected ModelMapper modelMapper;
	@Autowired
	protected SimpleDateFormat dateFormat;

	@Autowired
	protected ShowController(ShowService showService) {
		this.showService = showService;
	}

	protected Map<String, Object> buildInitialModel() {
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
		model.put("numResults", 0);
		model.put("shows", new ArrayList<>());

		return model;
	}

	protected List<Show> getShowData(ShowSearch showSearch) {
		ShowType showType = showSearch.getShowType();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("title", showSearch.getTitle());
		paramMap.put("starRating", showSearch.getStarRating());
		paramMap.put("mediaFormat", showSearch.getFormat());
		paramMap.put("genres", showSearch.getGenres());

		return showService.searchShows(showType.getName(), paramMap, DEFAULT_SORT);
	}

	protected Map<String, Object> buildSearchModel(ShowSearch showSearch, List<Show> searchResults) {
		Map<String, Object> model = new HashMap<>();
		List<ShowDto> showDtos;

		if (searchResults != null && !searchResults.isEmpty()) {
			showDtos = searchResults.stream().map(this::convertToShowDto).collect(Collectors.toList());
		}
		else {
			showDtos = new ArrayList<>();
		}

		model.put("showTypes", showTypeService.getAll());
		model.put("starRatings", DataConstants.STAR_RATINGS);
		model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
		model.put("showSearchFilter", showSearch);
		model.put("numResults", showDtos.size());
		model.put("shows", showDtos);

		return model;
	}

	protected ShowDto convertToShowDto(Show show) {
		return modelMapper.map(show, ShowDto.class);
	}
}
