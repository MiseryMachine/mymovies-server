package com.rjs.mymovies.server.util.web;

import org.springframework.http.HttpStatus;

/**
 * <p>
 * Created with IntelliJ IDEA.<br/>
 * User: Randy Strobel<br/>
 * Date: 5/19/2016<br/>
 * Time: 12:45 PM<br/>
 */
public class WebServiceException extends RemoteServiceException {
	private HttpStatus status;

	public WebServiceException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public WebServiceException(String message, Throwable cause, HttpStatus status) {
		super(message, cause);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
