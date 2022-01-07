package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class ConfigRepositoryImpl implements ConfigRepository {
    @Override
    public Config findByName(String name) {
        switch (name) {
            case "definition":
                return getConfig(CONFIG.DEFINITION);
            case "filterDefaults":
                return getConfig(CONFIG.FILTER_DEFAULTS);
            case "inferredPredicates":
                return getConfig(CONFIG.INFERRED_PREDICATES);
            case "inferredExcludePredicates":
                return getConfig(CONFIG.INFERRED_EXCLUDE_PREDICATES);
            case "conceptDashboard":
                return getConfig(CONFIG.CONCEPT_DASHBOARD);
            case "defaultPredicateNames":
                return getConfig(CONFIG.DEFAULT_PREDICATE_NAMES);
            case "xmlSchemaDataTypes":
                return getConfig(CONFIG.XML_SCHEMA_DATATYPES);
            case "defaultPrefixes":
                return getConfig(CONFIG.DEFAULT_PREFIXES);
            case "graphExcludePredicates":
                return getConfig(CONFIG.GRAPH_EXCLUDE_PREDICATES);
            default:
                throw new DALException("Unhandled config");
        }
    }

    private Config getConfig(TTIriRef iri) {
        // NOTE - DONT USE PREFIXES OR 'prepareSparql' HERE
        //        OR CYCLIC LOOP FETCHING DEFAULT PREFIXES
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?name ?data WHERE {")
            .add("    GRAPH <http://endhealth.info/config#> {")
            .add("      ?s ?label   ?name ;")
            .add("         ?config  ?data .")
            .add("    }")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", iri(iri.getIri()));
            qry.setBinding("label", iri(RDFS.LABEL.getIri()));
            qry.setBinding("config", iri(IM.HAS_CONFIG.getIri()));
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

