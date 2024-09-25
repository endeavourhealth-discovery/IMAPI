package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.logic.service.GithubService;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/github")
@CrossOrigin(origins = "*")
@Tag(name = "GithubController")
@RequestScope
public class GithubController {
  private static final Logger LOG = LoggerFactory.getLogger(GithubController.class.getName());

  GithubService githubService = new GithubService();

  @GetMapping(value = "public/githubLatest")
  public GithubRelease getLatestRelease() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubLatest.GET")) {
      LOG.debug("getGithubLatest");
      return githubService.getGithubLatestRelease();
    }
  }

  @GetMapping(value = "public/githubAllReleases")
  public List<GithubRelease> getReleases() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubReleases.GET")) {
      LOG.debug("getGithubReleases");
      return githubService.getGithubReleases();
    }
  }

  @GetMapping(value = "/updateGithubConfig")
  @PreAuthorize("hasAuthority('IMAdmin')")
  public void updateGithubConfig() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubConfig.UPDATE")) {
      LOG.debug("updateGithubConfig");
      githubService.updateGithubConfig();
    }
  }
}
