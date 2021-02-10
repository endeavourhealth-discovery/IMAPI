package org.endeavourhealth.controllers;

import org.endeavourhealth.dataaccess.IConfigService;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/config")
@CrossOrigin
public class ConfigController {


    @Autowired
    @Qualifier("ConfigService")
    IConfigService configService;


    // IConceptService conceptService = new ConceptServiceRDF4J();

    @GetMapping(value = "/quickAccess")
    public List<ConceptSummary> getQuickAccess() {
        return configService.getQuickAccess();
    }
}
