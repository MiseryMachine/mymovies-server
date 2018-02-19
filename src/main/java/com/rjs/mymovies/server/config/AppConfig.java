package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.text.SimpleDateFormat;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:27<br>
 */
@Configuration
public class AppConfig {
	@Value(("${date.pattern:MM/dd/yyyy}"))
	private String datePattern;
	@Value(("${spring.profiles.active:unknown}"))
	private String profile;
	@Value(("${spring.datasource.driver-class-name:unknown}"))
	private String dbDriver;

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public SimpleDateFormat dateFormat() {
		return new SimpleDateFormat(datePattern);
	}

	@Bean
	public ObjectMapper jsonObjectMapper() {
		return new ObjectMapper();
	}
}
