package com.rjs.mymovies.server.repos.tmdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 13:12<br>
 */
@Configuration
//@PropertySource("classpath:com.rjs.mymovies.repos.tmdb.tmdb-config.properties")
@PropertySource("classpath:/com/rjs/mymovies/repos/tmdb/tmdb-config.properties")
public class TMDBConfig {
	@Value(("${tmdb.api.key}"))
	public String apiKey;
	@Value(("${tmdb.api.locale}"))
	public String locale;
	@Value(("${tmdb.api.url}"))
	public String url;
	@Value(("${tmdb.api.url.genre}"))
	public String genrePath;
	@Value(("${tmdb.api.url.movie}"))
	public String moviePath;
	@Value(("${tmdb.api.url.tv}"))
	public String tvPath;
	@Value(("${tmdb.api.url.image}"))
	public String imageUrl;
	@Value(("${tmdb.api.url.image.normal}"))
	public String imageNormalPath;
	@Value(("${tmdb.api.url.image.thumb}"))
	public String imageThumbPath;
	@Value(("${tmdb.api.url.search}"))
	public String searchPath;
	@Value(("${tmdb.api.url.list}"))
	public String listPath;
}
