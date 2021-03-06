package com.rjs.mymovies.server.service.mdb.tmdb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 13:12<br>
 */
@Configuration
@ConfigurationProperties("tmdb.api")
public class TmdbConfig {
	private String key;
	private String locale;
	private String url;
	private String imageUrl;
	private String imageNormalPath;
	private String imageThumbPath;
	private String genrePath;
	private String moviePath;
	private String tvPath;
	private String searchPath;
	private String listPath;

	public TmdbConfig() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageNormalPath() {
		return imageNormalPath;
	}

	public void setImageNormalPath(String imageNormalPath) {
		this.imageNormalPath = imageNormalPath;
	}

	public String getImageThumbPath() {
		return imageThumbPath;
	}

	public void setImageThumbPath(String imageThumbPath) {
		this.imageThumbPath = imageThumbPath;
	}

	public String getGenrePath() {
		return genrePath;
	}

	public void setGenrePath(String genrePath) {
		this.genrePath = genrePath;
	}

	public String getMoviePath() {
		return moviePath;
	}

	public void setMoviePath(String moviePath) {
		this.moviePath = moviePath;
	}

	public String getTvPath() {
		return tvPath;
	}

	public void setTvPath(String tvPath) {
		this.tvPath = tvPath;
	}

	public String getSearchPath() {
		return searchPath;
	}

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public String getListPath() {
		return listPath;
	}

	public void setListPath(String listPath) {
		this.listPath = listPath;
	}
}
