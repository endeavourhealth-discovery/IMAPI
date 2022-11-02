package org.endeavourhealth.imapi.filer;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.*;

public abstract class TTDocumentFiler implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(TTDocumentFiler.class);

    private TTIriRef graph;
    protected Map<String, String> prefixMap = new HashMap<>();
    protected TTEntityFiler conceptFiler;
    protected TTEntityFiler instanceFiler;
    private Set<String> entitiesFiled;

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

    /**
     * Files a document within a broader transaction i.e. no commit
     * @param document the document to file
     * @throws TTFilerException filing exception
     */
    public void fileInsideTraction(TTDocument document) throws TTFilerException {
        LOG.info("Filing entities.... ");
        int i = 0;
        entitiesFiled= new HashSet<>();
        for (TTEntity entity : document.getEntities()) {
            if (entity.getCrud() == null) {
                if (document.getCrud() == null) {
                    entity.setCrud(IM.UPDATE_ALL);
                } else {
                    entity.setCrud(document.getCrud());
                }
            }

            TTIriRef entityGraph= entity.getGraph();
            if (entityGraph==null)
                entityGraph= document.getGraph();
            if (document.getGraph()==null)
                document.setGraph(entity.getGraph());
            if (entity.get(IM.PRIVACY_LEVEL)!=null)
                if (entity.get(IM.PRIVACY_LEVEL).asLiteral().intValue()>TTFilerFactory.getPrivacyLevel())
                            continue;
                fileEntity(entity, entityGraph);
                entitiesFiled.add(entity.getIri());
                i++;
                if (i %100== 0)
                 LOG.info("Filed {}  entities in transaction from {} in graph {}", i, document.getEntities().size(),entityGraph.getIri());

        }
        updateTct(document);
    }

    public void updateTct(TTDocument document) throws TTFilerException {
        LOG.info("Generating isas.... ");
        int i = 0;
        for (String entityIri:entitiesFiled) {
            i++;
            if (i %100== 0)
                LOG.info("Checked and built  isas up to entity {} in transaction from {} in graph {}", i, document.getEntities().size(),document.getGraph().getIri());
            conceptFiler.updateTct(entityIri);
        }

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
                            entity.setCrud(IM.UPDATE_ALL);
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
