package com.rjs.mymovies.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
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

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
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
