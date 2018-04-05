package com.rjs.mymovies.server.controllers;

import com.rjs.mymovies.server.model.DataConstants;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.dto.ShowDto;
import com.rjs.mymovies.server.model.dto.ShowFilterDto;
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

/*
	protected Map<String, Object> buildInitialModel() {
		ShowFilterDto showFilterDto = new ShowFilterDto();

		showFilterDto.setShowTypeName("Movie");
		showFilterDto.setFormat(DataConstants.MEDIA_FORMATS[0]);
		showFilterDto.setStarRating(DataConstants.STAR_RATINGS[DataConstants.STAR_RATINGS.length - 1]);
		showFilterDto.setTitle("");

		Map<String, Object> model = new HashMap<>();

		model.put("showTypes", showTypeService.getAll());
//		model.put("showTypes", showTypeService.getAllAsMap());
		model.put("starRatings", DataConstants.STAR_RATINGS);
		model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
		model.put("showSearchFilter", showFilterDto);
		model.put("numResults", 0);
		model.put("shows", new ArrayList<>());

		return model;
	}
*/

	protected ShowFilterDto initializeShowFilter() {
		ShowFilterDto showFilterDto = new ShowFilterDto();

		showFilterDto.setShowTypeName("Movie");
		showFilterDto.setFormat(DataConstants.MEDIA_FORMATS[0]);
		showFilterDto.setStarRating(DataConstants.STAR_RATINGS[DataConstants.STAR_RATINGS.length - 1]);
		showFilterDto.setTitle("");

		return showFilterDto;
	}

	protected List<Show> getShowData(ShowFilterDto showFilterDto) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("title", showFilterDto.getTitle());
		paramMap.put("starRating", showFilterDto.getStarRating());
		paramMap.put("mediaFormat", showFilterDto.getFormat());
		paramMap.put("genres", showFilterDto.getGenres());

//		return showService.searchShows(showFilterDto.getShowTypeName(), paramMap, DEFAULT_SORT);
		return showService.searchShows(showFilterDto.getShowTypeName(), paramMap, DEFAULT_SORT);
	}

	protected Map<String, Object> buildSearchModel(ShowFilterDto showFilterDto, List<Show> searchResults) {
		Map<String, Object> model = new HashMap<>();
		List<ShowDto> showDtos;

		if (searchResults != null && !searchResults.isEmpty()) {
			showDtos = searchResults.stream().map(this::convertToShowDto).collect(Collectors.toList());
		}
		else {
			showDtos = new ArrayList<>();
		}

		model.put("showTypes", showTypeService.getAll());
//		model.put("showTypes", showTypeService.getAllAsMap());
		model.put("starRatings", DataConstants.STAR_RATINGS);
		model.put("mediaFormats", DataConstants.MEDIA_FORMATS);
		model.put("showSearchFilter", showFilterDto);
		model.put("numResults", showDtos.size());
		model.put("shows", showDtos);

		return model;
	}

	protected ShowDto convertToShowDto(Show show) {
		return modelMapper.map(show, ShowDto.class);
	}
}
