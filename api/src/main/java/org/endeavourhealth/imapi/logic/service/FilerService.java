package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTTransactionFiler;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;

import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FilerService {

    private final TTDocumentFiler documentFiler = new TTDocumentFilerRdf4j();
    private final TTEntityFiler entityFiler = new TTEntityFilerRdf4j();
    private final TTEntityFiler entityProvFiler = new TTEntityFilerRdf4j(ConnectionManager.getProvConnection(), new HashMap<>());
    private final ProvService provService = new ProvService();
    private final TTTransactionFiler transactionFiler = new TTTransactionFiler();
    private final EntityService entityService = new EntityService();

    public void fileTransactionDocument(TTDocument document, String agentName) throws Exception {
        transactionFiler.fileTransaction(document);
        fileProvDoc(document, agentName);
    }

    public void fileDocument(TTDocument document, String agentName) throws TTFilerException, JsonProcessingException {
        documentFiler.fileDocument(document);
        fileProvDoc(document, agentName);
    }

    public void fileEntity(TTEntity entity, TTIriRef graph, String agentName, TTEntity usedEntity) throws TTFilerException, JsonProcessingException {
        entityFiler.fileEntity(entity, graph);
        fileProv(entity, graph, agentName, usedEntity);
    }

    private void fileProvDoc(TTDocument document, String agentName) throws JsonProcessingException, TTFilerException {
        for (TTEntity entity : document.getEntities()) {
            TTEntity usedEntity = null;
            if(entityService.iriExists(entity.getIri())) {
                usedEntity = entityService.getFullEntity(entity.getIri()).getEntity();
            }
            fileProv(entity, document.getGraph(), agentName, usedEntity);
        }
    }

    private void fileProv(TTEntity entity, TTIriRef graph, String agentName, TTEntity usedEntity) throws TTFilerException, JsonProcessingException {
        ProvAgent agent = provService.buildProvenanceAgent(entity, agentName);
        entityProvFiler.fileEntity(agent, graph);

        String usedEntityIri = null;
        if (null != usedEntity) {
            TTEntity provUsed = provService.buildUsedEntity(usedEntity);
            usedEntityIri = provUsed.getIri();
            entityProvFiler.fileEntity(provUsed, graph);
        }

        ProvActivity activity = provService.buildProvenanceActivity(entity, agent, usedEntityIri);
        entityProvFiler.fileEntity(activity, graph);
    }
}
