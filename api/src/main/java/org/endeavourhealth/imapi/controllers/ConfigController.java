package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.config.DashboardLayout;
import org.endeavourhealth.imapi.model.config.FilterDefault;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Api(value="ConfigController")
@SwaggerDefinition(tags = {
    @Tag(name = "Config Controller", description = "IM application configuration endpoint")
})
@RequestScope
public class ConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    ConfigManager configManager;

    @GetMapping(value = "/public/componentLayout")
    public List<ComponentLayoutItem> getComponentLayout(
            @RequestParam(name="name") String name
    ) throws JsonProcessingException {
        LOG.debug("getComponentLayout");
        if ("definition".equals(name))
            return configManager.getConfig(CONFIG.DEFINITION, new TypeReference<>(){});
        if ("summary".equals(name))
            return configManager.getConfig(CONFIG.SUMMARY, new TypeReference<>(){});
        else
            throw new IllegalArgumentException("Unknown component layout config");
    }

    @GetMapping(value="/public/filterDefaults")
    public FilterDefault getFilterDefaults() throws JsonProcessingException {
        LOG.debug("getFilterDefaults");
        return configManager.getConfig(CONFIG.FILTER_DEFAULTS, new TypeReference<>(){});
    }

    @GetMapping(value="/public/dashboardLayout")
    public List<DashboardLayout> getDashboardLayout(
            @RequestParam(name ="name") String name
    ) throws JsonProcessingException {
        LOG.debug("getDashboardLayout");
        if ("conceptDashboard".equals(name))
            return configManager.getConfig(CONFIG.CONCEPT_DASHBOARD, new TypeReference<>(){});
        else
            throw new IllegalArgumentException("Unknown dashboard layout config");
    }

    @GetMapping(value="/public/defaultPredicateNames")
    public Map<String, String> getDefaultPredicateNames() throws JsonProcessingException {
        LOG.debug("getDefaultPredicateNames");
        return configManager.getConfig(CONFIG.DEFAULT_PREDICATE_NAMES, new TypeReference<>() {});
    }

    @GetMapping(value="/public/xmlSchemaDataTypes")
    public List<String> getXMLSchemaDataTypes() throws JsonProcessingException {
        LOG.debug("getXMLSchemaDataTypes");
        return configManager.getConfig(CONFIG.XML_SCHEMA_DATATYPES, new TypeReference<>() {});
    }

    @GetMapping(value="/public/graphExcludePredicates")
    public List<String> getGraphExcludePredicates() throws JsonProcessingException {
        LOG.debug("getGraphExcludePredicates");
        return configManager.getConfig(CONFIG.GRAPH_EXCLUDE_PREDICATES, new TypeReference<>() {});
    }
}
