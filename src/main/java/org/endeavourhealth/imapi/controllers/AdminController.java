package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.postRequestPrimatives.StringBody;
import org.endeavourhealth.imapi.model.requests.CognitoGroupRequest;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
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
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
  AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

  @GetMapping(value = "/cognito/users")
  @Operation(
    summary = "List Cognito users",
    description = "Retrieve a list of all Cognito users.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successful retrieval of usernames")
    }
  )
  public List<String> listUsers() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.users.GET")) {
      log.debug("getUsers");
      return awsCognitoClient.adminListUsers();
    }
  }

  @GetMapping(value = "/cognito/user")
  @Operation(
    summary = "Get a Cognito user",
    description = "Retrieve a specific Cognito user by their username.",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful retrieval of user",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = User.class)
        )
      )
    }
  )
  public User getUser(@Parameter(description = "The username of the Cognito user to retrieve.") @RequestParam(name = "username") String username) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.User.get")) {
      log.debug("getUser");
      return awsCognitoClient.adminGetUser(username);
    }
  }

  @GetMapping(value = "/cognito/groups")
  @Operation(summary = "List Cognito groups", description = "Retrieve a list of all Cognito user groups.")
  public List<UserRole> listGroups() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.groups.GET")) {
      log.debug("getGroups");
      return awsCognitoClient.adminListGroups();
    }
  }

  @GetMapping(value = "/cognito/group/users")
  @Operation(summary = "List users in Cognito group", description = "Retrieve a list of all users within a specific Cognito group.")
  public List<String> listUsersInGroup(@Parameter(description = "The name of the Cognito group.") @RequestParam("group") String group) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.usersInGroup.GET")) {
      log.debug("getUsersInGroup");
      return awsCognitoClient.adminListUsersInGroup(group);
    }
  }

  @GetMapping(value = "/cognito/group/usersAsUser")
  @Operation(summary = "List users in Cognito group as User object")
  public List<User> listUsersInGroupAsUser(@Parameter(description = "The name of the Cognito group.") @RequestParam("group") String group) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.usersInGroupAsUser.GET")) {
      log.debug("getUsersInGroupAsUser");
      return awsCognitoClient.adminListUsersInGroupAsUser(group);
    }
  }

  @PostMapping(value = "/cognito/group/user")
  @Operation(summary = "Add user to Cognito group", description = "Add a specific user to a Cognito group.")
  public void addUserToGroup(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request payload containing the username and group name.") @RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.addUserToGroup.POST")) {
      log.debug("addUserToGroup");
      awsCognitoClient.adminAddUserToGroup(cognitoGroupRequest.getUsername(), cognitoGroupRequest.getGroupName());
    }
  }

  @DeleteMapping(value = "/cognito/group/user")
  @Operation(summary = "Remove user from Cognito group", description = "Remove a specific user from a Cognito group.")
  public void removeUserFromGroup(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request payload containing the username and group name.") @RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.removeUserFromGroup.DELETE")) {
      log.debug("removeUserFromGroup");
      awsCognitoClient.adminRemoveUserFromGroup(cognitoGroupRequest.getUsername(), cognitoGroupRequest.getGroupName());
    }
  }

  @DeleteMapping(value = "cognito/user")
  @Operation(summary = "Delete Cognito user", description = "Delete a specific Cognito user by username.")
  public void deleteUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The request payload containing the username.") @RequestBody StringBody username) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.deleteUser.DELETE")) {
      log.debug("deleteUser");
      awsCognitoClient.adminDeleteUser(username.getValue());
    }
  }

  @PostMapping(value = "cognito/user")
  @Operation(summary = "Create Cognito user", description = "Create a new Cognito user.")
  public User createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The user details to create.") @RequestBody User user) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Cognito.createUser.POST")) {
      log.debug("createUser");
      return awsCognitoClient.adminCreateUser(user);
    }
  }

  @DeleteMapping(value = "cognito/user/resetPassword")
  @Operation(summary = "Reset Cognito user password", description = "Reset a specific user's password in Cognito.")
  public void resetPassword(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The username for which to reset the password.") @RequestBody StringBody username) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.user.resetPassword.DELETE")) {
      log.debug("resetPassword");
      awsCognitoClient.adminResetUserPassword(username.getValue());
    }
  }
}
