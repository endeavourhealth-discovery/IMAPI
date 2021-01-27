package org.endeavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.ConceptReference;

import java.util.List;

public interface IConfigService {
    List<ConceptReference> getQuickAccess();
}
