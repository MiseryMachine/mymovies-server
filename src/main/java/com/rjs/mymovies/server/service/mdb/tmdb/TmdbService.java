package com.rjs.mymovies.server.service.mdb.tmdb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.mymovies.server.config.AppConfig;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.model.mdb.MdbGenre;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import com.rjs.mymovies.server.model.mdb.MdbShowDetail;
import com.rjs.mymovies.server.model.mdb.MdbShowListing;
import com.rjs.mymovies.server.service.ShowService;
import com.rjs.mymovies.server.service.ShowTypeService;
import com.rjs.mymovies.server.service.mdb.MdbService;
import com.rjs.mymovies.server.util.ImageUtil;
import com.rjs.mymovies.server.util.web.RestClient;
import com.rjs.mymovies.server.util.web.WebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 13:03<br>
 */
@Service("tmdbService")
public class TmdbService implements MdbService {
	private static final Logger LOGGER = Logger.getLogger(TmdbService.class.getName());

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private TmdbConfig tmdbConfig;
	@Autowired
	private SimpleDateFormat dateFormat;
	@Autowired
	private ObjectMapper jsonObjectMapper;
	@Autowired
	private ShowService showService;
	@Autowired
	private ShowTypeService showTypeService;

	public TmdbService() {
	}

	@Override
	public Iterable<MdbShow> searchShows(String showTypeName, String title) {
		List<MdbShow> results = new ArrayList<>();
		String mediumPath = "TV".equalsIgnoreCase(showTypeName) ? tmdbConfig.getTvPath() : tmdbConfig.getMoviePath();
		List<String> urlParamsList = new ArrayList<>();
		urlParamsList.add("api_key=" + tmdbConfig.getKey());
		urlParamsList.add("query=" + title);
		urlParamsList.add("language=" + tmdbConfig.getLocale());

		try {
			String url = new TmdbUrl(tmdbConfig.getUrl()).addPath(tmdbConfig.getSearchPath()).addPath(mediumPath).getUrl() +
					"?" + urlParamsList.stream().collect(Collectors.joining("&"));
			ResponseEntity<MdbShowListing> responseEntity = RestClient.exchange(HttpMethod.GET, url, null,
				null, "", new ParameterizedTypeReference<MdbShowListing>() {
				}, null);

			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new WebServiceException("Response status code = " + responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getStatusCode());
			}

			MdbShowListing showListing = responseEntity.getBody();

			if (showListing != null && showListing.results != null) {
				results.addAll(showListing.results);
				results.forEach(e -> {
					e.posterPath = new TmdbUrl(tmdbConfig.getImageUrl()).addPath(tmdbConfig.getImageThumbPath()).getUrl() + e.posterPath;
					if (StringUtils.isEmpty(e.releaseDate)) {
						e.releaseDate = "Unknown";
					}
				});
			}
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error", e);
		}

