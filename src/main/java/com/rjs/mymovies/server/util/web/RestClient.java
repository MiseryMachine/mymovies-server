package com.rjs.mymovies.server.util.web;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p/>
 * Created with IntelliJ IDEA.<br>
 * User: Randy Strobel<br>
 * Date: 2/7/2017<br>
 * Time: 12:12 PM<br>
 */
public class RestClient {
	private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

	public static ResponseEntity<LinkedHashMap<String, Object>> exchangeMap(HttpMethod httpMethod, String url, String userName, String password, String requestObject, Map<String, String> uriParameters) throws WebServiceException {
		return exchange(httpMethod, url, userName, password, requestObject, new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {
		}, uriParameters);
	}

	public static <T> ResponseEntity<T> exchange(HttpMethod httpMethod, String url, String userName, String password, String requestObject, ParameterizedTypeReference<T> typeReference, Map<String, String> uriParameters) throws WebServiceException {
		LOGGER.info("Performing " + httpMethod.name().toLowerCase() + " method to: " + url);
		HttpEntity httpEntity = HttpUtil.createHttpEntity(requestObject, userName, password, MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		WebServiceErrorHandler errorHandler = new WebServiceErrorHandler(url);
		restTemplate.setErrorHandler(errorHandler);

		try {
			if (uriParameters != null && !uriParameters.isEmpty()) {
				return restTemplate.exchange(url, httpMethod, httpEntity, typeReference, uriParameters);
			}
			else {
				return restTemplate.exchange(url, httpMethod, httpEntity, typeReference);
			}
		}
		catch (RestClientException e) {
			HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
			String message = "Web service error calling " + url + ": " + status.getReasonPhrase() + " (" + status.toString() + ")\nCause: " + e.getMessage();
			LOGGER.log(Level.SEVERE, message, e);

			throw new WebServiceException(message, e, status);
		}
	}
}
