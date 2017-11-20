package com.rjs.mymovies.server.model;

import org.springframework.data.annotation.Id;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2017-07-06<br>
 * Time: 11:01<br>
 */
public abstract class AbstractElement {
	@Id
	protected String id;

	protected AbstractElement() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
