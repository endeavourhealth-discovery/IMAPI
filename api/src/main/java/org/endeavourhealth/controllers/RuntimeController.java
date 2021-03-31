package org.endeavourhealth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/runtime", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
@CrossOrigin(origins = "*")
public class RuntimeController {

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeController.class);

    @GetMapping("/Concept/Id")
    public String getConceptIdForSchemeCode(@RequestParam("scheme") String scheme,
                                            @RequestParam("code") String code){

        return null;
    }

    @GetMapping("/Concept/Core/Code")
    public String getMappedCoreCodeForSchemeCode(@RequestParam("scheme") String scheme,
                                                 @RequestParam("code") String code,
                                                 @RequestParam("snomedOnly") Boolean snomedOnly){
       return null;
    }

    @GetMapping("/Term/Code")
    public String getCodeForTypeTerm(@RequestParam("scheme") String scheme,
                                     @RequestParam("context") String context,
                                     @RequestParam("term") String term,
                                     @RequestParam("autoCreate") boolean autoCreate){
        return null;
    }

    @GetMapping("/Concept")
    public Integer getConceptDbidForSchemeCode(@RequestParam("context") String context,
                                                @RequestParam("scheme") String scheme,
                                                @RequestParam("code") String code,
                                                @RequestParam("autoCreate") boolean autoCreate,
                                                @RequestParam("term") String term ){
        return null;
    }

    @GetMapping("/Concept/Core")
    public Integer getMappedCoreConceptDbidForSchemeCode(@RequestParam("scheme") String scheme,
                                                          @RequestParam("code") String code){
        return null;
    }

    @GetMapping("/Concept/Code")
    public String getCodeForConceptDbid(@RequestParam("dbid") Integer dbid) throws Exception {
       return null;
    }

    @GetMapping("/Term")
    public Integer getConceptDbidForTypeTerm(@RequestParam("type") String type,
                                              @RequestParam("term") String term,
                                              @RequestParam("autoCreate") boolean autoCreate){
       return null;
    }

    @GetMapping("/Term/Core")
    public Integer getMappedCoreConceptDbidForTypeTerm(@RequestParam("type") String type,
                                                        @RequestParam("term") String term){
       return null;
    }






}
