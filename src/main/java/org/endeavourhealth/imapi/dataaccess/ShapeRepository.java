package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.GraphHelper;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;

import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;

public class ShapeRepository {

  public static final String GET_ALL_SHAPES_SQL = """
    CONSTRUCT {
      ?entity ?predicate ?object.
      ?predicate im:pLabel ?predicateLabel.
      ?object im:oLabel ?objectLabel.
      ?object ?subPredicate ?subObject.
      ?subPredicate im:pLabel ?subPredicateLabel.
      ?subObject im:oLabel ?subObjectLabel.
      ?superShape ?superPred ?superOb.
      ?superPred im:pLabel ?superPredLabel.
      ?superOb ?superSubPred ?superSubOb.
      ?superSubPred im:pLabel ?superSubPredLabel.
      ?superSubOb im:oLabel ?superSubObLabel.
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
            ?subObject rdfs:label ?subObjectLabel.
            filter(isIri(?subObject))
          }
        }
      }
    }
    """;
  public static final String GET_SHAPES_SQL = """
    CONSTRUCT {
      ?entity ?predicate ?object.
      ?predicate im:pLabel ?predicateLabel.
      ?object im:oLabel ?objectLabel.
      ?object ?subPredicate ?subObject.
      ?subPredicate im:pLabel ?subPredicateLabel.
      ?subObject im:oLabel ?subObjectLabel.
      ?superShape ?superPred ?superOb.
      ?superPred im:pLabel ?superPredLabel.
      ?superOb ?superSubPred ?superSubOb.
      ?superSubPred im:pLabel ?superSubPredLabel.
      ?superSubOb im:oLabel ?superSubObLabel.
    }
    WHERE {
      GRAPH ?g {
        ?entity rdf:type sh:NodeShape.
        ?entity ?predicate ?object.
        filter (?predicate!=im:isA)
        Optional {?predicate rdfs:label ?predicateLabel}
        optional {
          ?object rdfs:label ?objectLabel.
          filter(isIri(?object))
        }
        OPTIONAL {
          ?object ?subPredicate ?subObject.
          filter (isBlank(?object))
          Optional { ?subPredicate rdfs:label ?subPredicateLabel}
          Optional {
            ?subObject rdfs:label ?subObjectLabel.
            filter(isIri(?subObject))
          }
        }
        Optional {
          ?entity rdfs:subClassOf+ ?superShape.
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
              filter (isIri(?superSubOb))
            }
          }
        }
      }
    }
    """;


  private ShapeRepository() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Gets all shapes from the information module e.g. for use to populate the cache
   *
   * @return maps from iri to shapes and predicate names for the Node shape predicates.
   * All iris referenced include their labels as names, except for the mode predicates themselves
   */
  public static TTEntityMap getShapes() {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      GraphQuery qry = conn.prepareGraphQuery(addSparqlPrefixes(GET_ALL_SHAPES_SQL));
      return GraphHelper.getEntityMap(qry);
    }
  }

  /**
   * Returns a set of iri to shape maps consisting of an optional focus shape and its ancestors
   * This is used to enable properties of shapes to be calculated from the super shjapes.
   * Includes the map of predicate names for the shape
   * All iris referenced include their labels as names, except for the mode predicates themselves
   *
   * @param focusIri the iri for the shape of interest. Null if all shapes
   * @return a set of iri to shape maps and a map of predoicate names
   */
  public static TTEntityMap getShapeAndAncestors(String focusIri) {

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      GraphQuery qry = conn.prepareGraphQuery(addSparqlPrefixes(GET_SHAPES_SQL));
      qry.setBinding("entity", Values.iri(focusIri));
      return GraphHelper.getEntityMap(qry);
    }
  }
}
