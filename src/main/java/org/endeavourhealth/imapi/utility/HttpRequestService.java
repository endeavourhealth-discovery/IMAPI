package org.endeavourhealth.imapi.utility;

import org.apache.http.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequestService {

  public <T> T post(String url, Map<String, Object> body, String ipAddress, Class<T> responseType) throws HttpException {
    RestClient restClient = RestClient.create();
    ResponseEntity<T> response = restClient
      .post()
      .uri(url)
      .contentType(MediaType.APPLICATION_JSON)
      .body(body)
      .header("X-CLIENT-IP", ipAddress)
      .retrieve().toEntity(responseType);
    if (response.getStatusCode() != HttpStatus.OK) {
      throw new HttpException("Http post failed to: " + url + " with status " + response.getStatusCode());
    }
    return response.getBody();
  }

  public <T> T postForm(String url, Map<String, Object> body, String ipAddress, Class<T> responseType) throws HttpException {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
    for (Map.Entry<String, Object> entry : body.entrySet()) {
      formData.add(entry.getKey(), entry.getValue().toString());
    }
    RestClient restClient = RestClient.create();
    ResponseEntity<T> response = restClient
      .post()
      .uri(url)
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(formData)
      .header("X-CLIENT-IP", ipAddress)
      .retrieve().toEntity(responseType);
    if (response.getStatusCode() != HttpStatus.OK) {
      throw new HttpException("Http post failed to: " + url + " with status " + response.getStatusCode());
    }
    return response.getBody();
  }

  public <T> T get(String url, Map<String, String> params, String ipAddress, Class<T> responseType) throws HttpException {
    RestClient restClient = RestClient.create();
    URI uri = URI.create(url);
    return restClient
      .get()
      .uri(uriBuilder -> {
        uriBuilder
          .scheme(uri.getScheme())
          .host(uri.getHost())
          .port(uri.getPort())
          .path(uri.getPath());
        for (Map.Entry<String, String> entry : params.entrySet()) {
          uriBuilder.queryParam(entry.getKey(), urlStringToUri(entry.getValue()));
        }
        return uriBuilder.build();
      })
      .header("X-CLIENT-IP", ipAddress)
      .retrieve().body(responseType);
  }

  private String urlStringToUri(String url) {
    if (url.startsWith("http://") || url.startsWith("https://")) {
      return URLEncoder.encode(url, StandardCharsets.UTF_8);
    } else return url;
  }
}
