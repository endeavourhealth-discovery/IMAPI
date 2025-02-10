package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.postRequestPrimatives.BooleanBody;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/cognito")
@CrossOrigin(origins = "*")
@Tag(name = "CognitoController")
@RequestScope
public class CognitoController {
  private static final Logger LOG = LoggerFactory.getLogger(CognitoController.class);
  private final AWSCognitoClient awsCognitoClient = new AWSCognitoClient();
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @GetMapping(value = "/public/config", produces = "application/json")
  @Operation(
    summary = "Get Cognito Configuration",
    description = "Retrieves configuration details for AWS Cognito, including regions, identity pool, user pool, and web client."
  )
  public String getConfig() {
    String region = Optional.ofNullable(System.getenv("COGNITO_REGION")).orElse("eu-west-2");
    String identity_pool = Optional.ofNullable(System.getenv("COGNITO_IDENTITY_POOL")).orElse("eu-west-2:a9f46df7-f27e-4cc5-827b-d573ecf20667");
    String user_pool = Optional.ofNullable(System.getenv("COGNITO_USER_POOL")).orElse("eu-west-2_Vt5ScFwss");
    String web_client = Optional.ofNullable(System.getenv("COGNITO_WEB_CLIENT")).orElse("57ihu3fqv3tidnj99dc5uicrgl");

    return """
      {
          "aws_project_region": "%s",
          "aws_cognito_identity_pool_id": "%s",
          "aws_cognito_region": "%s",
          "aws_user_pools_id": "%s",
          "aws_cognito_web_client_id": "%s",
          "oauth": {},
          "aws_cognito_username_attributes": [],
          "aws_cognito_social_providers": [],
          "aws_cognito_signup_attributes": [
              "EMAIL"
          ],
          "aws_cognito_mfa_configuration": "OFF",
          "aws_cognito_mfa_types": [
              "SMS"
          ],
          "aws_cognito_password_protection_settings": {
              "passwordPolicyMinLength": 8,
              "passwordPolicyCharacters": []
          },
          "aws_cognito_verification_mechanisms": [
              "EMAIL"
          ]
      }
      """.formatted(
      region,
      identity_pool,
      region,
      user_pool,
      web_client
    );
  }

  @GetMapping(value = "/public/isEmailRegistered")
  @Operation(
    summary = "Check Email Registration",
    description = "Checks if the provided email address is already registered in AWS Cognito."
  )
  public boolean isEmailRegistered(@RequestParam("email") String email) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.COGNITO.isEmailRegistered.GET")) {
      LOG.debug("isEmailRegistered");
      return awsCognitoClient.isEmailRegistered(email);
    }
  }

  @PostMapping(value = "/updateEmailVerified", produces = "application/json", consumes = "application/json")
  @Operation(
    summary = "Update Email Verification Status",
    description = "Updates the email verification status for the specified user in AWS Cognito."
  )
  public void updateEmailVerified(HttpServletRequest httpServletRequest, @RequestBody BooleanBody verified) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.COGNITO.updateEmailVerified.POST")) {
      LOG.debug("updateEmailVerified");
      String username = requestObjectService.getRequestAgentName(httpServletRequest);
      awsCognitoClient.updateEmailVerified(username, verified.getValue());
    }
  }
}
