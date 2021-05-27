package org.endeavourhealth.controllers;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.logic.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Api(value="ConfigController", description = "IM application configuration endpoint")
public class ConfigController {


    @Autowired
    ConfigService configService;

    @GetMapping(value = "/quickAccess")
    public List<ConceptSummary> getQuickAccess() throws JsonProcessingException, SQLException {
        return configService.getQuickAccess();
    }
}
