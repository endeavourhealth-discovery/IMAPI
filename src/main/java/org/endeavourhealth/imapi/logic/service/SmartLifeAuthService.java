package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.aws.AWSCognitoClient;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class SmartLifeAuthService {

  AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

  public HttpResponse<String> getCredentials(Map<String, String> request) throws IOException, InterruptedException {
    try (HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build()) {

      StringBuilder formBodyBuilder = new StringBuilder();
      for (Map.Entry<String, String> singleEntry : request.entrySet()) {
        if (!formBodyBuilder.isEmpty()) {
          formBodyBuilder.append("&");
        }
        formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
        formBodyBuilder.append("=");
        formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
      }

      HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://eu-west-2vt5scfwss.auth.eu-west-2.amazoncognito.com/oauth2/token"))
        .POST(HttpRequest.BodyPublishers.ofString(formBodyBuilder.toString()))
        .setHeader("Content-Type", "application/x-www-form-urlencoded")
        .build();
      
      return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
  }

  public void revokeToken(Map<String, String> request) {
    awsCognitoClient.revokeToken(request.get("token"), request.get("client_id"), request.get("client_secret"));
  }
}
