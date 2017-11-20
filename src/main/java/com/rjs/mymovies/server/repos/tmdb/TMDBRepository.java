package com.rjs.mymovies.server.repos.tmdb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjs.mymovies.server.model.Genre;
import com.rjs.mymovies.server.model.Medium;
import com.rjs.mymovies.server.model.Show;
import com.rjs.mymovies.server.model.mdb.MdbGenre;
import com.rjs.mymovies.server.model.mdb.MdbShow;
import com.rjs.mymovies.server.model.mdb.MdbShowDetail;
import com.rjs.mymovies.server.model.mdb.MdbShowListing;
import com.rjs.mymovies.server.repos.GenreRepository;
import com.rjs.mymovies.server.repos.MDBRepository;
import com.rjs.mymovies.server.repos.ShowRepository;
import com.rjs.mymovies.server.util.web.RestClient;
import com.rjs.mymovies.server.util.web.WebServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 13:03<br>
 */
@Service("mdbRepository")
public class TMDBRepository implements MDBRepository {
	private static final Logger LOGGER = Logger.getLogger(TMDBRepository.class.getName());

	@Autowired
	private TMDBConfig tmdbConfig;
	@Autowired
	private SimpleDateFormat dateFormat;
	@Autowired
	private ObjectMapper jsonObjectMapper;
	@Autowired
	private ShowRepository showRepository;
	@Autowired
	private GenreRepository genreRepository;

	public TMDBRepository() {
	}

	@Override
	public Iterable<MdbShow> searchShows(String title) {
		return searchShows(Medium.MOVIE, title);
	}

	@Override
	public Iterable<MdbShow> searchShows(Medium medium, String title) {
		List<MdbShow> results = new ArrayList<>();
		String mediumPath = medium == Medium.TV ? tmdbConfig.tvPath : tmdbConfig.moviePath;
		List<String> urlParamsList = new ArrayList<>();
		urlParamsList.add("api_key=" + tmdbConfig.apiKey);
		urlParamsList.add("query=" + title);
		urlParamsList.add("language=" + tmdbConfig.locale);

		try {
			String url = new TMDBUrl(tmdbConfig.url).addPath(tmdbConfig.searchPath).addPath(mediumPath).getUrl() +
					"?" + StringUtils.collectionToDelimitedString(urlParamsList, "&");
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
					e.posterPath = new TMDBUrl(tmdbConfig.imageUrl).addPath(tmdbConfig.imageThumbPath).getUrl() + e.posterPath;
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
	public Show addShow(Medium medium, String mdbId) {
		try {
			Show show = showRepository.findByMdbId(mdbId);

			if (show != null) {
				return show;
			}

			String mediumPath = medium == Medium.TV ? tmdbConfig.tvPath : tmdbConfig.moviePath;
			List<String> urlParamsList = new ArrayList<>();
			urlParamsList.add("api_key=" + tmdbConfig.apiKey);
			urlParamsList.add("language=" + tmdbConfig.locale);

			String url = new TMDBUrl(tmdbConfig.url)
					.addPath(mediumPath)
					.addPath("/" + mdbId)
					.getUrl();
			url += "?" + StringUtils.collectionToDelimitedString(urlParamsList, "&");

			ResponseEntity<MdbShowDetail> responseEntity = RestClient.exchange(HttpMethod.GET, url, null,
				null, "", new ParameterizedTypeReference<MdbShowDetail>() {
				}, null);

			if (responseEntity.getStatusCode() != HttpStatus.OK) {
				throw new WebServiceException("Response status code = " + responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getStatusCode());
			}

			MdbShowDetail tmdbMovie = responseEntity.getBody();

			if (tmdbMovie != null) {
				show = new Show();
				show.setMedium(medium);
				show.setMdbId(String.valueOf(tmdbMovie.id));
				show.setImdbId(tmdbMovie.imdbId);
				show.setTitle(tmdbMovie.title);
				show.setTagLine(tmdbMovie.tagline);
				show.setDescription(tmdbMovie.overview);
				show.setGenres(convertGenres(tmdbMovie.genres, Medium.MOVIE));
				show.setRuntime(tmdbMovie.runtime);
				show.setImageUrl(tmdbConfig.imageUrl + tmdbConfig.imageNormalPath + tmdbMovie.posterPath);

				if (!StringUtils.isEmpty(tmdbMovie.releaseDate)) {
					try {
						show.setReleaseDate(dateFormat.parse(tmdbMovie.releaseDate));
					}
					catch (ParseException e) {
						LOGGER.log(Level.SEVERE, "Error parsing show date value: " + tmdbMovie.releaseDate, e);
					}
				}

				return showRepository.save(show);
			}
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error", e);
		}

		return null;
	}

	@Override
	public Set<Genre> getGenres(Medium medium) {
		Set<Genre> genres = new LinkedHashSet<>();
		String mediumPath = (medium == null || medium == Medium.MOVIE) ? tmdbConfig.moviePath : tmdbConfig.tvPath;
		String url = new TMDBUrl(tmdbConfig.url)
				.addPath(tmdbConfig.genrePath)
				.addPath(mediumPath)
				.addPath(tmdbConfig.listPath)
				.getUrl();
		genres.addAll(getGenres(url, medium));
		//		genres.addAll(getGenres(env.getProperty("tmdb.api.url.genres.tv.get"), Medium.TV));

		return genres;
	}

	private Set<Genre> getGenres(String serviceUrl, Medium medium) {
		Set<Genre> genres = new LinkedHashSet<>();
		Map<String, MdbGenre> tmdbGenreMap = getTmdbGenreMap(serviceUrl);
		genres.addAll(convertGenres(tmdbGenreMap.values(), medium));

		return genres;
	}

	private Map<String, MdbGenre> getTmdbGenreMap(String serviceUrl) {
		Map<String, MdbGenre> results = new LinkedHashMap<>();

		try {
			List<String> urlParamsList = new ArrayList<>();
			urlParamsList.add("api_key=" + tmdbConfig.apiKey);
			urlParamsList.add("language=" + tmdbConfig.locale);

			String url = serviceUrl + "?" + StringUtils.collectionToDelimitedString(urlParamsList, "&");
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

	private Set<Genre> convertGenres(Collection<MdbGenre> mdbGenres, Medium medium) {
		Set<Genre> genres = new LinkedHashSet<>();

		if (mdbGenres != null) {
			for (MdbGenre mdbGenre : mdbGenres) {
				Genre genre = genreRepository.findByNameAndMedium(mdbGenre.name, medium);

				if (genre == null) {
					genre = new Genre();
					genre.setName(mdbGenre.name);
					genre.setMdbId(String.valueOf(mdbGenre.id));
					genre.setMedium(medium);
					genre = genreRepository.save(genre);
				}

				genres.add(genre);
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
