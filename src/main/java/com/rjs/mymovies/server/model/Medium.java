package com.rjs.mymovies.server.model;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 11:26<br>
 */
public enum Medium {
	MOVIE("Movie"),
	TV("TV"),
	STREAM("Streaming");

	private String text;

	Medium(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}
}
