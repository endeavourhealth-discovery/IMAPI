package org.endeavourhealth.imapi.aws;

import org.endeavourhealth.imapi.model.admin.User;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.*;

public class AWSCognitoClient {
  private static final Map<String, String> userCache = new HashMap<>();
  private static final String COGNITO_USER_POOL = System.getenv("COGNITO_USER_POOL");
  private static final String COGNITO_REGION = System.getenv("COGNITO_REGION");
  private static final String COGNITO_WEB_CLIENT = System.getenv("COGNITO_WEB_CLIENT");
  private final CognitoIdentityProviderClient identityProvider;

  public AWSCognitoClient() {
    this.identityProvider = createCognitoClient();
  }

  private CognitoIdentityProviderClient createCognitoClient() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_ACCESS_KEY"));
    AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
    return CognitoIdentityProviderClient.builder()
      .region(Region.of(COGNITO_REGION))
      .credentialsProvider(credentialsProvider)
      .build();
  }

  public String adminGetUsername(String id) throws UserNotFoundException {
    String username = userCache.get(id);
    if (null != username) return username;
    ListUsersRequest usersRequest = ListUsersRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .attributesToGet("sub").filter("sub = \"" + id + "\"")
      .build();
    ListUsersResponse result = identityProvider.listUsers(usersRequest);
    if (result.users().isEmpty()) throw new UserNotFoundException("User with id: " + id + " not found.");
    username = result.users().getFirst().username();
    userCache.put(id, username);
    return username;
  }

  public String adminGetId(String username) throws UserNotFoundException {
    String id = getKeysByValue(username);
    if (null != id) return id;
    AdminGetUserResponse result = adminGetUserByUsername(username);
    id = result.userAttributes().getFirst().value();
    userCache.put(id, username);
    return id;
  }

  public User adminGetUser(String username) throws UserNotFoundException {
    try {
      AdminGetUserRequest request = AdminGetUserRequest.builder()
        .userPoolId(COGNITO_USER_POOL)
        .username(username)
        .build();
      AdminGetUserResponse result = identityProvider.adminGetUser(request);
      User user = awsUserToUser(result);
      AdminListGroupsForUserRequest adminListGroupsForUserRequest = AdminListGroupsForUserRequest.builder()
        .userPoolId(COGNITO_USER_POOL)
        .username(username)
        .build();
      AdminListGroupsForUserResponse adminListGroupsForUserResult = identityProvider.adminListGroupsForUser(adminListGroupsForUserRequest);
      user.setRoles(adminListGroupsForUserResult.groups().stream().map(GroupType::groupName).toList());
      return user;
    } catch (CognitoIdentityProviderException e) {
      throw new UserNotFoundException("User with id: " + username + " not found.");
    }
  }

  public void adminAddUserToGroup(String username, String group) {
    AdminAddUserToGroupRequest adminAddUserToGroupRequest = AdminAddUserToGroupRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .username(username)
      .groupName(group)
      .build();
    identityProvider.adminAddUserToGroup(adminAddUserToGroupRequest);
  }

  public void adminRemoveUserFromGroup(String username, String group) {
    AdminRemoveUserFromGroupRequest adminRemoveUserFromGroupRequest = AdminRemoveUserFromGroupRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .username(username).groupName(group)
      .build();
    identityProvider.adminRemoveUserFromGroup(adminRemoveUserFromGroupRequest);
  }

  public void adminDeleteUser(String username) {
    AdminDeleteUserRequest adminDeleteUserRequest = AdminDeleteUserRequest.builder()
      .username(username)
      .userPoolId(COGNITO_USER_POOL)
      .build();
    identityProvider.adminDeleteUser(adminDeleteUserRequest);
  }

  private AdminGetUserResponse adminGetUserByUsername(String username) throws UserNotFoundException {
    try {
      AdminGetUserRequest adminGetUserRequest = AdminGetUserRequest.builder()
        .userPoolId(COGNITO_USER_POOL)
        .username(username)
        .build();
      return identityProvider.adminGetUser(adminGetUserRequest);
    } catch (CognitoIdentityProviderException e) {
      throw new UserNotFoundException("User with username: " + username + " not found.");
    }
  }

  public List<String> adminListUsers() {
    ListUsersRequest listUsersRequest = ListUsersRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .build();
    ListUsersResponse listUsersResult = identityProvider.listUsers(listUsersRequest);
    return listUsersResult.users().stream().map(UserType::username).sorted().toList();
  }

  public List<String> adminListGroups() {
    ListGroupsRequest listGroupsRequest = ListGroupsRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .build();
    ListGroupsResponse listGroupsResult = identityProvider.listGroups(listGroupsRequest);
    return listGroupsResult.groups().stream().map(GroupType::groupName).sorted().toList();
  }

  public List<String> adminListUsersInGroup(String groupName) {
    ListUsersInGroupRequest listUsersInGroupRequest = ListUsersInGroupRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .groupName(groupName)
      .build();
    ListUsersInGroupResponse listUsersInGroupResult = identityProvider.listUsersInGroup(listUsersInGroupRequest);
    return listUsersInGroupResult.users().stream().map(UserType::username).toList();
  }

  public List<User> adminListUsersInGroupAsUser(String groupName) throws UserNotFoundException {
    List<String> usernames = adminListUsersInGroup(groupName);
    List<User> users = new ArrayList<>();
    for (String username : usernames) {
      users.add(adminGetUser(username));
    }
    return users;
  }

  public User adminCreateUser(User user) {
    AdminCreateUserRequest adminCreateUserRequest = AdminCreateUserRequest.builder().userPoolId(COGNITO_USER_POOL)
      .username(user.getUsername())
      .userAttributes(AttributeType.builder().name("email").value(user.getEmail()).build())
      .userAttributes(AttributeType.builder().name("custom:forename").value(user.getFirstName()).build())
      .userAttributes(AttributeType.builder().name("custom:surname").value(user.getLastName()).build())
      .userAttributes(AttributeType.builder().name("custom:avatar").value(user.getAvatar()).build())
      .build();
    AdminCreateUserResponse result = identityProvider.adminCreateUser(adminCreateUserRequest);
    return awsUserToUser(result.user());
  }

  public void adminResetUserPassword(String username) {
    AdminResetUserPasswordRequest request = AdminResetUserPasswordRequest.builder().userPoolId(COGNITO_USER_POOL)
      .username(username)
      .build();
    identityProvider.adminResetUserPassword(request);
  }

  public boolean isEmailRegistered(String email) {
    if (null == email || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty.");
    ListUsersRequest listUsersRequest = ListUsersRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .attributesToGet("email")
      .filter("\"email\"=\"" + email + "\"")
      .build();
    ListUsersResponse users = identityProvider.listUsers(listUsersRequest);
    return !users.users().isEmpty();
  }

  public void updateEmailVerified(String username, boolean emailVerified) {
    AdminUpdateUserAttributesRequest request = AdminUpdateUserAttributesRequest.builder()
      .userPoolId(COGNITO_USER_POOL)
      .username(username)
      .userAttributes(AttributeType.builder().name("email_verified").value(emailVerified ? "true" : "false").build())
      .build();
    identityProvider.adminUpdateUserAttributes(request);
  }

  private String getKeysByValue(String value) {
    for (Map.Entry<String, String> entry : AWSCognitoClient.userCache.entrySet()) {
      if (Objects.equals(value, entry.getValue())) return entry.getKey();
    }
    return null;
  }

  private User awsUserToUser(AdminGetUserResponse result) {
    User user = new User();
    result.userAttributes().stream().filter(att -> att.name().equals("sub")).findFirst().ifPresent(id -> user.setId(id.value()));
    user.setUsername(result.username());
    user.setMfaStatus(result.mfaOptions().stream().map(MFAOptionType::attributeName).toList());
    result.userAttributes().stream().filter(att -> att.name().equals("email")).findFirst().ifPresent(email -> user.setEmail(email.value()));
    result.userAttributes().stream().filter(att -> att.name().equals("custom:forename")).findFirst().ifPresent(forename -> user.setFirstName(forename.value()));
    result.userAttributes().stream().filter(att -> att.name().equals("custom:surname")).findFirst().ifPresent(surname -> user.setLastName(surname.value()));
    result.userAttributes().stream().filter(att -> att.name().equals("custom:avatar")).findFirst().ifPresent(avatar -> user.setAvatar(avatar.value()));
    user.setPassword("");
    return user;
  }

  private User awsUserToUser(UserType result) {
    User user = new User();
    result.attributes().stream().filter(att -> att.name().equals("sub")).findFirst().ifPresent(id -> user.setId(id.value()));
    user.setUsername(result.username());
    user.setMfaStatus(result.mfaOptions().stream().map(MFAOptionType::attributeName).toList());
    result.attributes().stream().filter(att -> att.name().equals("email")).findFirst().ifPresent(email -> user.setEmail(email.value()));
    result.attributes().stream().filter(att -> att.name().equals("custom:forename")).findFirst().ifPresent(forename -> user.setFirstName(forename.value()));
    result.attributes().stream().filter(att -> att.name().equals("custom:surname")).findFirst().ifPresent(surname -> user.setLastName(surname.value()));
    result.attributes().stream().filter(att -> att.name().equals("custom:avatar")).findFirst().ifPresent(avatar -> user.setAvatar(avatar.value()));
    user.setPassword("");
    return user;
  }

  public void revokeToken(String token, String clientId, String clientSecret) {
    RevokeTokenRequest request = RevokeTokenRequest.builder()
      .token(token)
      .clientId(clientId)
      .clientSecret(clientSecret)
      .build();

    identityProvider.revokeToken(request);
  }

}

