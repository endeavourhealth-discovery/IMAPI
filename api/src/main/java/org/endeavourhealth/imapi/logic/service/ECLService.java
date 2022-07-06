package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.EditSet;
import org.endeavourhealth.imapi.transforms.ECLToTT;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Component
public class ECLService {
    private static final Logger LOG = LoggerFactory.getLogger(ECLService.class);

    private final EntityTripleRepository entityTripleRepository;
    private final EntityRepository entityRepository;

    public ECLService() {
        entityTripleRepository = new EntityTripleRepository();
        entityRepository= new EntityRepository();
    }

    public Set<EntitySummary> evaluateECL(String ecl, boolean includeLegacy) throws DataFormatException {
        TTValue definition = new ECLToTT().getClassExpression(ecl);
        return evaluateECLDefinition(definition,includeLegacy);
    }

    /**
     * @param definition
     * Evaluates a definition
     * @return  Set of concepts conforming to the definition
     */
    public Set<EntitySummary> evaluateECLDefinition(TTValue definition, boolean includeLegacy) {
        LOG.debug("Evaluate");
        Set<EntitySummary> result = new HashSet<>();
        EditSet editSet = evaluateECLDefinition(definition);

        if (editSet.getIncs() != null) result = editSet.getIncs();

        if (includeLegacy) {
            LOG.debug("Fetching legacy concepts for {} members", result.size());
            result.addAll(entityTripleRepository.getLegacyConceptSummaries(result));
        }

        LOG.debug("Found {} total concepts", result.size());

        return result;
    }

    private EditSet evaluateECLDefinition(TTValue ttValue) throws DALException {
        EditSet result = new EditSet();
        if (ttValue.isNode()) {
            for (Map.Entry<TTIriRef, TTArray> predicateValue : ttValue.asNode().getPredicateMap().entrySet()) {
                if (SHACL.AND.equals(predicateValue.getKey())) {
                    processAND(result, predicateValue);
                } else if (SHACL.OR.equals(predicateValue.getKey())) {
                    processOR(result, predicateValue);
                } else if (SHACL.NOT.equals(predicateValue.getKey())) {
                    processNOT(result, predicateValue);
                } else {
                    result.addAllIncs(entityTripleRepository.getSubjectAndDescendantSummariesByPredicateObjectRelType(predicateValue.getKey().getIri(), predicateValue.getValue().asIriRef().getIri()));
                }
            }
        } else if (ttValue.isIriRef()) {
            result.addAllIncs(entityTripleRepository.getSubclassesAndReplacements(ttValue.asIriRef().getIri()));
        } else {
            throw new IllegalStateException("Unhandled concept set node type");
        }

        if (result.getIncs() != null && result.getExcs() != null) {
            result.getIncs().removeAll(result.getExcs());
            result.setExcs(null);
        }

        return result;
    }

    private void processAND(EditSet result, Map.Entry<TTIriRef, TTArray> ands) throws DALException {
        for (TTValue and : ands.getValue().iterator()) {
            EditSet andNode = evaluateECLDefinition(and);

            if (andNode.getIncs() != null) {
                if (result.getIncs() == null) {
                    result.addAllIncs(andNode.getIncs());
                } else {
                    result.getIncs().retainAll(andNode.getIncs());
                }
            }

            if (andNode.getExcs() != null) {
                result.addAllExcs(andNode.getExcs());
            }
        }
    }

    private void processOR(EditSet result, Map.Entry<TTIriRef, TTArray> ors) {
        for (TTValue or : ors.getValue().iterator()) {
            EditSet orNode = evaluateECLDefinition(or);

            if (orNode.getIncs() != null)
                result.addAllIncs(orNode.getIncs());

            if (orNode.getExcs() != null)
                throw new IllegalStateException("'OR NOT' currently unsupported");
        }
    }

    private void processNOT(EditSet result, Map.Entry<TTIriRef, TTArray> nots) {
        if (nots.getValue().isIriRef()) {
            result.addAllExcs(evaluateECLDefinition(nots.getValue().asIriRef()).getIncs());
        } else {
            for (TTValue not : nots.getValue().iterator()) {
                EditSet notNode = evaluateECLDefinition(not);

                if (notNode.getIncs() != null)
                    result.addAllExcs(notNode.getIncs());

                if (notNode.getExcs() != null)
                    throw new IllegalStateException("'NOT NOT' currently unsupported");
            }
        }
    }

    public SearchResponse eclSearch(boolean includeLegacy, Integer limit, String ecl) throws DataFormatException {
        Set<EntitySummary> evaluated = evaluateECL(ecl, includeLegacy);
        List<SearchResultSummary> evaluatedAsSummary = evaluated.stream().limit(limit != null ? limit : 1000).map(ttIriRef -> {
            try {
                return entityRepository.getEntitySummaryByIri(ttIriRef.getIri());
            } catch (DALException e) {
                return new SearchResultSummary().setIri(ttIriRef.getIri()).setName(ttIriRef.getName());
            }
        }).collect(Collectors.toList());
        SearchResponse result = new SearchResponse();
        result.setEntities(evaluatedAsSummary);
        result.setCount(evaluated.size());
        result.setPage(1);
        return result;
    }
}
