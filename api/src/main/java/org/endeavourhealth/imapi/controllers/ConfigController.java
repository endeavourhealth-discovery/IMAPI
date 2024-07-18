package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.config.DashboardLayout;
import org.endeavourhealth.imapi.model.config.FilterDefault;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Tag(name="ConfigController")
@RequestScope
public class ConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    ConfigManager configManager;

    @GetMapping(value = "/public/componentLayout")
    public List<ComponentLayoutItem> getComponentLayout(@RequestParam(name="name") String name) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.ComponentLayout.GET")) {
            LOG.debug("getComponentLayout");
            if ("definition".equals(name))
                return configManager.getConfig(CONFIG.DEFINITION, new TypeReference<>() {
                });
            if ("summary".equals(name))
                return configManager.getConfig(CONFIG.SUMMARY, new TypeReference<>() {
                });
            else
                throw new IllegalArgumentException("Unknown component layout config");
        }
    }

    @GetMapping(value = "public/monitoring")
    public String getMonitoring() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.Monitoring.GET")) {
            LOG.debug("getMonitoring");
            return configManager.getConfig(CONFIG.MONITORING, new TypeReference<>() {});
        }
    }

    @GetMapping(value = "public/githubLatest")
    public GithubRelease getLatestRelease() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubLatest.GET")) {
            LOG.debug("getGithubLatest");
            return configManager.getConfig(CONFIG.IMDIRECTORY_LATEST_RELEASE, new TypeReference<>() {
            });
        }
    }

    @PostMapping(value = "public/githubLatest")
    public void setGithubLatest(@RequestBody GithubRelease githubRelease) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubLatest.POST")) {
            LOG.debug("setGithubLatest");
            Config config = new Config();
            config.setName("IMDirectory latest release");
            config.setComment("Latest github release details for IMDirectory repository");
            ObjectMapper mapper = new ObjectMapper();
            String gitHubReleaseJson = mapper.writeValueAsString(githubRelease);
            config.setData(gitHubReleaseJson);
            configManager.setConfig(CONFIG.IMDIRECTORY_LATEST_RELEASE,config);
        }
    }

    @GetMapping(value = "public/githubAllReleases")
    public List<GithubRelease> getReleases() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubReleases.GET")) {
            LOG.debug("getGithubReleases");
            return configManager.getConfig(CONFIG.IMDIRECTORY_ALL_RELEASES, new TypeReference<>() {
            });
        }
    }

    @PostMapping(value = "public/githubAllReleases")
    public void setReleases(@RequestBody List<GithubRelease> githubReleases) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.githubReleases.POST")) {
            LOG.debug("setGithubReleases");
            Config config = new Config();
            config.setName("IMDirectory all releases");
            config.setComment("All github release details for IMDirectory repository");
            ObjectMapper mapper = new ObjectMapper();
            String gitHubReleaseJson = mapper.writeValueAsString(githubReleases);
            config.setData(gitHubReleaseJson);
            configManager.setConfig(CONFIG.IMDIRECTORY_ALL_RELEASES, config);
        }
    }

    @GetMapping(value="/public/filterDefaults")
    public FilterDefault getFilterDefaults() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.FilterDefaults.GET")) {
            LOG.debug("getFilterDefaults");
            return configManager.getConfig(CONFIG.FILTER_DEFAULTS, new TypeReference<>() {
            });
        }
    }

    @GetMapping(value="/public/defaultPredicateNames")
    public Map<String, String> getDefaultPredicateNames() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.DefaultPredicateNames.GET")) {
            LOG.debug("getDefaultPredicateNames");
            return configManager.getConfig(CONFIG.DEFAULT_PREDICATE_NAMES, new TypeReference<>() {
            });
        }
    }

    @GetMapping(value="/public/xmlSchemaDataTypes")
    public List<String> getXMLSchemaDataTypes() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.XmlSchemaDataTypes.GET")) {
            LOG.debug("getXMLSchemaDataTypes");
            return configManager.getConfig(CONFIG.XML_SCHEMA_DATA_TYPES, new TypeReference<>() {
            });
        }
    }

    @GetMapping(value="/public/graphExcludePredicates")
    public List<String> getGraphExcludePredicates() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Config.GraphExcludePredicates.GET")) {
            LOG.debug("getGraphExcludePredicates");
            return configManager.getConfig(CONFIG.GRAPH_EXCLUDE_PREDICATES, new TypeReference<>() {
            });
        }
    }
}
