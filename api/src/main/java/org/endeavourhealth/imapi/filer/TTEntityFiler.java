package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface TTEntityFiler {
    void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException;
    void updateTct(String entity) throws TTFilerException;
}
