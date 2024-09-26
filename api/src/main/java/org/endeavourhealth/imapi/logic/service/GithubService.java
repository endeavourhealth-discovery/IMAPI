package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.controllers.GithubController;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
  public void updateGithubConfig() throws IOException {
    LOG.info("updating github config");
    String owner = "endeavourhealth-discovery";
    String repo = "IMDirectory";
    GithubRelease latestRelease = getLatestReleaseFromGithub(owner, repo);
    List<GithubRelease> allReleases = getAllReleasesFromGithub(owner, repo);
    setGithubLatest(latestRelease);
    setGithubReleases(allReleases);
  }

  private GithubRelease getLatestReleaseFromGithub(String owner, String repo) throws IOException {
    String command = """
      curl -L \\
        -H "Accept: application/vnd.github+json" \\
        -H "Authorization: Bearer %s" \\
        -H "X-GitHub-Api-Version: 2022-11-28" \\
        https://api.github.com/repos/%s/%s/releases/latest
      """.formatted(System.getenv("GITHUB_TOKEN"), owner, repo);
    Process process = Runtime.getRuntime().exec(command);
    InputStream inputStream = process.getInputStream();
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> jsonReleaseMap = mapper.readValue(inputStream, Map.class);
    return processGithubRelease(jsonReleaseMap);
  }

  private List<GithubRelease> getAllReleasesFromGithub(String owner, String repo) throws IOException {
    String command = """
      curl -L \\
        -H "Accept: application/vnd.github+json" \\
        -H "Authorization: Bearer %s" \\
        -H "X-GitHub-Api-Version: 2022-11-28" \\
        https://api.github.com/repos/%s/%s/releases
      """.formatted(System.getenv("GITHUB_TOKEN"), owner, repo);
    Process process = Runtime.getRuntime().exec(command);
    InputStream inputStream = process.getInputStream();
    ObjectMapper mapper = new ObjectMapper();
    List<GithubRelease> results = new ArrayList<>();
    List<LinkedHashMap<String, Object>> jsonReleasesList = mapper.readValue(inputStream, List.class);
    for (LinkedHashMap<String, Object> jsonRelease : jsonReleasesList) {
      results.add(processGithubRelease(jsonRelease));
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

  private GithubRelease processGithubRelease(Map<String,Object> jsonReleaseMap) throws IOException {
    GithubRelease release = new GithubRelease();
    String version = (String) jsonReleaseMap.get("tag_name");
    release.setVersion(version);
    String name = (String) jsonReleaseMap.get("name");
    release.setTitle(name);
    String createdAt = (String) jsonReleaseMap.get("created_at");
    release.setCreatedDate(processDate(createdAt));
    String publishedAt = (String) jsonReleaseMap.get("published_at");
    release.setPublishedDate(processDate(publishedAt));
    String releaseNotes = (String) jsonReleaseMap.get("body");
    release.setReleaseNotes(processReleaseNotes(releaseNotes));
    Object author = jsonReleaseMap.get("author");
    Map<String, String> authorMap = (Map<String, String>) author;
    String authorName = authorMap.get("login");
    release.setAuthor(authorName);
    String url = (String) jsonReleaseMap.get("html_url");
    release.setUrl(url);
    return release;
  }
}
