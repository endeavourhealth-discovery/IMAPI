package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.aws.UserNotFoundException;
import org.endeavourhealth.imapi.model.admin.CognitoGroupRequest;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@PreAuthorize("hasAuthority('IMAdmin')")
public class AdminController {
  private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
  AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

  @GetMapping(value="/cognito/users")
  public List<String> listUsers() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.users.GET")) {
      LOG.debug("getUsers");
      return awsCognitoClient.adminListUsers();
    }
  }

  @GetMapping(value = "/cognito/user")
  public User getUser(@RequestParam(name = "username") String username) throws IOException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.User.get")) {
      LOG.debug("getUser");
      return awsCognitoClient.adminGetUser(username);
    }
  }

  @GetMapping(value = "/cognito/groups")
  public List<String> listGroups() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.groups.GET")) {
      LOG.debug("getGroups");
      return awsCognitoClient.adminListGroups();
    }
  }

  @GetMapping(value = "/cognito/usersInGroup")
  public List<String> listUsersInGroup(@RequestParam("group") String group) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.usersInGroup.GET")) {
      LOG.debug("getUsersInGroup");
      return awsCognitoClient.adminListUsersInGroup(group);
    }
  }

  @PostMapping(value = "/cognito/addUserToGroup")
  public void addUserToGroup(@RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.addUserToGroup.POST")) {
      LOG.debug("addUserToGroup");
      awsCognitoClient.adminAddUserToGroup(cognitoGroupRequest.getUsername(), cognitoGroupRequest.getGroupName());
    }
  }

  @PostMapping(value = "/cognito/removeUserFromGroup")
  public void removeUserFromGroup(@RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.removeUserFromGroup.POST")) {
      LOG.debug("removeUserFromGroup");
      awsCognitoClient.adminRemoveUserFromGroup(cognitoGroupRequest.getUsername(), cognitoGroupRequest.getGroupName());
    }
  }

  @PostMapping(value = "cognito/deleteUser")
  public void deleteUser(@RequestBody CognitoGroupRequest cognitoGroupRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Admin.Cognito.deleteUser.POST")) {
      LOG.debug("deleteUser");
      awsCognitoClient.adminDeleteUser(cognitoGroupRequest.getUsername());
    }
  }

  @PostMapping(value = "cognito/createUser")
  public User createUser(@RequestBody User user) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Cognito.createUser.POST")) {
      LOG.debug("createUser");
      return awsCognitoClient.adminCreateUser(user);
    }
  }
}
