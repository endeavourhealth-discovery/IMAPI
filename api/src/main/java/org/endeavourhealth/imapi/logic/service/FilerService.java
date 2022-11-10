package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTTransactionFiler;
import org.endeavourhealth.imapi.filer.rdf4j.TTDocumentFilerRdf4j;

import org.endeavourhealth.imapi.filer.rdf4j.TTEntityFilerRdf4j;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FilerService {

    private static final Logger LOG = LoggerFactory.getLogger(FilerService.class);
    private final TTDocumentFiler documentFiler = new TTDocumentFilerRdf4j();
    private final TTEntityFiler entityFiler = new TTEntityFilerRdf4j();
    private final TTEntityFiler entityProvFiler = entityFiler;
    private final ProvService provService = new ProvService();
    private final TTTransactionFiler transactionFiler = new TTTransactionFiler();
    private final EntityService entityService = new EntityService();
    private final OpenSearchService openSearchService = new OpenSearchService();

    public void fileTransactionDocument(TTDocument document, String agentName) throws Exception {
        transactionFiler.fileTransaction(document);
        fileProvDoc(document, agentName);
    }

    public void fileDocument(TTDocument document, String agentName) throws TTFilerException, JsonProcessingException {
        documentFiler.fileDocument(document);
        fileProvDoc(document, agentName);
    }

    public void fileEntity(TTEntity entity, TTIriRef graph, String agentName, TTEntity usedEntity) throws TTFilerException {
        try {
            entityFiler.fileEntity(entity, graph);
            ProvActivity activity = fileProv(entity, agentName, usedEntity);
            writeDelta(entity, activity);
            fileOpenSearch(entity.getIri());
        } catch (Exception e) {
            throw new TTFilerException("Error filing entity", e);
        }
    }

    public void writeDelta(TTEntity entity, ProvActivity activity) throws Exception {
        TTDocument document = new TTDocument();
        document.addEntity(entity);
        document.addEntity(activity);
        transactionFiler.writeLog(document);
    }

    private void fileProvDoc(TTDocument document, String agentName) throws JsonProcessingException, TTFilerException {
        for (TTEntity entity : document.getEntities()) {
            TTEntity usedEntity = null;
            if(entityService.iriExists(entity.getIri())) {
                usedEntity = entityService.getFullEntity(entity.getIri()).getEntity();
            }
            fileProv(entity, agentName, usedEntity);
        }
    }

    private ProvActivity fileProv(TTEntity entity, String agentName, TTEntity usedEntity) throws TTFilerException, JsonProcessingException {
        TTIriRef graph = IM.GRAPH_PROV;
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
        return activity;
    }

    private void fileOpenSearch(String iri) throws TTFilerException {
        try {
            EntityDocument doc = entityService.getOSDocument(iri);
            openSearchService.fileDocument(doc);
        } catch (Exception e) {
            throw new TTFilerException("Unable to file opensearch", e);
        }

    }
}