		return results;
	}

	@Override
	public Show addShow(String showTypeName, String mdbId) {
		try {
			Show show = showService.findByMdbId(mdbId);

			if (show != null) {
				return show;
			}

			String mediumPath = "TV".equalsIgnoreCase(showTypeName) ? tmdbConfig.getTvPath() : tmdbConfig.getMoviePath();
			List<String> urlParamsList = new ArrayList<>();
			urlParamsList.add("api_key=" + tmdbConfig.getKey());
			urlParamsList.add("language=" + tmdbConfig.getLocale());

			String url = new TmdbUrl(tmdbConfig.getUrl())
					.addPath(mediumPath)
					.addPath("/" + mdbId)
					.getUrl();
			url += "?" + urlParamsList.stream().collect(Collectors.joining("&"));

			ResponseEntity<MdbShowDetail> responseEntity = RestClient.exchange(HttpMethod.GET, url, null,
				null, "", new ParameterizedTypeReference<MdbShowDetail>() {
				}, null);

			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new WebServiceException("Response status code = " + responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getStatusCode());
			}

			MdbShowDetail tmdbMovie = responseEntity.getBody();

			if (tmdbMovie != null) {
				show = new Show();
				show.setShowType(showTypeName);
				show.setMdbId(String.valueOf(tmdbMovie.id));
				show.setImdbId(tmdbMovie.imdbId);
				show.setTitle(tmdbMovie.title);
				show.setTagLine(tmdbMovie.tagline);
				show.setDescription(tmdbMovie.overview);
				show.setGenres(convertGenres(tmdbMovie.genres, showTypeName));
				show.setRuntime(tmdbMovie.runtime);
//				show.setImageUrl(tmdbConfig.getImageUrl() + tmdbConfig.getImageNormalPath() + tmdbMovie.posterPath);

				if (!StringUtils.isBlank(tmdbMovie.releaseDate)) {
					try {
						show.setReleaseDate(dateFormat.parse(tmdbMovie.releaseDate));
					}
					catch (ParseException e) {
						LOGGER.log(Level.SEVERE, "Error parsing show date value: " + tmdbMovie.releaseDate, e);
					}
				}

				show = showService.save(show);

				// Download and save poster image locally
				downlaodPosterImage(show, tmdbMovie.posterPath);

				return show;
			}
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error", e);
		}

		return null;
	}

	@Override
	public Set<String> getGenres(String showTypeName) {
		String mediumPath = (StringUtils.isBlank(showTypeName) || "MOVIE".equalsIgnoreCase(showTypeName)) ? tmdbConfig.getMoviePath() : tmdbConfig.getTvPath();
		String url = new TmdbUrl(tmdbConfig.getUrl())
				.addPath(tmdbConfig.getGenrePath())
				.addPath(mediumPath)
				.addPath(tmdbConfig.getListPath())
				.getUrl();
		return getGenres(url, showTypeName);
	}

	private void downlaodPosterImage(Show show, String tmdbPosterPath) throws IOException {
		if (show.getId() == null) {
			throw new IllegalStateException("Show must be saved prior to adding poster image.");
		}

		URL posterUrl = new URL(tmdbConfig.getImageUrl() + tmdbConfig.getImageNormalPath() + tmdbPosterPath);
		ImageUtil.saveImage(posterUrl, appConfig.getLocalImagePath(String.valueOf(show.getId())), "poster");
	}

	private Set<String> getGenres(String serviceUrl, String showTypeName) {
		Map<String, MdbGenre> tmdbGenreMap = getTmdbGenreMap(serviceUrl);
		return convertGenres(tmdbGenreMap.values(), showTypeName);
	}

	private Map<String, MdbGenre> getTmdbGenreMap(String serviceUrl) {
		Map<String, MdbGenre> results = new LinkedHashMap<>();

		try {
			List<String> urlParamsList = new ArrayList<>();
			urlParamsList.add("api_key=" + tmdbConfig.getKey());
			urlParamsList.add("language=" + tmdbConfig.getLocale());

			String url = serviceUrl + "?" + urlParamsList.stream().collect(Collectors.joining("&"));
			ResponseEntity<LinkedHashMap<String, Object>> responseEntity = RestClient.exchangeMap(HttpMethod.GET, url, null, null, "", null);

			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new WebServiceException("Response status code = " + responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getStatusCode());
			}

			LinkedHashMap<String, Object> rootMap = responseEntity.getBody();

			if (rootMap != null) {
				String genreJson = jsonObjectMapper.writeValueAsString(rootMap.get("genres"));
				List<MdbGenre> mdbGenres = jsonObjectMapper.readValue(genreJson, new TypeReference<List<MdbGenre>>() {
				});

				mdbGenres.forEach(e -> results.put(String.valueOf(e.id), e));
			}
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error", e);
		}

		return results;
	}

	private Set<String> convertGenres(Collection<MdbGenre> mdbGenres, String showTypeName) {
		ShowType showType = showTypeService.get(showTypeName);

		if (showType == null) {
			showType = new ShowType();
			showType.setName(showTypeName);

			showType = showTypeService.save(showType);
		}

		Set<String> genres = new LinkedHashSet<>();
		Set<String> curGenres = showType.getGenres();

		if (mdbGenres != null) {
			boolean saveShowType = false;

			for (MdbGenre mdbGenre : mdbGenres) {
				genres.add(mdbGenre.name);

				if (!curGenres.contains(mdbGenre.name)) {
					curGenres.add(mdbGenre.name);

					saveShowType = true;
				}
			}

			if (saveShowType) {
				showType = showTypeService.save(showType);
			}
		}

		return genres;
	}

/*
	private String buildURL(String serviceUrl) throws IOException {
		return buildURL(serviceUrl, null, null);
	}

	private String buildURL(String serviceUrl, Map<String, String> urlParams) throws IOException {
		return buildURL(serviceUrl, null, urlParams);
	}

	private String buildURL(String serviceUrl, List<String> pathParams, Map<String, String> urlParams) throws IOException {
		String pathParamsUrl = "";

		if (pathParams != null) {
			for (String pathParam : pathParams) {
				pathParamsUrl += "/" + pathParam;
			}
		}

		String url = tmdbConfig.url + serviceUrl + pathParamsUrl + "?api_key=" + tmdbConfig.apiKey;

		if (urlParams != null) {
			for (String key : urlParams.keySet()) {
				url += "&" + key + "=" + urlParams.get(key);
			}
		}

		return url;
	}
*/
}
