package com.rjs.mymovies.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyMoviesServerApplication {
	public MyMoviesServerApplication() {
	}

//	@Bean
//	public LocalValidatorFactoryBean validator() {
//		return new LocalValidatorFactoryBean();
//	}

	public static void main(String[] args) {
		SpringApplication.run(MyMoviesServerApplication.class, args);
	}
}
