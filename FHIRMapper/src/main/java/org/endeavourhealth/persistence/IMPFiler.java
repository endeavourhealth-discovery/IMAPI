package org.endeavourhealth.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public abstract class IMPFiler implements AutoCloseable {
    private static Logger LOG = LoggerFactory.getLogger(IMPFiler.class);
    ObjectMapper om = new ObjectMapper();

    private Map<String, Collection<String>> modelPropCache = new HashMap<>();

    public void fileIMPs(Collection<IMDMBase> imps) {
        if (imps == null)
            return;

        for (IMDMBase imp : imps) {
            fileIMP(imp);
        }
    }

    public void fileIMP(IMDMBase imp) {
        ObjectNode root = om.convertValue(imp, ObjectNode.class);

        if (!root.has("_id")) {
            LOG.error("NULL OBJECT!");
            return;
        }

        String id = root.get("_id").asText();
        String type = root.get("_type").asText();

        writeData(id, type, root);
        writeRelationships(id, type, root);
    }

    abstract void writeData(String id, String type, ObjectNode root);

    private void writeRelationships(String id, String type, ObjectNode root) {
        Collection<String> refProps = getRefPropsForModel(type);
        for(String rp : refProps) {
            if (root.has(rp)) {
                String target = root.get(rp).asText();
                LOG.trace("Model ref {} -> {} -> {}", id, rp, target);
                writeRelationship(id, rp, target);
            }
        }
    }

    abstract void writeRelationship(String id, String rp, String target);

    private Collection<String> getRefPropsForModel(String model) {
        Collection<String> result = modelPropCache.get(model);

        if (result == null) {
            result = loadRefPropsFromDB(model);
            modelPropCache.put(model, result);
        }

        return result;
    }

    private Collection<String> loadRefPropsFromDB(String model) {
        List<String> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
            .add("select ?prop")
            .add("where { ")
            .add("    ?model sh:property ?bn .")
            .add("    ?bn sh:path ?prop .")
            .add("    ?bn sh:node ?o .")
            .add("    ?o rdf:type sh:NodeShape.")
            .add("}")
            .toString();

        try (RepositoryConnection im = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(im, spql);
            qry.setBinding("model", iri(IM.NAMESPACE + model));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(getSuffix(bs.getValue("prop").stringValue()));
                }
            }
        }

        return result;
    }

    private String getSuffix(String iri) {
        return iri.substring(iri.lastIndexOf("#") + 1);
    }
}
