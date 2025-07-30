package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.SmartLifeAuthService;
import org.endeavourhealth.imapi.model.smartlife.OAuthTokenResponse;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "SmartLife Auth Controller")
@RequestScope
@Slf4j
public class SmartLifeAuthController {

  SmartLifeAuthService smartLifeAuthService = new SmartLifeAuthService();

  @PostMapping(value = "oauth/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  @Operation(
    summary = "TODO",
    description = "TODO"
  )
  public OAuthTokenResponse requestCredentials(@RequestParam Map<String, String> request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("Oauth.Token.POST")) {
      log.debug("requestCredentials");
      OAuthTokenResponse a = smartLifeAuthService.getCredentials(request);
      return new OAuthTokenResponse();
    }
  }

  @PostMapping(value = "oauth/revoke")
  @Operation(
    summary = "TODO",
    description = "TODO"
  )
  public void revokeCredentials(@RequestParam Map<String, String> request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("Oauth.Revoke.POST")) {
      log.debug("revokeCredentials");
      smartLifeAuthService.revokeToken(request);
    }
  }
}