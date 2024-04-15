package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


@Component
public class SetService {
    private static final Logger LOG = LoggerFactory.getLogger(SetService.class);
    private final QueryRepository queryRepository = new QueryRepository();

    private SetTextFileExporter setTextFileExporter = new SetTextFileExporter();

    public Query setQueryLabels(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }

    public String getTSVSetExport(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets,
                                  boolean ownRow, boolean im1id, List<String> schemes) throws QueryException, JsonProcessingException {
        return setTextFileExporter.getSetFile(setIri, definition, core, legacy, includeSubsets, ownRow, im1id, schemes, "\t");

    }

    public String getCSVSetExport(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets,
                                  boolean ownRow, boolean im1id, List<String> schemes) throws QueryException, JsonProcessingException {
        return setTextFileExporter.getSetFile(setIri, definition, core, legacy, includeSubsets, ownRow, im1id, schemes, ",");

    }

    public SetContent getSetContent(String setIri, boolean definition, boolean core, boolean legacy, boolean subsets, List<String> schemes) throws QueryException, JsonProcessingException {
        SetContent result = new SetContent();

        LOG.trace("Fetching metadata for {}...", setIri);
        TTEntity entity = new EntityTripleRepository().getEntityPredicates(setIri, Set.of(RDFS.LABEL, RDFS.COMMENT, IM.STATUS, IM.VERSION, IM.DEFINITION)).getEntity();

        if (null != entity) {
            result.setName(entity.getName())
                .setDescription(entity.getDescription())
                .setVersion(entity.getVersion());

            if (null != entity.getStatus())
                result.setStatus(entity.getStatus().getName());

            if (definition && null != entity.get(iri(IM.DEFINITION))) {
                Query qryDef = entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
                result.setSetDefinition(new IMQToECL().getECLFromQuery(qryDef, true));
            }
        }

        SetExporter setExporter = new SetExporter();

        if (subsets) {
            LOG.trace("Fetching subsets...");
            result.setSubsets(setExporter.getSubsetIrisWithNames(setIri).stream().map(TTIriRef::getIri).collect(Collectors.toSet()));
        }

        if (core || legacy || subsets) {
            LOG.trace("Expanding...");
            result.setConcepts(setExporter
                .getExpandedSetMembers(setIri, core, legacy, subsets, schemes)
            );
        }

        return result;
    }
}
