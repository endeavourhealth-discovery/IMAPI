package org.endeavourhealth.imapi.logic.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.config.ConfigManager
import org.endeavourhealth.imapi.model.config.Config
import org.endeavourhealth.imapi.model.customexceptions.ConfigException
import org.endeavourhealth.imapi.model.github.GithubDTO
import org.endeavourhealth.imapi.model.github.GithubRelease
import org.endeavourhealth.imapi.model.github.REPO
import org.endeavourhealth.imapi.utility.SharedObjectMapper
import org.endeavourhealth.imapi.vocabulary.CONFIG
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Slf4j
@Component
class GithubService {
  val configManager: ConfigManager = ConfigManager()
  private val log = LoggerFactory.getLogger(javaClass)

  @Throws(ConfigException::class, JsonProcessingException::class)
  fun getGithubLatestRelease(repo: REPO): GithubRelease {
    val url = getLatestReleaseUrlFromRepo(repo)
    val config = configManager.getConfig(url, object : TypeReference<GithubRelease>() {
    })
    if (null == config) throw ConfigException("Github latest release config not found.")
    return config
  }

  @Throws(JsonProcessingException::class)
  private fun setGithubLatest(repo: REPO, githubRelease: GithubRelease?) {
    val config = getLatestReleaseConfigFromRepo(repo)
    val mapper = SharedObjectMapper.INSTANCE
    val gitHubReleaseJson = mapper.writeValueAsString(githubRelease)
    config.setData(gitHubReleaseJson)
    val url = getLatestReleaseUrlFromRepo(repo)
    configManager.setConfig(url, config)
  }

  @Throws(JsonProcessingException::class, ConfigException::class)
  fun getGithubReleases(repo: REPO): MutableList<GithubRelease> {
    val url = getAllReleasesUrlFromRepo(repo)
    val config = configManager.getConfig(url, object : TypeReference<MutableList<GithubRelease>>() {
    })
    if (null == config) throw ConfigException("Github release config not found.")
    return config
  }

  private fun getLatestReleaseUrlFromRepo(repo: REPO): CONFIG {
    return when (repo) {
      REPO.IM_DIRECTORY -> CONFIG.IMDIRECTORY_LATEST_RELEASE
      REPO.IM_QUERY_RUNNER -> CONFIG.IMQUERY_RUNNER_LATEST_RELEASE
    }
  }

  private fun getAllReleasesUrlFromRepo(repo: REPO): CONFIG {
    return when (repo) {
      REPO.IM_DIRECTORY -> CONFIG.IMDIRECTORY_ALL_RELEASES
      REPO.IM_QUERY_RUNNER -> CONFIG.IMQUERY_RUNNER_ALL_RELEASES
    }
  }

  private fun getLatestReleaseConfigFromRepo(repo: REPO): Config {
    return when (repo) {
      REPO.IM_DIRECTORY -> Config().setName("IMDirectory latest release")
        .setComment("Latest github release details for IMDirectory repository")

      REPO.IM_QUERY_RUNNER -> Config().setName("QueryRunner latest release")
        .setComment("Latest github release details for QueryRunner")
    }
  }

  private fun getAllReleasesConfigFromRepo(repo: REPO): Config {
    return when (repo) {
      REPO.IM_DIRECTORY -> Config().setName("IMDirectory all releases")
        .setComment("All github release details for IMDirectory repository")

      REPO.IM_QUERY_RUNNER -> Config().setName("QueryRunner all releases")
        .setComment("All github release details for QueryRunner repository")
    }
  }

  @Throws(JsonProcessingException::class)
  private fun setGithubReleases(repo: REPO, githubReleases: MutableList<GithubRelease>?) {
    val config = getAllReleasesConfigFromRepo(repo)
    val mapper = SharedObjectMapper.INSTANCE
    val gitHubReleaseJson = mapper.writeValueAsString(githubReleases)
    config.setData(gitHubReleaseJson)
    val url = getAllReleasesUrlFromRepo(repo)
    configManager.setConfig(url, config)
  }

