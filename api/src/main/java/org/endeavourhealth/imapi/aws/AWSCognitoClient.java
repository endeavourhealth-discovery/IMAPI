package org.endeavourhealth.imapi.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import org.endeavourhealth.imapi.model.admin.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AWSCognitoClient {
  private static final Map<String, String> userCache = new HashMap<>();
  private final AWSCognitoIdentityProvider identityProvider;

  public AWSCognitoClient() {
    this.identityProvider = createCognitoClient();
  }

  private AWSCognitoIdentityProvider createCognitoClient() {
    AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_ACCESS_KEY"));
    AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(cred);
    return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(credentialsProvider).withRegion(System.getenv("COGNITO_REGION")).build();
  }

  public String adminGetUsername(String id) throws UserNotFoundException {
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
    AdminGetUserResult result = adminGetUserByUsername(username);
    id = result.getUserAttributes().get(0).getValue();
    userCache.put(id, username);
    return id;
  }

  public User adminGetUser(String username) throws UserNotFoundException {
    try {
      AdminGetUserRequest request = new AdminGetUserRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withUsername(username);
      AdminGetUserResult result = identityProvider.adminGetUser(request);
      User user = awsUserToUser(result);
      AdminListGroupsForUserRequest adminListGroupsForUserRequest = new AdminListGroupsForUserRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withUsername(username);
      AdminListGroupsForUserResult adminListGroupsForUserResult = identityProvider.adminListGroupsForUser(adminListGroupsForUserRequest);
      user.setRoles(adminListGroupsForUserResult.getGroups().stream().map(GroupType::getGroupName).toList());
      return user;
    } catch (AmazonServiceException ase) {
      throw new UserNotFoundException("User with id: " + username + " not found.");
    }
  }

  public void adminAddUserToGroup(String username, String group) {
    AdminAddUserToGroupRequest adminAddUserToGroupRequest = new AdminAddUserToGroupRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withUsername(username).withGroupName(group);
    identityProvider.adminAddUserToGroup(adminAddUserToGroupRequest);
  }

  public void adminRemoveUserFromGroup(String username, String group) {
    AdminRemoveUserFromGroupRequest adminRemoveUserFromGroupRequest = new AdminRemoveUserFromGroupRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withUsername(username).withGroupName(group);
    identityProvider.adminRemoveUserFromGroup(adminRemoveUserFromGroupRequest);
  }

  public void adminDeleteUser(String username) {
    AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest().withUsername(username).withUserPoolId(System.getenv("COGNITO_USER_POOL"));
    identityProvider.adminDeleteUser(adminDeleteUserRequest);
  }

  private AdminGetUserResult adminGetUserByUsername(String username) throws UserNotFoundException {
    try {
      AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withUsername(username);
      return identityProvider.adminGetUser(adminGetUserRequest);
    } catch (AWSCognitoIdentityProviderException e) {
      throw new UserNotFoundException("User with username: " + username + " not found.");
    }
  }

  public List<String> adminListUsers() {
    ListUsersRequest listUsersRequest = new ListUsersRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL"));
    ListUsersResult listUsersResult = identityProvider.listUsers(listUsersRequest);
    return listUsersResult.getUsers().stream().map(UserType::getUsername).sorted().toList();
  }

  public List<String> adminListGroups() {
    ListGroupsRequest listGroupsRequest = new ListGroupsRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL"));
    ListGroupsResult listGroupsResult = identityProvider.listGroups(listGroupsRequest);
    return listGroupsResult.getGroups().stream().map(GroupType::getGroupName).sorted().toList();
  }

  public List<String> adminListUsersInGroup(String groupName) {
    ListUsersInGroupRequest listUsersInGroupRequest = new ListUsersInGroupRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL")).withGroupName(groupName);
    ListUsersInGroupResult listUsersInGroupResult = identityProvider.listUsersInGroup(listUsersInGroupRequest);
    return listUsersInGroupResult.getUsers().stream().map(UserType::getUsername).toList();
  }

  public User adminCreateUser(User user) {
    AdminCreateUserRequest adminCreateUserRequest = new AdminCreateUserRequest().withUserPoolId(System.getenv("COGNITO_USER_POOL"))
      .withUsername(user.getUsername())
      .withUserAttributes(new AttributeType().withName("email").withValue(user.getEmail()))
      .withUserAttributes(new AttributeType().withName("custom:forename").withValue(user.getFirstName()))
      .withUserAttributes(new AttributeType().withName("custom:surname").withValue(user.getLastName()))
      .withUserAttributes(new AttributeType().withName("custom:avatar").withValue(user.getAvatar()));
    AdminCreateUserResult result = identityProvider.adminCreateUser(adminCreateUserRequest);
    return awsUserToUser(result.getUser());
  }

  private String getKeysByValue(String value) {
    for (Map.Entry<String, String> entry : AWSCognitoClient.userCache.entrySet()) {
      if (Objects.equals(value, entry.getValue())) return entry.getKey();
    }
    return null;
  }

  private User awsUserToUser(AdminGetUserResult result) {
    User user = new User();
    result.getUserAttributes().stream().filter(att -> att.getName().equals("sub")).findFirst().ifPresent(id -> user.setId(id.getValue()));
    user.setUsername(result.getUsername());
    user.setMfaStatus(result.getUserMFASettingList());
    result.getUserAttributes().stream().filter(att -> att.getName().equals("email")).findFirst().ifPresent(email -> user.setEmail(email.getValue()));
    result.getUserAttributes().stream().filter(att -> att.getName().equals("custom:forename")).findFirst().ifPresent(forename -> user.setFirstName(forename.getValue()));
    result.getUserAttributes().stream().filter(att -> att.getName().equals("custom:surname")).findFirst().ifPresent(surname -> user.setLastName(surname.getValue()));
    result.getUserAttributes().stream().filter(att -> att.getName().equals("custom:avatar")).findFirst().ifPresent(avatar -> user.setAvatar(avatar.getValue()));
    user.setPassword("");
    return user;
  }

  private User awsUserToUser(UserType result) {
    User user = new User();
    result.getAttributes().stream().filter(att -> att.getName().equals("sub")).findFirst().ifPresent(id -> user.setId(id.getValue()));
    user.setUsername(result.getUsername());
    user.setMfaStatus(result.getMFAOptions().stream().map(MFAOptionType::getAttributeName).toList());
    result.getAttributes().stream().filter(att -> att.getName().equals("email")).findFirst().ifPresent(email -> user.setEmail(email.getValue()));
    result.getAttributes().stream().filter(att -> att.getName().equals("custom:forename")).findFirst().ifPresent(forename -> user.setFirstName(forename.getValue()));
    result.getAttributes().stream().filter(att -> att.getName().equals("custom:surname")).findFirst().ifPresent(surname -> user.setLastName(surname.getValue()));
    result.getAttributes().stream().filter(att -> att.getName().equals("custom:avatar")).findFirst().ifPresent(avatar -> user.setAvatar(avatar.getValue()));
    user.setPassword("");
    return user;
  }
}

