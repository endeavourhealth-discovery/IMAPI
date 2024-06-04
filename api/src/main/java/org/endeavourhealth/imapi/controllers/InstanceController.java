package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.InstanceService;
import org.endeavourhealth.imapi.model.dto.InstanceDTO;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("instance")
@CrossOrigin(origins = "*")
@Tag(name="InstanceController")
@RequestScope
public class InstanceController {
    private static final Logger LOG = LoggerFactory.getLogger(InstanceController.class);

    @Autowired
    InstanceService instanceService;

    @GetMapping(value = "/public/partial", produces = "application/json")
    public InstanceDTO getPartialInstance(@RequestParam(name = "iri") String iri,
                                          @RequestParam(name = "predicate", required = false) Set<String> predicates) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Instance.Partial.GET")) {
            LOG.debug("getPartialInstance");
            return instanceService.getInstancePredicates(iri, predicates);
        }
    }

    @GetMapping(value = "/public/search")
    public List<TTIriRef> search(@RequestParam(name = "request") String request,
                                 @RequestParam(name = "typesIris") Set<String> typesIris ) throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Instance.Search.GET")) {
            LOG.debug("search");
            return instanceService.search(request, typesIris);
        }
    }

    @GetMapping("/public/typesCount")
    public List<SimpleCount> typesCount() throws IOException {
        try (MetricsTimer t = MetricsHelper.recordTime("API.Instance.TypesCount.GET")) {
            LOG.debug("typesCount");
            return instanceService.typesCount();
        }
    }


}
