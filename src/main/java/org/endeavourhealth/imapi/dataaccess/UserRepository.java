package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.databases.UserDB;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.USER;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArrayList;

public class UserRepository {

  public String getSparqlSelect() {
    StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("SELECT ?o WHERE { ").add("  ?s ?p ?o").add("}");
    return sparql.toString();
  }

  public String getSparqlDelete() {
    StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("DELETE WHERE { ").add("  ?s ?p ?o").add("}");
    return sparql.toString();
  }

  public String getSparqlInsert() {
    StringJoiner sparql = new StringJoiner(System.lineSeparator()).add("INSERT {").add("  ?s ?p ?o ").add("}").add("WHERE { SELECT ?s ?p ?o {} }");
    return sparql.toString();
  }

  public String getByPredicate(String user, VocabEnum predicate) {
    return getByPredicate(user, predicate.toString());
  }

  public String getByPredicate(String user, String predicate) {
    String result = "";
    String sparql = getSparqlSelect();
    try (UserDB conn = UserDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(Namespace.USER + user));
      qry.setBinding("p", iri(predicate));

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

    try (UserDB conn = UserDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(Namespace.USER + user));
      qry.setBinding("p", USER.USER_MRU.asDbIri());
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
    List<String> result = new ArrayList<>();
    String sparql = getSparqlSelect();

    try (UserDB conn = UserDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(Namespace.USER + user));
      qry.setBinding("p", USER.USER_FAVOURITES.asDbIri());
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

  public void delete(String user, VocabEnum predicate) {
    String sparql = getSparqlDelete();
    try (UserDB conn = UserDB.getConnection()) {
      Update qry = conn.prepareDeleteSparql(sparql);
      qry.setBinding("s", iri(Namespace.USER + user));
      qry.setBinding("p", predicate.asDbIri());
      qry.execute();
    }
  }

  public void insert(String user, VocabEnum predicate, Object object) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      String sparql = getSparqlInsert();
      try (UserDB conn = UserDB.getConnection()) {
        Update qry = conn.prepareInsertSparql(sparql);
        qry.setBinding("s", iri(Namespace.USER + user));
        qry.setBinding("p", predicate.asDbIri());
        qry.setBinding("o", literal(om.writeValueAsString(object)));
        qry.execute();
      }
    }
  }

  public void updateUserMRU(String user, List<RecentActivityItemDto> mru) throws JsonProcessingException {
    if (mru.isEmpty()) {
      delete(user, USER.USER_MRU);
      return;
    }
    if (mru.stream().allMatch(this::isValidRecentActivityItem)) {
      delete(user, USER.USER_MRU);
      insert(user, USER.USER_MRU, mru);
    } else throw new IllegalArgumentException("One or more activity items are invalid");
  }

  private boolean isValidRecentActivityItem(RecentActivityItemDto item) {
    return !item.getIri().isEmpty() && !item.getAction().isEmpty() && null != item.getDateTime();
  }

  public void updateUserFavourites(String user, List<String> favourites) throws JsonProcessingException {
    delete(user, USER.USER_FAVOURITES);
    insert(user, USER.USER_FAVOURITES, favourites);
  }

  public void updateByPredicate(String user, String data, VocabEnum predicate) throws JsonProcessingException {
    delete(user, predicate);
    insert(user, predicate, data);
  }

  public void updateByPredicate(String user, Boolean data, VocabEnum predicate) throws JsonProcessingException {
    updateByPredicate(user, data.toString(), predicate);
  }

  public List<String> getUserOrganisations(String user) throws JsonProcessingException {
    List<String> result = asArrayList(Namespace.IM);
    String sparql = getSparqlSelect();
    try (UserDB conn = UserDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("s", iri(Namespace.USER + user));
      qry.setBinding("p", USER.ORGANISATIONS.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          try (CachedObjectMapper om = new CachedObjectMapper()) {
            List<String> organisations = om.readValue(bs.getValue("o").stringValue(), new TypeReference<>() {
            });
            if (!organisations.isEmpty()) return organisations;
          }
        }
      }
    }

    return result;
  }

  public void updateUserOrganisations(String user, List<String> organisations) throws JsonProcessingException {
    delete(user, USER.ORGANISATIONS);
    insert(user, USER.ORGANISATIONS, organisations);
  }

  public boolean getUserIdExists(String userId) {
    try (UserDB conn = UserDB.getConnection()) {
      String sparql = "ASK { ?s ?p ?o. }";
      BooleanQuery qry = conn.prepareBooleanSparql(sparql);
      qry.setBinding("s", iri(Namespace.USER + userId));
      return qry.evaluate();
    }
  }
}
