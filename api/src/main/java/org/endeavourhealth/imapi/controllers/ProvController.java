package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.ProvService;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

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
    public List<TTEntity> getProvHistory(@RequestParam(name = "iri") String iri) {
        LOG.debug("getProvHistory");
        return provService.getProvHistory(iri);
    }
}
