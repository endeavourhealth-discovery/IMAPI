package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.ProvService;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/prov")
@CrossOrigin(origins = "*")
@Tag(name = "Provenance Controller")
@RequestScope
public class ProvController {

    private static final Logger LOG = LoggerFactory.getLogger(ProvController.class);

    ProvService provService = new ProvService();

    @GetMapping("/public/history")
    public List<TTEntity> getProvHistory(@RequestParam(name = "iri") String iri) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Prov.History.GET")) {
            LOG.debug("getProvHistory");
            return provService.getProvHistory(iri);
        }
    }
}
