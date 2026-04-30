package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.GithubService;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.customexceptions.ConfigException;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.imapi.model.github.REPO;
import org.endeavourhealth.imapi.model.security.Permission;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/github")
@CrossOrigin(origins = "*")
@Tag(name = "GithubController")
@RequestScope
@Slf4j
public class GithubController {

  private final GithubService githubService = new GithubService();
  private final SecurityService securityService = new SecurityService();

  @Operation(summary = "Retrieve the latest GitHub release", description = "Gets the latest release information from the GitHub repository.")
  @GetMapping(value = "/public/githubLatest")
  public GithubRelease getLatestRelease(@RequestParam(name = "repositoryName") REPO repo) throws IOException, ConfigException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubLatest.GET")) {
      log.debug("getGithubLatest");
      return githubService.getGithubLatestRelease(repo);
    }
  }

  @Operation(summary = "Retrieve all GitHub releases", description = "Gets a list of all releases available in the GitHub repository.")
  @GetMapping(value = "/public/githubAllReleases")
  public List<GithubRelease> getReleases(@RequestParam(name = "repositoryName") REPO repo) throws IOException, ConfigException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubReleases.GET")) {
      log.debug("getGithubReleases");
      return githubService.getGithubReleases(repo);
    }
  }

  @Operation(summary = "Update GitHub configuration", description = "Triggers an update to the GitHub repository configuration.")
  @PostMapping(value = "/private/updateGithubConfig")
  public void updateGithubConfig(HttpServletRequest request, @RequestBody REPO repo) throws IOException, InterruptedException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubConfig.UPDATE")) {
      log.debug("updateGithubConfig");
      securityService.requiresPermission(new Permission(Resource.GITHUB, List.of(UserRole.ADMIN), List.of()), request);
      githubService.updateGithubConfig(repo);
    }
  }
}
