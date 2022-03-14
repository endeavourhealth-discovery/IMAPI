package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class TTDocumentFiler implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(TTDocumentFiler.class);

    private TTIriRef graph;
    protected Map<String, String> prefixMap = new HashMap<>();
    protected TTEntityFiler conceptFiler;
    protected TTEntityFiler instanceFiler;

    protected abstract void startTransaction() throws TTFilerException;
    protected abstract void commit() throws TTFilerException;
    protected abstract void rollback() throws TTFilerException;

    public void fileDocument(TTDocument document) throws TTFilerException {

        //Sets the graph iri for use in statements, so they are owned by the graph
        this.graph = document.getGraph();

        LOG.info("Saving ontology - {}", new Date());

        // Record document details, updating ontology and module
        LOG.info("Processing document-ontology-module");

        fileEntities(prefixMap, document);

        LOG.info("Ontology filed");

        LOG.info("Finished - {}", new Date());
    }

    private void fileEntities(Map<String, String> prefixMap, TTDocument document) throws TTFilerException {
        LOG.info("Filing entities.... ");

        startTransaction();
        try {
            if (document.getEntities() != null) {
                int i = 0;
                for (TTEntity entity : document.getEntities()) {
                    TTIriRef entityGraph= entity.getGraph()!=null ?entity.getGraph() : graph;
                    if (entity.get(IM.PRIVACY_LEVEL)!=null)
                        if (entity.get(IM.PRIVACY_LEVEL).asLiteral().intValue()>TTFilerFactory.getPrivacyLevel())
                            continue;
                    if (entity.getCrud() == null) {
                        if (document.getCrud() == null) {
                            entity.setCrud(IM.REPLACE);
                        } else {
                            entity.setCrud(document.getCrud());
                        }
                    }
                    fileEntity(entity, entityGraph);
                    i++;
                    if (i % 10000 == 0) {
                        LOG.info("Filed {} entities from {} in graph {}", i, document.getEntities().size(),document.getGraph().getIri());
                        commit();
                        startTransaction();
                    }
                }
            }
            commit();
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    private void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException {
        if (IM.GRAPH_ODS.equals(graph))
            instanceFiler.fileEntity(entity, graph);
        else
            conceptFiler.fileEntity(entity, graph);
    }
}
