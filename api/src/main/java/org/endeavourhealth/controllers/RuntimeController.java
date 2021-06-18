package org.endeavourhealth.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.endeavourhealth.logic.service.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping(value = "api/runtime", produces = "text/plain")
@CrossOrigin(origins = "*")
@Api(value="RuntimeController", description = "Runtime API endpoint (v1 backwards compatibility")
public class RuntimeController {

    @Autowired
    RuntimeService runtimeService;

    @GetMapping(value = "/Entity/Id")
    public String getEntityIdForSchemeCode(@RequestParam("scheme") String scheme,
                                            @RequestParam("code") String code) throws SQLException {

        return runtimeService.getEntityIdForSchemeCode(scheme, code);
    }

    @GetMapping("/Entity/Core/Code")
    public String getMappedCoreCodeForSchemeCode(@RequestParam("scheme") String scheme,
                                                 @RequestParam("code") String code,
                                                 @RequestParam("snomedOnly") Boolean snomedOnly) throws SQLException {
       return runtimeService.getMappedCoreCodeForSchemeCode(scheme, code, snomedOnly);
    }

    @GetMapping("/Term/Code")
    public String getCodeForTypeTerm(@RequestParam("scheme") String scheme,
                                     @RequestParam("context") String context,
                                     @RequestParam("term") String term,
                                     @RequestParam("autoCreate") boolean autoCreate){
        return runtimeService.getCodeForTypeTerm(scheme, context, term, autoCreate);
    }

    @GetMapping(value = "/Entity")
    public String getEntityDbidForSchemeCode(@RequestParam("scheme") String scheme,
                                                @RequestParam("code") String code) throws SQLException {
        Integer result = runtimeService.getEntityDbidForSchemeCode(scheme,code);
        if(result == null){
            return null;
        }else{
            return result.toString();
        }
    }

    @GetMapping("/Entity/Core")
    public Integer getMappedCoreEntityDbidForSchemeCode(@RequestParam("scheme") String scheme,
                                                          @RequestParam("code") String code) throws SQLException {
        return runtimeService.getMappedCoreEntityDbidForSchemeCode(scheme, code);
    }

    @GetMapping(value = "/Entity/Code")
    public String getCodeForEntityDbid(@RequestParam("dbid") Integer dbid) throws Exception {
       return runtimeService.getCodeForEntityDbid(dbid);
    }

    @GetMapping("/Term")
    public Integer getEntityDbidForTypeTerm(@RequestParam("type") String type,
                                              @RequestParam("term") String term,
                                              @RequestParam("autoCreate") boolean autoCreate){
       return runtimeService.getEntityDbidForTypeTerm(type,term,autoCreate);
    }

    @GetMapping("/Term/Core")
    public Integer getMappedCoreEntityDbidForTypeTerm(@RequestParam("type") String type,
                                                        @RequestParam("term") String term){
       return runtimeService.getMappedCoreEntityDbidForTypeTerm(type, term);
    }

    @GetMapping("/Entity/isValueSetMember")
    public String checkEntityByCodeSchemeInVSet(@RequestParam("code") String code, @RequestParam("scheme") String scheme, @RequestParam("vSet") String vSet) throws JsonProcessingException, SQLException {
        return runtimeService.isInVSet(code, scheme,vSet).toString();
    }

}