  @Scheduled(cron = "0 0 0 * * *")
  @Throws(IOException::class, InterruptedException::class)
  fun updateAllGithubConfigs() {
    updateGithubConfig(REPO.IM_DIRECTORY)
    updateGithubConfig(REPO.IM_QUERY_RUNNER)
  }

  @Throws(IOException::class, InterruptedException::class)
  fun updateGithubConfig(repo: REPO) {
    log.info("updating github config")
    val owner = "endeavourhealth-discovery"
    val latestRelease = getLatestReleaseFromGithub(owner, repo)
    val allReleases = getAllReleasesFromGithub(owner, repo)
    setGithubLatest(repo, latestRelease)
    setGithubReleases(repo, allReleases)
  }

  @PostConstruct
  @Throws(IOException::class, InterruptedException::class)
  private fun updateGithubConfigOnStart() {
    if ("production" == System.getenv("MODE")) {
      updateAllGithubConfigs()
    }
  }

  @Throws(IOException::class, InterruptedException::class)
  private fun getLatestReleaseFromGithub(owner: String, repo: REPO): GithubRelease {
    val response: HttpResponse<String>
    HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build().use { client ->
        val request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.github.com/repos/$owner/$repo/releases/latest"))
          .GET()
          .setHeader("Accept", "application/vnd.github+json")
          .setHeader("Authorization", "Bearer ${System.getenv("GITHUB_TOKEN")}")
          .setHeader("X-GitHub-Api-Version", "2022-11-28")
          .build()
        response = client.send(request, HttpResponse.BodyHandlers.ofString())
      }
    val mapper = ObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val jsonReleaseMap = mapper.readValue(response.body(), GithubDTO::class.java)
    return processGithubRelease(jsonReleaseMap)
  }

  @Throws(IOException::class, InterruptedException::class)
  private fun getAllReleasesFromGithub(owner: String, repo: REPO): MutableList<GithubRelease> {
    val response: HttpResponse<String>
    HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build().use { client ->
        val request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.github.com/repos/$owner/$repo/releases"))
          .GET()
          .setHeader("Accept", "application/vnd.github+json")
          .setHeader("Authorization", "Bearer ${System.getenv("GITHUB_TOKEN")}")
          .setHeader("X-GitHub-Api-Version", "2022-11-28")
          .build()
        response = client.send(request, HttpResponse.BodyHandlers.ofString())
      }
    val mapper = ObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val results: MutableList<GithubRelease> = ArrayList<GithubRelease>()
    val githubDTOList: MutableList<GithubDTO> =
      mapper.readValue(response.body(), object : TypeReference<MutableList<GithubDTO>>() {
      })
    for (githubDTO in githubDTOList) {
      results.add(processGithubRelease(githubDTO))
    }
    return results
  }

  private fun processReleaseNotes(releaseNotes: String?): MutableList<String> {
    if (releaseNotes.isNullOrEmpty()) return mutableListOf()

    val lines: Array<String> =
      releaseNotes.split(System.lineSeparator().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return lines.toMutableList()
  }

  private fun processDate(date: String?): String {
    if (date.isNullOrEmpty()) return ""

    val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val dateTime = LocalDateTime.parse(date, formatterInput)
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    return dateTime.format(formatterOutput)
  }

  private fun processGithubRelease(githubDTO: GithubDTO): GithubRelease {
    val release = GithubRelease()
      .setVersion(githubDTO.tag_name)
      .setTitle(githubDTO.name)
      .setCreatedDate(processDate(githubDTO.created_at))
      .setPublishedDate(processDate(githubDTO.published_at))
      .setReleaseNotes(processReleaseNotes(githubDTO.body))
      .setUrl(githubDTO.html_url)

    if (githubDTO.author != null) release.setAuthor(githubDTO.author.login)

    return release
  }
}
