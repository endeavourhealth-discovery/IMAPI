package org.endeavourhealth.imapi.logic.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.endeavourhealth.imapi.config.ConfigManager
import org.endeavourhealth.imapi.model.config.Config
import org.endeavourhealth.imapi.model.customexceptions.ConfigException
import org.endeavourhealth.imapi.model.github.GithubDTO
import org.endeavourhealth.imapi.model.github.GithubRelease
import org.endeavourhealth.imapi.model.github.REPO
import org.endeavourhealth.imapi.vocabulary.CONFIG
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class GithubService {
  private val configManager = ConfigManager()
  private val log = LoggerFactory.getLogger(javaClass)
  private val objectMapper =
    ObjectMapper().apply { configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }
  private val httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()

  @Throws(ConfigException::class, JsonProcessingException::class)
  fun getGithubLatestRelease(repo: REPO): GithubRelease {
    val config = configManager.getConfig(getLatestReleaseUrl(repo), object : TypeReference<GithubRelease>() {
    })
    return config ?: throw ConfigException("Github latest release config not found.")
  }

  @Throws(JsonProcessingException::class)
  private fun setGithubLatest(repo: REPO, githubRelease: GithubRelease) {
    val config = getLatestReleaseConfig(repo).setData(objectMapper.writeValueAsString(githubRelease))
    val url = getLatestReleaseUrl(repo)
    configManager.setConfig(url, config)
  }

  @Throws(JsonProcessingException::class, ConfigException::class)
  fun getGithubReleases(repo: REPO): MutableList<GithubRelease> {
    val config = configManager.getConfig(getAllReleasesUrl(repo), object : TypeReference<MutableList<GithubRelease>>() {
    })
    return config ?: throw ConfigException("Github releases config not found.")
  }

  private fun getLatestReleaseUrl(repo: REPO): CONFIG =
    when (repo) {
      REPO.IM_DIRECTORY -> CONFIG.IMDIRECTORY_LATEST_RELEASE
      REPO.IM_QUERY_RUNNER -> CONFIG.IMQUERY_RUNNER_LATEST_RELEASE
    }

  private fun getAllReleasesUrl(repo: REPO): CONFIG =
    when (repo) {
      REPO.IM_DIRECTORY -> CONFIG.IMDIRECTORY_ALL_RELEASES
      REPO.IM_QUERY_RUNNER -> CONFIG.IMQUERY_RUNNER_ALL_RELEASES
    }

  private fun getLatestReleaseConfig(repo: REPO): Config =
    when (repo) {
      REPO.IM_DIRECTORY -> Config().setName("IMDirectory latest release")
        .setComment("Latest github release details for IMDirectory repository")

      REPO.IM_QUERY_RUNNER -> Config().setName("QueryRunner latest release")
        .setComment("Latest github release details for QueryRunner")
    }

  private fun getAllReleasesConfigFromRepo(repo: REPO): Config =
    when (repo) {
      REPO.IM_DIRECTORY -> Config().setName("IMDirectory all releases")
        .setComment("All github release details for IMDirectory repository")

      REPO.IM_QUERY_RUNNER -> Config().setName("QueryRunner all releases")
        .setComment("All github release details for QueryRunner repository")
    }

  @Throws(JsonProcessingException::class)
  private fun setGithubReleases(repo: REPO, githubReleases: List<GithubRelease>) {
    val config = getAllReleasesConfigFromRepo(repo).setData(objectMapper.writeValueAsString(githubReleases))
    configManager.setConfig(getAllReleasesUrl(repo), config)
  }

  @Scheduled(cron = "0 0 0 * * *")
  @Throws(IOException::class, InterruptedException::class)
  fun updateAllGithubConfigs() {
    REPO.entries.forEach(::updateGithubConfig)
  }

  @Throws(IOException::class, InterruptedException::class)
  fun updateGithubConfig(repo: REPO) {
    log.info("Updating github config for {}", repo)
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
  private fun getLatestReleaseFromGithub(owner: String, repo: REPO): GithubRelease = getFromGithub(
    "/repos/$owner/$repo/releases/latest",
    object : TypeReference<GithubDTO>() {}).let(::processGithubRelease)

  @Throws(IOException::class, InterruptedException::class)
  private fun getAllReleasesFromGithub(owner: String, repo: REPO): List<GithubRelease> =
    getFromGithub(
      "/repos/$owner/$repo/releases",
      object : TypeReference<MutableList<GithubDTO>>() {}).map(::processGithubRelease)

  @Throws(IOException::class, InterruptedException::class)
  private fun <T> getFromGithub(path: String, type: TypeReference<T>): T {
    val request = HttpRequest.newBuilder()
      .uri(URI.create("$GITHUB_API$path"))
      .GET()
      .header("ACCEPT", "application/vnd.github+json")
      .header("Authorization", "Bearer ${System.getenv("GITHUB_TOKEN")}")
      .header("X-GitHub-Api-Version", "2022-11-28")
      .build()

    val response: HttpResponse<String?> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    if (response.statusCode() !in 200..299) {
      throw IOException("Github API request failed: ${response.statusCode()}")
    }
    return objectMapper.readValue(response.body(), type)
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

  private fun processGithubRelease(githubDTO: GithubDTO): GithubRelease = GithubRelease()
    .setVersion(githubDTO.tag_name)
    .setTitle(githubDTO.name)
    .setCreatedDate(processDate(githubDTO.created_at))
    .setPublishedDate(processDate(githubDTO.published_at))
    .setReleaseNotes(processReleaseNotes(githubDTO.body))
    .setUrl(githubDTO.html_url)
    .apply { githubDTO.author?.let { setAuthor(it.login) } }

  companion object {
    private const val GITHUB_API = "https://api.github.com/"
  }
}
