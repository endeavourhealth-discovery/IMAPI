package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.GraphHelper;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;

import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareGraphSparql;

/**
 * Data access class for accessing information about rdf properties
 */
public class PropertyRepository {

  public static final String PROPERTIES_SQL = """
    CONSTRUCT {
      ?entity ?predicate ?object.
      ?predicate im:pLabel ?predicateLabel.
      ?object im:oLabel ?objectLabel.
      ?object ?subPredicate ?subObject.
      ?subPredicate im:pLabel ?subPredicateLabel.
      ?subObject im:oLabel ?subObjectLabel.
      ?subObject ?subObPred  ?subObOb.
      ?superShape ?superPred ?superOb.
      ?superPred im:pLabel ?superPredLabel.
      ?superOb ?superSubPred ?superSubOb.
      ?superSubPred im:pLabel ?superSubPredLabel.
      ?superSubOb im:oLabel ?superSubObLabel.
      ?superSubOb ?superSubObPred ?superSubObOb.
    }
    WHERE {
      GRAPH ?g {
        ?entity rdf:type sh:NodeShape.
        ?entity ?predicate ?object.
        filter (?predicate!=im:isA)
        Optional {?predicate rdfs:label ?predicateLabel}
        OPTIONAL {
          ?object rdfs:label ?objectLabel.
          filter(isIri(?object))
        }
        OPTIONAL {
          ?object ?subPredicate ?subObject.
          filter (isBlank(?object))
          Optional { ?subPredicate rdfs:label ?subPredicateLabel}
          Optional {
            ?subObject rdfs:label ?subObjectLabel
            filter(isIri(?subObject))
          }
          Optional {
            ?subObject ?subObPred ?subObOb.
            filter(isBlank(?subObject))
          }
        }
        Optional {
          ?entity rdfs:subPropertyOf+ ?superShape.
          ?superShape rdf:type sh:NodeShape.
          ?superShape ?superPred ?superOb.
          filter(?superPred!=im:isA)
          Optional { ?superPred rdfs:label ?superPredLabel}
          Optional {
            ?superOb ?superSubPred ?superSubOb.
            filter (isBlank(?superOb))
            Optional { ?superSubPred rdfs:label ?superSubPredLabel}
            Optional {
              ?superSubOb rdfs:label ?superSubObLabel.
              filter(isIri(?superSubOb)
            }
            Optional {
              ?superSubOb ?superSubObPred ?superSubObOb.
              filter(isBlank(?superSubOb))
            }
          }
        }
      }
    }
    """;
  private static final String GET_ALL_PROPERTIES_SQL = """
    CONSTRUCT {
      ?entity ?predicate ?object.
      ?predicate im:pLabel ?predicateLabel.
      ?object im:oLabel ?objectLabel.
      ?object ?subPredicate ?subObject.
      ?subPredicate im:pLabel ?subPredicateLabel.
      ?subObject im:oLabel ?subObjectLabel.
      ?subObject ?subObPred ?subObOb.
    }
    WHERE {
      GRAPH ?g {
        ?entity rdf:type rdf:PropertyRef.
        ?entity ?predicate ?object.
        filter (?predicate!=im:isA)
        Optional {?predicate rdfs:label ?predicateLabel}
        OPTIONAL {
          ?object rdfs:label ?objectLabel.
          filter (isIri(?object))
        }
        OPTIONAL {
          ?object ?subPredicate ?subObject.
          filter (isBlank(?object))
          Optional { ?subPredicate rdfs:label ?subPredicateLabel}
          Optional {
            ?subObject rdfs:label ?subObjectLabel.
            filter(isIri(?subObject))
          }
          Optional {
            ?subObject ?subObPred ?subObOb.
            filter (isBlank(?subObject))
          }
        }
      }
    }
    """;

  private PropertyRepository() {
    throw new IllegalStateException("Utility class");
  }

  public static TTEntityMap getProperty(String focusIri, String graph) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      GraphQuery qry = prepareGraphSparql(conn, PROPERTIES_SQL, graph);
      qry.setBinding("entity", Values.iri(focusIri));
      return GraphHelper.getEntityMap(qry);
    }
  }

  /**
   * Gets all properties from the information module e.g. for use to populate the cache
   *
   * @return maps from iri to shapes and predicate names for the property entity predicates.
   * All iris referenced include their labels as names, except for the mode predicates themselves
   */
  public static TTEntityMap getProperties(String graph) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      GraphQuery qry = prepareGraphSparql(conn, GET_ALL_PROPERTIES_SQL, graph);
      return GraphHelper.getEntityMap(qry);
    }
  }
}
