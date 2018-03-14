package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());

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

	@Bean
	public File defaultBoxArt() {
		try {
			return new ClassPathResource("/static/img/default-movie-box.png").getFile();
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Cannot locate default box art image.", e);
		}

		return null;
	}

	@Bean
	public File defaultBoxArtThumb() {
		try {
			return new ClassPathResource("/static/img/default-movie-box_thumb.png").getFile();
		}
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Cannot locate default box art thumb image.", e);
		}

		return null;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getLocalImagePath(String showId) {
		return localFilePath + "/shows/" + showId + "/images";
	}
}
