package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.ValidatingValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.config.Config;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.imq.Value;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.USER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareUpdateSparql;

public class UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    public String getSparqlSelect() {
        StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("SELECT ?o WHERE {").add("  ?s ?p ?o").add("}");
        return sparql.toString();
    }

    public String getSparqlDelete() {
        StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("DELETE WHERE {").add("  ?s ?p ?o").add("}");
        return sparql.toString();
    }

    public String getSparqlInsert() {
        StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("INSERT {").add("  ?s ?p ?o ").add("}").add("WHERE { SELECT ?s ?p ?o {} }");
        return sparql.toString();
    }

    public String getUserTheme(String user) {
        String result = "";
        String sparql = getSparqlSelect();
        try (RepositoryConnection conn = ConnectionManager.getUserConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            qry.setBinding("s", iri(USER.NAMESPACE + user));
            qry.setBinding("p", iri(USER.USER_THEME.getIri()));
            System.out.println(qry.toString());

            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result = bs.getValue("o").stringValue();
                }
            }
        }
        return result;
    }

    public List<RecentActivityItemDto> getUserMRU(String user) throws JsonProcessingException {
        List<RecentActivityItemDto> result = new ArrayList<>();
        String sparql = getSparqlSelect();

        try (RepositoryConnection conn = ConnectionManager.getUserConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            qry.setBinding("s", iri(USER.NAMESPACE + user));
            qry.setBinding("p", iri(USER.USER_MRU.getIri()));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    try (CachedObjectMapper om = new CachedObjectMapper()) {
                        return om.readValue(bs.getValue("o").stringValue(), new TypeReference<>() {
                        });
                    }
                }
            }
        }
        return result;
    }

    public List<String> getUserFavourites(String user) throws JsonProcessingException {
        List<String> result = new ArrayList<String>();
        String sparql = getSparqlSelect();

        try (RepositoryConnection conn = ConnectionManager.getUserConnection()) {
            TupleQuery qry = prepareSparql(conn, sparql);
            qry.setBinding("s", iri(USER.NAMESPACE + user));
            qry.setBinding("p", iri(USER.USER_FAVOURITES.getIri()));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    try (CachedObjectMapper om = new CachedObjectMapper()) {
                        return om.readValue(bs.getValue("o").stringValue(), new TypeReference<>() {
                        });
                    }
                }
            }
        }
        return result;
    }

    public void delete(String user, String predicate) {
        String sparql = getSparqlDelete();
        try (RepositoryConnection conn = ConnectionManager.getUserConnection()) {
            Update qry = conn.prepareUpdate(sparql);
            qry.setBinding("s", iri(USER.NAMESPACE + user));
            qry.setBinding("p", iri(predicate));
            qry.execute();
        }
    }

    public void insert(String user, String predicate, Object object) throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            String sparql = getSparqlInsert();
            try (RepositoryConnection conn = ConnectionManager.getUserConnection()) {
                Update qry = prepareUpdateSparql(conn, sparql);
                qry.setBinding("s", iri(USER.NAMESPACE + user));
                qry.setBinding("p", iri(predicate));
                qry.setBinding("o", literal(om.writeValueAsString(object)));
                qry.execute();
            }
        }
    }

    public void updateUserMRU(String user, List<RecentActivityItemDto> mru) throws JsonProcessingException {
        delete(user, USER.USER_MRU.getIri());
        insert(user, USER.USER_MRU.getIri(), mru);
    }

    public void updateUserFavourites(String user, List<String> favourites) throws JsonProcessingException {
        delete(user, USER.USER_FAVOURITES.getIri());
        insert(user, USER.USER_FAVOURITES.getIri(), favourites);
    }

    public void updateUserTheme(String user, String theme) throws JsonProcessingException {
        delete(user, USER.USER_THEME.getIri());
        insert(user, USER.USER_THEME.getIri(), theme);
    }


}