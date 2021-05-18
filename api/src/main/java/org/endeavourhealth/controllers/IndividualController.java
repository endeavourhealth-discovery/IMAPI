package org.endeavourhealth.controllers;

import org.endeavourhealth.dataaccess.IndividualService;
import org.endeavourhealth.imapi.model.tripletree.TTInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/individual")
@CrossOrigin(origins = "*")
public class IndividualController {


    @Autowired
    IndividualService individualService;

    @GetMapping(value = "", produces = "application/json")
    public TTInstance getIndividual(@RequestParam(name = "iri") String iri) throws Exception {
        return individualService.getIndividual(iri);
    }
}
