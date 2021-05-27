package org.endeavourhealth.logic.service;

import org.endeavourhealth.dataaccess.repository.IndividualRepository;
import org.endeavourhealth.dataaccess.repository.IndividualTplDataRepository;
import org.endeavourhealth.dataaccess.repository.IndividualTplRepository;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Qualifier("IndividualService")
public class IndividualService {
    private static final Logger LOG = LoggerFactory.getLogger(IndividualService.class);

    IndividualRepository individualRepository = new IndividualRepository();
    IndividualTplRepository individualTplRepository = new IndividualTplRepository();
    IndividualTplDataRepository individualTplDataRepository = new IndividualTplDataRepository();

    public TTInstance getIndividual(String iri) throws Exception {
        LOG.debug("getIndividual");

        TTInstance result = individualRepository.getByIri(iri);

        TTNode objects = individualTplRepository.findAllBySubjectIri(iri);
        for (Map.Entry<TTIriRef, TTValue> predicateMap : objects.getPredicateMap().entrySet())
            result.set(predicateMap.getKey(), predicateMap.getValue());

        TTNode data = individualTplDataRepository.findAllBySubjectIri(iri);
        for (Map.Entry<TTIriRef, TTValue> predicateMap : data.getPredicateMap().entrySet())
            result.set(predicateMap.getKey(), predicateMap.getValue());

        return result;
    }
}
