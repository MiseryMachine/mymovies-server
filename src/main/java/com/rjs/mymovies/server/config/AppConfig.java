package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:27<br>
 */
@Configuration
@ConfigurationProperties("mymovies")
public class AppConfig {
	private static final String SHOW_PATH = "/shows";
	private static final String IMAGE_PATH = "/images";
	private static final int POSTER_THUMB_WIDTH = 92;
//	@Value(("${date.pattern:MM/dd/yyyy}"))
	private String datePattern;
	private String localFilePath;

	public AppConfig() {
	}

	@Bean
	public SimpleDateFormat dateFormat() {
		return new SimpleDateFormat(datePattern);
	}

	@Bean
	public ObjectMapper jsonObjectMapper() {
		return new ObjectMapper();
	}

	public String getLocalImagePath(String showId) {
		return localFilePath + SHOW_PATH + "/" + showId + IMAGE_PATH;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
}
