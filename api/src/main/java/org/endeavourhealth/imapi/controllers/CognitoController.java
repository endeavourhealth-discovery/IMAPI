package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.aws.AWSCognitoClient;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

@RestController
@RequestMapping("api/cognito")
@CrossOrigin(origins = "*")
@Tag(name = "CognitoController")
@RequestScope
public class CognitoController {
  private static final Logger LOG = LoggerFactory.getLogger(CognitoController.class);
  AWSCognitoClient awsCognitoClient = new AWSCognitoClient();

  @GetMapping(value = "/public/isEmailRegistered")
  public boolean isEmailRegistered(@RequestParam("email") String email) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.COGNITO.isEmailRegistered.GET")) {
      LOG.debug("isEmailRegistered");
      return awsCognitoClient.isEmailRegistered(email);
    }
  }
}
