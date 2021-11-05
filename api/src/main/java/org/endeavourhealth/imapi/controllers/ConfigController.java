package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.logic.service.ConfigService;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.config.DashboardLayout;
import org.endeavourhealth.imapi.model.config.FilterDefault;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Api(value="ConfigController")
@SwaggerDefinition(tags = {
    @Tag(name = "Config Controller", description = "IM application configuration endpoint")
})
public class ConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    ConfigService configService;

    @GetMapping(value = "/componentLayout")
    public List<ComponentLayoutItem> getComponentLayout(
            @RequestParam(name="name") String name
    ) throws SQLException, JsonProcessingException {
        LOG.debug("getComponentLayout");
        return configService.getConfig(name, new TypeReference<List<ComponentLayoutItem>>(){});
    }

    @GetMapping(value="/filterDefaults")
    public FilterDefault getFilterDefaults() throws SQLException, JsonProcessingException {
        LOG.debug("getFilterDefaults");
        return configService.getConfig("filterDefaults", new TypeReference<FilterDefault>(){});
    }

    @GetMapping(value="/dashboardLayout")
    public List<DashboardLayout> getDashboardLayout(
            @RequestParam(name ="name") String name
    ) throws SQLException, JsonProcessingException {
        LOG.debug("getDashboardLayout");
        return configService.getConfig(name, new TypeReference<List<DashboardLayout>>(){});
    }

    @GetMapping(value="/defaultPredicateNames")
    public Map<String, String> getDefaultPredicateNames() throws SQLException, JsonProcessingException {
        LOG.debug("getDefaultPredicateNames");
        return configService.getConfig("defaultPredicateNames", new TypeReference<Map<String, String>>() {});
    }

    @GetMapping(value="/xmlSchemaDataTypes")
    public List<String> getXMLSchemaDataTypes() throws SQLException, JsonProcessingException {
        LOG.debug("getXMLSchemaDataTypes");
        return configService.getConfig("xlmSchemaDataTypes", new TypeReference<List<String>>() {});
    }
}
