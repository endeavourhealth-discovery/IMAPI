package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.ECLToIML;
import org.endeavourhealth.imapi.transforms.IMLToECL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Component
public class SetService {
    private final SetRepository setRepository = new SetRepository();
    private final QueryRepository queryRepository = new QueryRepository();

    public Query setQueryLabels(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }
}
