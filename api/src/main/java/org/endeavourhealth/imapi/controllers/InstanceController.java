package org.endeavourhealth.imapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.logic.service.InstanceService;
import org.endeavourhealth.imapi.model.dto.InstanceDTO;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("instance")
@CrossOrigin(origins = "*")
@Api(value="InstanceController")
@SwaggerDefinition(tags = {
        @Tag(name = "Instance Controller", description = "Main Instance endpoint")
})
public class InstanceController {
    private static final Logger LOG = LoggerFactory.getLogger(InstanceController.class);

    @Autowired
    InstanceService instanceService;

    @GetMapping(value = "/partial", produces = "application/json")
    public InstanceDTO getPartialInstance(@RequestParam(name = "iri") String iri,
                                          @RequestParam(name = "predicate", required = false) Set<String> predicates) throws SQLException {
        LOG.debug("getPartialInstance");
        return instanceService.getInstancePredicates(iri,predicates);
    }

    @GetMapping(value = "/search")
    public List<TTIriRef> search(@RequestParam(name = "request") String request,
                                 @RequestParam(name = "typesIris") Set<String> typesIris ) throws SQLException {
        LOG.debug("search");
        return instanceService.search(request, typesIris);
    }

    @GetMapping("/typesCount")
    public List<SimpleCount> typesCount() throws SQLException {
        LOG.debug("typesCount");
        return instanceService.typesCount();
    }


}
