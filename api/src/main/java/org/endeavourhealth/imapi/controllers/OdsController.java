package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.OdsService;
import org.endeavourhealth.imapi.model.dto.InstanceDTO;
import org.endeavourhealth.imapi.model.ods.OdsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("ods")
@CrossOrigin(origins = "*")
@Tag(name="OdsController")
@RequestScope
public class OdsController {
    private static final Logger LOG = LoggerFactory.getLogger(OdsController.class);

    @Autowired
    OdsService odsService;

    @GetMapping(value = "/public/ods", produces = "application/json")
    public OdsResponse getOdsData(@RequestParam(name = "odsCode") String odsCode) {
        LOG.debug("getOdsData");
        return odsService.getOrganisationData(odsCode);
    }
}
