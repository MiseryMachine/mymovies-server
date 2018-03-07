package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
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
@ConfigurationProperties("my-movies")
public class AppConfig {
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String SHOW_PATH = "/shows";
	private static final String IMAGE_PATH = "/images";
	private String localFilePath;

	public AppConfig() {
	}

	@Bean
	public SimpleDateFormat dateFormat() {
		return new SimpleDateFormat(DATE_PATTERN);
	}

	@Bean
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(dateFormat());

		return objectMapper;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public String getLocalImagePath(String showId) {
		return localFilePath + SHOW_PATH + "/" + showId + IMAGE_PATH;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
}
