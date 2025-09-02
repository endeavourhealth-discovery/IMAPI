package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.SmartLifeAuthService;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "SmartLife Auth Controller")
@RequestScope
@Slf4j
public class SmartLifeAuthController {

  final SmartLifeAuthService smartLifeAuthService = new SmartLifeAuthService();

  @PostMapping(value = "oauth/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  @Operation(
    summary = "TODO",
    description = "TODO"
  )
  public String requestCredentials(@RequestParam Map<String, String> request) throws IOException, InterruptedException {
    try (MetricsTimer t = MetricsHelper.recordTime("Oauth.Token.POST")) {
      log.debug("requestCredentials");
      String client_id = request.get("client_id");
      String client_secret = request.get("client_secret");
      HttpResponse<String> response = smartLifeAuthService.getCredentials(client_id, client_secret);

      log.info("response: {}", response.body());

      return response.body();
    }
  }

  @PostMapping(value = "oauth/revoke")
  @Operation(
    summary = "TODO",
    description = "TODO"
  )
  public void revokeCredentials(@RequestParam Map<String, String> request) {
    try (MetricsTimer t = MetricsHelper.recordTime("Oauth.Revoke.POST")) {
      log.debug("revokeCredentials");
      smartLifeAuthService.revokeToken(request);
    }
  }
}