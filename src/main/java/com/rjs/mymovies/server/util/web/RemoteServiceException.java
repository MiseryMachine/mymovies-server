package com.rjs.mymovies.server.util.web;

import java.io.IOException;

/**
 * <p>
 * Created with IntelliJ IDEA.<br/>
 * User: Randy Strobel<br/>
 * Date: 8/17/2016<br/>
 * Time: 5:00 PM<br/>
 */
public class RemoteServiceException extends IOException {
	public RemoteServiceException() {
	}

	public RemoteServiceException(String message) {
		super(message);
	}

	public RemoteServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemoteServiceException(Throwable cause) {
		super(cause);
	}
}
