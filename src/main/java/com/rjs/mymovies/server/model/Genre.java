package com.rjs.mymovies.server.model;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 11:25<br>
 */
public class Genre extends AbstractElement {
	@Indexed(unique = true, dropDups = true)
	private String mdbId;
	private Medium medium;
	@NotNull(message = "Genre must have a name.")
	private String name;

	public Genre() {
	}

	public String getMdbId() {
		return mdbId;
	}

	public void setMdbId(String mdbId) {
		this.mdbId = mdbId;
	}

	public Medium getMedium() {
		return medium;
	}

	public void setMedium(Medium medium) {
		this.medium = medium;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name + " [" + medium + "]";
	}
}
