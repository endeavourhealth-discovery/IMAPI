package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


@Component
public class SetService {
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

    public String getCSVSetExport(String setIri,boolean definition, boolean core, boolean legacy,boolean includeSubsets,
                                  boolean ownRow, boolean im1id, List<String> schemes) throws QueryException, JsonProcessingException {
        return setTextFileExporter.getSetFile(setIri, definition, core, legacy, includeSubsets, ownRow, im1id, schemes, ",");

    }
    public SetContent getSetContent(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets) throws QueryException, JsonProcessingException {
        SetContent result = new SetContent();
        TTEntity entity = new EntityTripleRepository().getEntityPredicates(setIri, Set.of(IM.DEFINITION)).getEntity();
        if (entity != null) {
            if (entity.get(iri(IM.DEFINITION)) != null) {
                result.setSetDefinition(new IMQToECL().getECLFromQuery(entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class), true));
            }
        }
        Set<Concept> members = new SetExporter().getExpandedSetMembers(setIri, legacy, true, null);
        result.setConcepts(members);
        return result;
    }

}
