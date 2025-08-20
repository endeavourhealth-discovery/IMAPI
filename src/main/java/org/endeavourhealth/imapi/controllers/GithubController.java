package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.GithubService;
import org.endeavourhealth.imapi.model.customexceptions.ConfigException;
import org.endeavourhealth.imapi.model.github.GithubRelease;
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

  final GithubService githubService = new GithubService();

  @Operation(summary = "Retrieve the latest GitHub release", description = "Gets the latest release information from the GitHub repository.")
  @GetMapping(value = "public/githubLatest")
  public GithubRelease getLatestRelease() throws IOException, ConfigException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubLatest.GET")) {
      log.debug("getGithubLatest");
      return githubService.getGithubLatestRelease();
    }
  }

  @Operation(summary = "Retrieve all GitHub releases", description = "Gets a list of all releases available in the GitHub repository.")
  @GetMapping(value = "public/githubAllReleases")
  public List<GithubRelease> getReleases() throws IOException, ConfigException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubReleases.GET")) {
      log.debug("getGithubReleases");
      return githubService.getGithubReleases();
    }
  }

  @Operation(summary = "Update GitHub configuration", description = "Triggers an update to the GitHub repository configuration.")
  @PostMapping(value = "/updateGithubConfig")
  public void updateGithubConfig() throws IOException, InterruptedException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubConfig.UPDATE")) {
      log.debug("updateGithubConfig");
      githubService.updateGithubConfig();
    }
  }
}
