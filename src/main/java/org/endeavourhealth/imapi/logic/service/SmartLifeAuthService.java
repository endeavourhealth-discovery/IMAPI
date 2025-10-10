package org.endeavourhealth.imapi.logic.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class SmartLifeAuthService {

  public HttpResponse<String> getCredentials(String clientId, String clientSecret) throws IOException, InterruptedException {
    try (HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build()) {

      String clientHash = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

      Map<String, String> params = new HashMap<>();
      params.put("grant_type", "client_credentials");
      params.put("client_id", clientId);

      String formData = params.entrySet()
        .stream()
        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));

      URI uri = URI.create(String.format("https://%s.auth.%s.amazoncognito.com/oauth2/token",
        System.getenv("COGNITO_USER_POOL").replace("_", "").toLowerCase(),
        System.getenv("COGNITO_REGION").toLowerCase()
      ));

      HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(uri)
        .setHeader("Content-Type", "application/x-www-form-urlencoded")
        .setHeader("Authorization", "Basic " + clientHash)
        .POST(HttpRequest.BodyPublishers.ofString(formData))
        .build();

      return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
  }

  public void revokeToken(Map<String, String> request) {
    // TODO
  }
}
