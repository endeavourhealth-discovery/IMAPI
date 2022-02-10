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
        return configManager.getConfig(name, new TypeReference<List<ComponentLayoutItem>>(){});
    }

    @GetMapping(value="/public/filterDefaults")
    public FilterDefault getFilterDefaults() throws JsonProcessingException {
        LOG.debug("getFilterDefaults");
        return configManager.getConfig("filterDefaults", new TypeReference<FilterDefault>(){});
    }

    @GetMapping(value="/public/dashboardLayout")
    public List<DashboardLayout> getDashboardLayout(
            @RequestParam(name ="name") String name
    ) throws JsonProcessingException {
        LOG.debug("getDashboardLayout");
        return configManager.getConfig(name, new TypeReference<List<DashboardLayout>>(){});
    }

    @GetMapping(value="/public/defaultPredicateNames")
    public Map<String, String> getDefaultPredicateNames() throws JsonProcessingException {
        LOG.debug("getDefaultPredicateNames");
        return configManager.getConfig("defaultPredicateNames", new TypeReference<Map<String, String>>() {});
    }

    @GetMapping(value="/public/xmlSchemaDataTypes")
    public List<String> getXMLSchemaDataTypes() throws JsonProcessingException {
        LOG.debug("getXMLSchemaDataTypes");
        return configManager.getConfig("xmlSchemaDataTypes", new TypeReference<List<String>>() {});
    }

    @GetMapping(value="/public/graphExcludePredicates")
    public List<String> getGraphExcludePredicates() throws JsonProcessingException {
        LOG.debug("getGraphExcludePredicates");
        return configManager.getConfig("graphExcludePredicates", new TypeReference<List<String>>() {});
    }
}
