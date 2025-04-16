package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.model.admin.CognitoGroupRequest;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.postRequestPrimatives.StringBody;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@CrossOrigin(origins = "*")
@Tag(name = "AdminController")
@RequestScope
@Slf4j
@PreAuthorize("hasAuthority('IMAdmin')")
public class AdminController {
  AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

  @Operation(summary = "List Cognito users", description = "Retrieve a list of all Cognito users.")
  @GetMapping(value = "/cognito/users")
  public List<String> listUsers() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.users.GET")) {
      log.debug("getUsers");
      return awsCognitoClient.adminListUsers();
    }
  }

  @Operation(summary = "Get a Cognito user", description = "Retrieve a specific Cognito user by their username.")
  @GetMapping(value = "/cognito/user")
  public User getUser(@Parameter(description = "The username of the Cognito user to retrieve.") @RequestParam(name = "username") String username) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.User.get")) {
      log.debug("getUser");
      return awsCognitoClient.adminGetUser(username);
    }
  }

  @Operation(summary = "List Cognito groups", description = "Retrieve a list of all Cognito user groups.")
  @GetMapping(value = "/cognito/groups")
  public List<String> listGroups() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.groups.GET")) {
      log.debug("getGroups");
      return awsCognitoClient.adminListGroups();
    }
  }

  @Operation(summary = "List users in Cognito group", description = "Retrieve a list of all users within a specific Cognito group.")
  @GetMapping(value = "/cognito/group/users")
  public List<String> listUsersInGroup(@Parameter(description = "The name of the Cognito group.") @RequestParam("group") String group) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.usersInGroup.GET")) {
      log.debug("getUsersInGroup");
      return awsCognitoClient.adminListUsersInGroup(group);
    }
  }

  @Operation(summary = "Add user to Cognito group", description = "Add a specific user to a Cognito group.")
  @PostMapping(value = "/cognito/group/user")
  public void addUserToGroup(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request payload containing the username and group name.") @RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.addUserToGroup.POST")) {
      log.debug("addUserToGroup");
      awsCognitoClient.adminAddUserToGroup(cognitoGroupRequest.getUsername(), cognitoGroupRequest.getGroupName());
    }
  }

  @Operation(summary = "Remove user from Cognito group", description = "Remove a specific user from a Cognito group.")
  @DeleteMapping(value = "/cognito/group/user")
  public void removeUserFromGroup(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request payload containing the username and group name.") @RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.removeUserFromGroup.DELETE")) {
      log.debug("removeUserFromGroup");
      awsCognitoClient.adminRemoveUserFromGroup(cognitoGroupRequest.getUsername(), cognitoGroupRequest.getGroupName());
    }
  }

  @Operation(summary = "Delete Cognito user", description = "Delete a specific Cognito user by username.")
  @DeleteMapping(value = "cognito/user")
  public void deleteUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request payload containing the username.") @RequestBody StringBody username) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.deleteUser.DELETE")) {
      log.debug("deleteUser");
      awsCognitoClient.adminDeleteUser(username.getValue());
    }
  }

  @Operation(summary = "Create Cognito user", description = "Create a new Cognito user.")
  @PostMapping(value = "cognito/user")
  public User createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The user details to create.") @RequestBody User user) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Cognito.createUser.POST")) {
      log.debug("createUser");
      return awsCognitoClient.adminCreateUser(user);
    }
  }

  @Operation(summary = "Reset Cognito user password", description = "Reset a specific user's password in Cognito.")
  @DeleteMapping(value = "cognito/user/resetPassword")
  public void resetPassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The username for which to reset the password.") @RequestBody StringBody username) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.user.resetPassword.DELETE")) {
      log.debug("resetPassword");
      awsCognitoClient.adminResetUserPassword(username.getValue());
    }
  }
}
