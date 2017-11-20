package com.rjs.mymovies.server.model.mdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2/7/2017<br>
 * Time: 1:29 PM<br>
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class MdbShow implements Serializable {
	public int id;
	public String title;
	public String overview;
	@JsonProperty("poster_path")
	public String posterPath;
	@JsonProperty("release_date")
	public String releaseDate;
	@JsonProperty("genre_ids")
	public List<Integer> genreIds;
}
