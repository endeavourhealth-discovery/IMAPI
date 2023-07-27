package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.springframework.stereotype.Component;

@Component
public class SetService {
    private final QueryRepository queryRepository = new QueryRepository();

    private SetTextFileExporter setTextFileExporter = new SetTextFileExporter();

    public Query setQueryLabels(Query query) {
        queryRepository.labelQuery(query);
        return query;
    }

    public String getTSVSetExport(String setIri,boolean definition, boolean core, boolean legacy,boolean includeSubsets, boolean ownRow, boolean im1id) throws QueryException, JsonProcessingException {
        return setTextFileExporter.getSetFile(setIri, definition, core, legacy, includeSubsets, ownRow, im1id, "\t");

    }

    public String getCSVSetExport(String setIri,boolean definition, boolean core, boolean legacy,boolean includeSubsets, boolean ownRow, boolean im1id) throws QueryException, JsonProcessingException {
        return setTextFileExporter.getSetFile(setIri, definition, core, legacy, includeSubsets, ownRow, im1id, ",");

    }
}
