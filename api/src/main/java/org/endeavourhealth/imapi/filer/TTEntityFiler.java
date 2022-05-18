package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface TTEntityFiler {
    void fileEntity(TTEntity entity, TTIriRef graph, String agentIri) throws TTFilerException;
    void updateTct(TTDocument document) throws TTFilerException;
}
