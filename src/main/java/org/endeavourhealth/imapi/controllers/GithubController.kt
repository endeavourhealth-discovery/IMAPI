package org.endeavourhealth.imapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.logic.service.GithubService
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.customexceptions.ConfigException
import org.endeavourhealth.imapi.model.github.GithubRelease
import org.endeavourhealth.imapi.model.github.REPO
import org.endeavourhealth.imapi.model.postRequestPrimatives.REPOBody
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.security.Permission
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope
import java.io.IOException
import java.util.List

@RestController
@RequestMapping("api/github")
@CrossOrigin(origins = ["*"])
@Tag(name = "GithubController")
class GithubController (
    private val githubService: GithubService,
    private val securityService: SecurityService
)
{
  private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Retrieve the latest GitHub release", description = "Gets the latest release information from the GitHub repository.")
    @GetMapping(value = ["/public/githubLatest"])
    @Throws(IOException::class, ConfigException::class)
    fun getLatestRelease(@RequestParam(name = "repositoryName") repo: REPO): GithubRelease {
        MetricsHelper.recordTime("API.Config.githubLatest.GET").use {
            log.debug("getGithubLatest")
            return githubService.getGithubLatestRelease(repo)
        }
    }

    @Operation(summary = "Retrieve all GitHub releases", description = "Gets a list of all releases available in the GitHub repository.")
    @GetMapping(value = ["/public/githubAllReleases"])
    @Throws(IOException::class, ConfigException::class)
    fun getReleases(@RequestParam(name = "repositoryName") repo: REPO): MutableList<GithubRelease> {
        MetricsHelper.recordTime("API.Config.githubReleases.GET").use {
            log.debug("getGithubReleases")
            return githubService.getGithubReleases(repo)
        }
    }

    @Operation(summary = "Update GitHub configuration", description = "Triggers an update to the GitHub repository configuration.")
    @PostMapping(value = ["/private/updateGithubConfig"])
    @Throws(IOException::class, InterruptedException::class)
    fun updateGithubConfig(request: HttpServletRequest, @RequestBody repoBody: REPOBody) {
        MetricsHelper.recordTime("API.Config.githubConfig.UPDATE").use {
            log.debug("updateGithubConfig")
            securityService.requiresPermission(Permission(Resource.GITHUB, listOf(UserRole.ADMIN), mutableListOf<NamespacePermission>()), request)
            githubService.updateGithubConfig(repoBody.repo)
        }
    }
}
