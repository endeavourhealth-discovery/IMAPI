package org.endeavourhealth.imapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Configuration
public class ConfigManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);

    public <T> T getConfig(CONFIG iri, TypeReference<T> resultType) throws JsonProcessingException {
        LOG.debug("getConfig<TypeReference>");

        try (CachedObjectMapper om = new CachedObjectMapper()) {
            Config config = getConfig(iri);
            if (config == null)
                return null;
            return om.readValue(config.getData(), resultType);
        }
    }

    public Config getConfig(CONFIG config) {
        // NOTE - DON'T USE PREFIXES OR 'prepareSparql' HERE
        //        OR CYCLIC LOOP FETCHING DEFAULT PREFIXES
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?name ?data WHERE {")
                .add("    GRAPH <http://endhealth.info/config#> {")
                .add("      ?s ?label   ?name ;")
                .add("         ?config  ?data .")
                .add("    }")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConfigConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", Values.iri(config.iri));
            qry.setBinding("label", Values.iri(RDFS.LABEL.iri));
            qry.setBinding("config", Values.iri(IM.HAS_CONFIG.iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return new Config()
                            .setName(bs.getValue("name").stringValue())
                            .setData(bs.getValue("data").stringValue());
                } else {
                    return null;
                }
            }
        }
    }


}
