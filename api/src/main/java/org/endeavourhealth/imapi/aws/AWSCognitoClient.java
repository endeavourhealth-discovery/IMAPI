package org.endeavourhealth.imapi.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AWSCognitoClient {
  private static final Map<String, String> userCache = new HashMap<>();
  private final AWSCognitoIdentityProvider identityProvider;

  public AWSCognitoClient() {
    this.identityProvider = createCognitoClient();
  }

  private AWSCognitoIdentityProvider createCognitoClient() {
    AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_ACCESS_KEY"));
    AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(cred);
    return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(credentialsProvider).withRegion(System.getenv("COGNITO_REGION")).build();
  }

  public String adminGetUser(String id) throws UserNotFoundException {
    String username = userCache.get(id);
    if (null != username) return username;
    ListUsersRequest usersRequest = new ListUsersRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withAttributesToGet("sub").withFilter("sub = \"" + id + "\"");
    ListUsersResult result = identityProvider.listUsers(usersRequest);
    if (result.getUsers().isEmpty()) throw new UserNotFoundException("User with id: " + id + " not found.");
    username = result.getUsers().get(0).getUsername();
    userCache.put(id, username);
    return username;
  }

  public String adminGetId(String username) throws UserNotFoundException {
    String id = getKeysByValue(username);
    if (null != id) return id;
    ListUsersRequest usersRequest = new ListUsersRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withAttributesToGet("sub").withFilter("username = \"" + username + "\"");
    ListUsersResult result = identityProvider.listUsers(usersRequest);
    if (result.getUsers().isEmpty()) throw new UserNotFoundException("User with username: " + username + " not found.");
    id = result.getUsers().get(0).getAttributes().get(0).getValue();
    userCache.put(id, username);
    return id;
  }

  private String getKeysByValue(String value) {
    for (Map.Entry<String, String> entry : AWSCognitoClient.userCache.entrySet()) {
      if (Objects.equals(value, entry.getValue())) return entry.getKey();
    }
    return null;
  }
}

