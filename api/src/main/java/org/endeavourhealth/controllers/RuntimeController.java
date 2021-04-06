package org.endeavourhealth.controllers;

import org.endeavourhealth.dataaccess.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/runtime", produces = "text/plain")
@CrossOrigin(origins = "*")
public class RuntimeController {

    @Autowired
    RuntimeService runtimeService;

    @GetMapping(value = "/Concept/Id")
    public String getConceptIdForSchemeCode(@RequestParam("scheme") String scheme,
                                            @RequestParam("code") String code){

        return runtimeService.getConceptIdForSchemeCode(scheme, code);
    }

    @GetMapping("/Concept/Core/Code")
    public String getMappedCoreCodeForSchemeCode(@RequestParam("scheme") String scheme,
                                                 @RequestParam("code") String code,
                                                 @RequestParam("snomedOnly") Boolean snomedOnly){
       return runtimeService.getMappedCoreCodeForSchemeCode(scheme, code, snomedOnly);
    }

    @GetMapping("/Term/Code")
    public String getCodeForTypeTerm(@RequestParam("scheme") String scheme,
                                     @RequestParam("context") String context,
                                     @RequestParam("term") String term,
                                     @RequestParam("autoCreate") boolean autoCreate){
        return runtimeService.getCodeForTypeTerm(scheme, context, term, autoCreate);
    }

    @GetMapping(value = "/Concept")
    public String getConceptDbidForSchemeCode(@RequestParam("scheme") String scheme,
                                                @RequestParam("code") String code){
        Integer result = runtimeService.getConceptDbidForSchemeCode(scheme,code);
        if(result == null){
            return null;
        }else{
            return result.toString();
        }
    }

    @GetMapping("/Concept/Core")
    public Integer getMappedCoreConceptDbidForSchemeCode(@RequestParam("scheme") String scheme,
                                                          @RequestParam("code") String code){
        return runtimeService.getMappedCoreConceptDbidForSchemeCode(scheme, code);
    }

    @GetMapping(value = "/Concept/Code")
    public String getCodeForConceptDbid(@RequestParam("dbid") Integer dbid) throws Exception {
       return runtimeService.getCodeForConceptDbid(dbid);
    }

    @GetMapping("/Term")
    public Integer getConceptDbidForTypeTerm(@RequestParam("type") String type,
                                              @RequestParam("term") String term,
                                              @RequestParam("autoCreate") boolean autoCreate){
       return runtimeService.getConceptDbidForTypeTerm(type,term,autoCreate);
    }

    @GetMapping("/Term/Core")
    public Integer getMappedCoreConceptDbidForTypeTerm(@RequestParam("type") String type,
                                                        @RequestParam("term") String term){
       return runtimeService.getMappedCoreConceptDbidForTypeTerm(type, term);
    }






}
