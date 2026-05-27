package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.customexceptions.ConfigException;
import org.endeavourhealth.imapi.model.github.GithubDTO;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.interfacemanager.model.CONFIG;
import org.endeavourhealth.interfacemanager.model.REPO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GithubService {
  final ConfigManager configManager = new ConfigManager();

  public GithubRelease getGithubLatestRelease(REPO repo) throws ConfigException, JsonProcessingException {
    CONFIG url = getLatestReleaseUrlFromRepo(repo);
    GithubRelease config = configManager.getConfig(url, new TypeReference<>() {
    });
    if (null == config)
      throw new ConfigException("Github latest release config not found.");
    return config;
  }

  private void setGithubLatest(REPO repo, GithubRelease githubRelease) throws JsonProcessingException {
    Config config = getLatestReleaseConfigFromRepo(repo);
    ObjectMapper mapper = new ObjectMapper();
    String gitHubReleaseJson = mapper.writeValueAsString(githubRelease);
    config.setData(gitHubReleaseJson);
    CONFIG url = getLatestReleaseUrlFromRepo(repo);
    configManager.setConfig(url, config);
  }

  public List<GithubRelease> getGithubReleases(REPO repo) throws JsonProcessingException, ConfigException {
    CONFIG url = getAllReleasesUrlFromRepo(repo);
    List<GithubRelease> config = configManager.getConfig(url, new TypeReference<>() {
    });
    if (null == config)
      throw new ConfigException("Github release config not found.");
    return config;
  }

  private CONFIG getLatestReleaseUrlFromRepo(REPO repo) {
    return switch (repo) {
      case REPO.IM_DIRECTORY -> CONFIG.IMDIRECTORY_LATEST_RELEASE;
      case REPO.IM_QUERY_RUNNER -> CONFIG.IMQUERY_RUNNER_LATEST_RELEASE;
    };
  }

  private CONFIG getAllReleasesUrlFromRepo(REPO repo) {
    return switch (repo) {
      case REPO.IM_DIRECTORY -> CONFIG.IMDIRECTORY_ALL_RELEASES;
      case REPO.IM_QUERY_RUNNER -> CONFIG.IMQUERY_RUNNER_ALL_RELEASES;
    };
  }

  private Config getLatestReleaseConfigFromRepo(REPO repo) {
    return switch (repo) {
      case REPO.IM_DIRECTORY ->
        new Config().setName("IMDirectory latest release").setComment("Latest github release details for IMDirectory repository");
      case REPO.IM_QUERY_RUNNER ->
        new Config().setName("QueryRunner latest release").setComment("Latest github release details for QueryRunner");
    };
  }

  private Config getAllReleasesConfigFromRepo(REPO repo) {
    return switch (repo) {
      case REPO.IM_DIRECTORY ->
        new Config().setName("IMDirectory all releases").setComment("All github release details for IMDirectory repository");
      case REPO.IM_QUERY_RUNNER ->
        new Config().setName("QueryRunner all releases").setComment("All github release details for QueryRunner repository");
    };
  }

  private void setGithubReleases(REPO repo, List<GithubRelease> githubReleases) throws JsonProcessingException {
    Config config = getAllReleasesConfigFromRepo(repo);
    ObjectMapper mapper = new ObjectMapper();
    String gitHubReleaseJson = mapper.writeValueAsString(githubReleases);
    config.setData(gitHubReleaseJson);
    CONFIG url = getAllReleasesUrlFromRepo(repo);
    configManager.setConfig(url, config);
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void updateAllGithubConfigs() throws IOException, InterruptedException {
    updateGithubConfig(REPO.IM_DIRECTORY);
    updateGithubConfig(REPO.IM_QUERY_RUNNER);
  }

  public void updateGithubConfig(REPO repo) throws IOException, InterruptedException {
    log.info("updating github config");
    String owner = "endeavourhealth-discovery";
    GithubRelease latestRelease = getLatestReleaseFromGithub(owner, repo);
    List<GithubRelease> allReleases = getAllReleasesFromGithub(owner, repo);
    setGithubLatest(repo, latestRelease);
    setGithubReleases(repo, allReleases);
  }

  @PostConstruct
  private void updateGithubConfigOnStart() throws IOException, InterruptedException {
    if ("production".equals(System.getenv("MODE"))) {
      updateAllGithubConfigs();
    }
  }

  private GithubRelease getLatestReleaseFromGithub(String owner, REPO repo) throws IOException, InterruptedException {
    HttpResponse<String> response;
    try (HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build()) {

      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.github.com/repos/%s/%s/releases/latest".formatted(owner, repo.toString())))
        .GET()
        .setHeader("Accept", "application/vnd.github+json")
        .setHeader("Authorization", "Bearer %s".formatted(System.getenv("GITHUB_TOKEN")))
        .setHeader("X-GitHub-Api-Version", "2022-11-28")
        .build();

      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    GithubDTO jsonReleaseMap = mapper.readValue(response.body(), GithubDTO.class);
    return processGithubRelease(jsonReleaseMap);
  }

  private List<GithubRelease> getAllReleasesFromGithub(String owner, REPO repo) throws IOException, InterruptedException {
    HttpResponse<String> response;
    try (HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build()) {

      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.github.com/repos/%s/%s/releases".formatted(owner, repo.toString())))
        .GET()
        .setHeader("Accept", "application/vnd.github+json")
        .setHeader("Authorization", "Bearer %s".formatted(System.getenv("GITHUB_TOKEN")))
        .setHeader("X-GitHub-Api-Version", "2022-11-28")
        .build();

      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    List<GithubRelease> results = new ArrayList<>();
    List<GithubDTO> githubDTOList = mapper.readValue(response.body(), new TypeReference<>() {
    });
    for (GithubDTO githubDTO : githubDTOList) {
      results.add(processGithubRelease(githubDTO));
    }
    return results;
  }

  private List<String> processReleaseNotes(String releaseNotes) {
    if (releaseNotes == null || releaseNotes.isEmpty())
      return new ArrayList<>();

    String[] lines = releaseNotes.split(System.lineSeparator());
    return Arrays.asList(lines);
  }

  private String processDate(String date) {
    if (date == null || date.isEmpty())
      return "";

    DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    LocalDateTime dateTime = LocalDateTime.parse(date, formatterInput);
    DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    return dateTime.format(formatterOutput);
  }

  private GithubRelease processGithubRelease(GithubDTO githubDTO) {
    GithubRelease release = new GithubRelease()
      .setVersion(githubDTO.getTag_name())
      .setTitle(githubDTO.getName())
      .setCreatedDate(processDate(githubDTO.getCreated_at()))
      .setPublishedDate(processDate(githubDTO.getPublished_at()))
      .setReleaseNotes(processReleaseNotes(githubDTO.getBody()))
      .setUrl(githubDTO.getHtml_url());

    if (githubDTO.getAuthor() != null)
      release.setAuthor(githubDTO.getAuthor().getLogin());

    return release;
  }
}
