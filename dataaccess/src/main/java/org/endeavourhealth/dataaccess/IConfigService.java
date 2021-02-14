package org.endeavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.search.ConceptSummary;

import java.util.List;

public interface IConfigService {
    List<ConceptSummary> getQuickAccess();
}
