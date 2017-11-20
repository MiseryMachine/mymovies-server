package com.rjs.mymovies.server.util.web;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * <p>
 * Created with IntelliJ IDEA.<br/>
 * User: Randy Strobel<br/>
 * Date: 5/11/2016<br/>
 * Time: 2:33 PM<br/>
 */
public class HttpUtil {
	public static <E> HttpEntity<E> createHttpEntity(E body) {
		return createHttpEntity(body, null, null, null);
	}

	public static <E> HttpEntity<E> createHttpEntity(E body, String username, String password) {
		return createHttpEntity(body, username, password, null);
	}

	public static <E> HttpEntity<E> createHttpEntity(E body, MediaType contentType) {
		return createHttpEntity(body, null, null, contentType);
	}

	public static <E> HttpEntity<E> createHttpEntity(E body, String username, String password, MediaType contentType) {
		HttpHeaders headers = new HttpHeaders();

		if (contentType != null) {
			//			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setContentType(contentType);
		}

		if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
			String auth = username + ":" + password;
			byte[] encodedAuth = Base64.encodeBase64(
				auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.set("Authorization", authHeader);
		}

		return body != null ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);
	}
}
