package org.endeavourhealth.imapi.logic.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.aws.*;
import org.endeavourhealth.imapi.model.smartlife.OAuthTokenRequest;
import org.endeavourhealth.imapi.model.smartlife.OAuthTokenResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;

import java.util.Map;


public class SmartLifeAuthService {

  AWSCognitoClient client = new AWSCognitoClient();

  public OAuthTokenResponse getCredentials(Map<String, String> request) {
    final ObjectMapper mapper = new ObjectMapper();
    final OAuthTokenRequest tokenRequest = mapper.convertValue(request, OAuthTokenRequest.class);
    return client.initiateAuth(tokenRequest.getClient_id(), tokenRequest.getClient_secret());
  }

  public void revokeToken(Map<String, String> request) {
    final ObjectMapper mapper = new ObjectMapper();
    final OAuthTokenRequest tokenRequest = mapper.convertValue(request, OAuthTokenRequest.class);
    client.revokeToken(tokenRequest.getToken(), tokenRequest.getClient_id(), tokenRequest.getClient_secret());
  }
}
