package com.rjs.mymovies.server.util.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * <p>
 * Created with IntelliJ IDEA.<br/>
 * User: Randy Strobel<br/>
 * Date: 6/24/2016<br/>
 * Time: 12:33 PM<br/>
 */
public class WebServiceErrorHandler implements ResponseErrorHandler {
	private String url;
	private HttpStatus status;
	private String message;

	public WebServiceErrorHandler(String url) {
		this.url = url;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
		HttpStatus.Series series = clientHttpResponse.getStatusCode().series();

		return series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
		String serviceReply = clientHttpResponse.getHeaders().getFirst("service_reply");
		status = clientHttpResponse.getStatusCode();

		message = "Web service error calling " + url + ":\n" + clientHttpResponse.getStatusText() + " (" + status.toString() + ")";

		if (serviceReply != null && !serviceReply.trim().isEmpty()) {
			message += " - " + serviceReply;
		}

		throw new WebServiceException(message, status);
	}
}
