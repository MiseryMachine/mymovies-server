package com.rjs.mymovies.server.model;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 12:10<br>
 */
public class MediaFormat extends AbstractElement {
	@Indexed(unique = true, dropDups = true)
	@NotNull(message = "Media format must have a name.")
	private String name;
	private String description;

	public MediaFormat() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
