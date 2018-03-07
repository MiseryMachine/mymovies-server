package com.rjs.mymovies.server.service.mdb.tmdb;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 13:24<br>
 */
public class TmdbUrl {
	private String url;

	public TmdbUrl() {
		this("");
	}

	public TmdbUrl(String url) {
		this.url = url;
	}

	public TmdbUrl addPath(String path) {
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
