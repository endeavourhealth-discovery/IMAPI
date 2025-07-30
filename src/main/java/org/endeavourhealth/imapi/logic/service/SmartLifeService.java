package org.endeavourhealth.imapi.logic.service;


import org.endeavourhealth.imapi.aws.*;
import org.endeavourhealth.imapi.model.smartlife.OAuthTokenResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.RevokeTokenRequest;


public class SmartLifeService {

  AWSCognitoClient client = new AWSCognitoClient();

  public OAuthTokenResponse getCredentials(String userName, String password) {
    AdminInitiateAuthResponse response = client.initiateAuth(userName, password);
    String a = response.toString();
    return new OAuthTokenResponse();
  }

  public void revokeToken(String token, String clientId, String clientSecret) {
    client.revokeToken(token, clientId, clientSecret);
  }
}
