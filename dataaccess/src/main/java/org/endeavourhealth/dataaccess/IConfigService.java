package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.search.ConceptSummary;

import java.util.List;

public interface IConfigService {
    List<ConceptSummary> getQuickAccess() throws JsonProcessingException;
}
