package com.rjs.mymovies.server.repos.tmdb;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 13:24<br>
 */
public class TMDBUrl {
	private String url;

	public TMDBUrl() {
		this("");
	}

	public TMDBUrl(String url) {
		this.url = url;
	}

	public TMDBUrl addPath(String path) {
		url += path;

		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
