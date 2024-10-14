package org.endeavourhealth.imapi.controllers;

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

@RestController
@RequestMapping("api/cognito")
@CrossOrigin(origins = "*")
@Tag(name = "CognitoController")
@RequestScope
public class CognitoController {
  private static final Logger LOG = LoggerFactory.getLogger(CognitoController.class);
  private final AWSCognitoClient awsCognitoClient = new AWSCognitoClient();
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @GetMapping(value = "/public/isEmailRegistered")
  public boolean isEmailRegistered(@RequestParam("email") String email) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.COGNITO.isEmailRegistered.GET")) {
      LOG.debug("isEmailRegistered");
      return awsCognitoClient.isEmailRegistered(email);
    }
  }

  @PostMapping(value = "/updateEmailVerified")
  public void updateEmailVerified(HttpServletRequest httpServletRequest, @RequestBody BooleanBody verified) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.COGNITO.updateEmailVerified.POST")) {
      LOG.debug("updateEmailVerified");
      String username = requestObjectService.getRequestAgentName(httpServletRequest);
      awsCognitoClient.updateEmailVerified(username, verified.getValue());
    }
  }
}
