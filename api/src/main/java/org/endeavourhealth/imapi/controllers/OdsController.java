package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.OdsService;
import org.endeavourhealth.imapi.model.ods.OdsResponse;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("api/ods")
@CrossOrigin(origins = "*")
@Tag(name = "OdsController")
@RequestScope
public class OdsController {
    private static final Logger LOG = LoggerFactory.getLogger(OdsController.class);

    @Autowired
    OdsService odsService;

    @GetMapping(value = "/public/organisation/{odsCode}", produces = "application/json")
    public OdsResponse getOrganisationData(@PathVariable(name = "odsCode") String odsCode) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Ods.Organisation.GET")) {
            LOG.debug("getOrganisationData");

            OdsResponse response = odsService.getOrganisationData(odsCode);

            if (response != null && response.getOrganisation() != null)
                return response;
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ODS code not found");
        }
    }

    @GetMapping(value = "/public/roles", produces = "application/json")
    public OdsResponse getRoleData() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Ods.Roles.GET")) {
            LOG.debug("getRoleData");

            OdsResponse response = odsService.getRoleData();

            if (response != null && response.getRoles() != null)
                return response;
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No roles");
        }
    }
}
