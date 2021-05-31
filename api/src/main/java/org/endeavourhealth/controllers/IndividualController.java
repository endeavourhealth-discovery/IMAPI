package org.endeavourhealth.controllers;

import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.logic.service.IndividualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/individual")
@CrossOrigin(origins = "*")
@Api(value="IndividualController", description = "Endpoint relating to IM Individuals (concept instances)")
public class IndividualController {


    @Autowired
    IndividualService individualService;

    @GetMapping(value = "", produces = "application/json")
    public TTConcept getIndividual(@RequestParam(name = "iri") String iri) throws Exception {
        return individualService.getIndividual(iri);
    }
}
