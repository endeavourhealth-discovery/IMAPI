package org.endeavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.search.SearchResponseConcept;

import java.util.List;

public interface IConfigService {
    List<SearchResponseConcept> getQuickAccess();
}
