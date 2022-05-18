package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;

import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.stereotype.Component;

@Component
public class FilerService {

    private final TTDocumentFiler documentFiler = new TTDocumentFilerRdf4j();
    private final TTEntityFiler entityFiler = new TTEntityFilerRdf4j();

    public void fileDocument(TTDocument document, String agentIri) throws TTFilerException {
        documentFiler.fileDocument(document, agentIri);
    }

    public void fileEntity(TTEntity entity, TTIriRef graph, String agentIri) throws TTFilerException {
        entityFiler.fileEntity(entity,graph, agentIri);
    }
}
