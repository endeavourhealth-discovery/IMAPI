package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.controllers.GithubController;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.github.GithubDTO;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class GithubService {
  private static final Logger LOG = LoggerFactory.getLogger(GithubController.class.getName());
  ConfigManager configManager = new ConfigManager();
  public GithubRelease getGithubLatestRelease() throws JsonProcessingException {
    return configManager.getConfig(CONFIG.IMDIRECTORY_LATEST_RELEASE, new TypeReference<>() {
    });
  }

  private void setGithubLatest(GithubRelease githubRelease) throws JsonProcessingException {
    Config config = new Config();
    config.setName("IMDirectory latest release");
    config.setComment("Latest github release details for IMDirectory repository");
    ObjectMapper mapper = new ObjectMapper();
    String gitHubReleaseJson = mapper.writeValueAsString(githubRelease);
    config.setData(gitHubReleaseJson);
    configManager.setConfig(CONFIG.IMDIRECTORY_LATEST_RELEASE, config);
  }

  public List<GithubRelease> getGithubReleases() throws JsonProcessingException {
    return configManager.getConfig(CONFIG.IMDIRECTORY_ALL_RELEASES, new TypeReference<>() {
    });
  }

  private void setGithubReleases(List<GithubRelease> githubReleases) throws JsonProcessingException {
    Config config = new Config();
    config.setName("IMDirectory all releases");
    config.setComment("All github release details for IMDirectory repository");
    ObjectMapper mapper = new ObjectMapper();
    String gitHubReleaseJson = mapper.writeValueAsString(githubReleases);
    config.setData(gitHubReleaseJson);
    configManager.setConfig(CONFIG.IMDIRECTORY_ALL_RELEASES, config);
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void updateGithubConfig() throws IOException, InterruptedException {
    LOG.info("updating github config");
    String owner = "endeavourhealth-discovery";
    String repo = "IMDirectory";
    GithubRelease latestRelease = getLatestReleaseFromGithub(owner, repo);
    List<GithubRelease> allReleases = getAllReleasesFromGithub(owner, repo);
    setGithubLatest(latestRelease);
    setGithubReleases(allReleases);
  }

  private GithubRelease getLatestReleaseFromGithub(String owner, String repo) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build();

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.github.com/repos/%s/%s/releases/latest".formatted(owner, repo)))
      .GET()
      .setHeader("Accept", "application/vnd.github+json")
      .setHeader("Authorization", "Bearer %s".formatted(System.getenv("GITHUB_TOKEN")))
      .setHeader("X-GitHub-Api-Version", "2022-11-28")
      .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    ObjectMapper mapper = new ObjectMapper();
    GithubDTO jsonReleaseMap = mapper.readValue(response.body(), GithubDTO.class);
    return processGithubRelease(jsonReleaseMap);
  }

  private List<GithubRelease> getAllReleasesFromGithub(String owner, String repo) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build();

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://api.github.com/repos/%s/%s/releases".formatted(owner, repo)))
      .GET()
      .setHeader("Accept", "application/vnd.github+json")
      .setHeader("Authorization", "Bearer %s".formatted(System.getenv("GITHUB_TOKEN")))
      .setHeader("X-GitHub-Api-Version", "2022-11-28")
      .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    ObjectMapper mapper = new ObjectMapper();
    List<GithubRelease> results = new ArrayList<>();
    List<GithubDTO> githubDTOList = mapper.readValue(response.body(), new TypeReference<List<GithubDTO>>() {});
    for (GithubDTO githubDTO : githubDTOList) {
      results.add(processGithubRelease(githubDTO));
    }
    return results;
  }

  private List<String> processReleaseNotes(String releaseNotes) throws JsonProcessingException {
    String[] lines = releaseNotes.split(System.lineSeparator());
    return Arrays.asList(lines);
  }

  private String processDate(String date) {
    DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    LocalDateTime dateTime = LocalDateTime.parse(date, formatterInput);
    DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    return dateTime.format(formatterOutput);
  }

  private GithubRelease processGithubRelease(GithubDTO githubDTO) throws IOException {
    return new GithubRelease()
      .setVersion(githubDTO.getTag_name())
      .setTitle(githubDTO.getName())
      .setCreatedDate(processDate(githubDTO.getCreated_at()))
      .setPublishedDate(processDate(githubDTO.getPublished_at()))
      .setReleaseNotes(processReleaseNotes(githubDTO.getBody()))
      .setAuthor(githubDTO.getAuthor().getLogin())
      .setUrl(githubDTO.getHtml_url());
  }
}
