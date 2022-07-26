package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Set;

public interface TTEntityFiler {
    void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException;
    void updateTct(String entity) throws TTFilerException;
}
