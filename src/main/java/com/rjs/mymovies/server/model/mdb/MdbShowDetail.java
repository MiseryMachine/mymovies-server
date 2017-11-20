package com.rjs.mymovies.server.model.mdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2/7/2017<br>
 * Time: 3:37 PM<br>
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class MdbShowDetail extends MdbShow {
	public List<MdbGenre> genres;
	@JsonProperty("imdb_id")
	public String imdbId;
	public int runtime;
	public String tagline;
}
