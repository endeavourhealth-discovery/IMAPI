package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.GraphHelper;
import org.endeavourhealth.imapi.model.tripletree.*;

public class ShapeRepository {

	private ShapeRepository(){
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Gets all shapes from the information module e.g. for use to populate the cache
	 * @return maps from iri to shapes and predicate names for the Node shape predicates.
	 * All iris referenced include their labels as names, except for the mode predicates themselves
	 */
	public static TTEntityMap getShapes(){
		String sql = getAllShapesSql();
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			return GraphHelper.getEntityMap(qry);
		}
	}


	/**
	 * Returns a set of iri to shape maps consisting of an optional focus shape and its ancestors
	 * This is used to enable properties of shapes to be calculated from the super shjapes.
	 * Includes the map of predicate names for the shape
	 * All iris referenced include their labels as names, except for the mode predicates themselves
	 * @param focusIri the iri for the shape of interest. Null if all shapes
	 * @return a set of iri to shape maps and a map of predoicate names
	 */
	public static TTEntityMap getShapeAndAncestors(String focusIri) {

		String sql = getShapesSql();
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			qry.setBinding("entity", Values.iri(focusIri));
			return GraphHelper.getEntityMap(qry);
		}
	}

	public static String getAllShapesSql(){
		return "PREFIX im: <http://endhealth.info/im#>\n" +
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
			"PREFIX sh: <http://www.w3.org/ns/shacl#>\n" +
			"CONSTRUCT {\n" +
			"    ?entity ?predicate ?object.\n" +
			"    ?predicate im:pLabel ?predicateLabel.\n" +
			"    ?object im:oLabel ?objectLabel.\n" +
			"    ?object ?subPredicate ?subObject.\n" +
			"    ?subPredicate im:pLabel ?subPredicateLabel.\n" +
			"    ?subObject im:oLabel ?subObjectLabel.\n" +
			"    ?superShape ?superPred ?superOb.\n" +
			"    ?superPred im:pLabel ?superPredLabel.\n" +
			"    ?superOb ?superSubPred ?superSubOb.\n" +
			"    ?superSubPred im:pLabel ?superSubPredLabel.\n" +
			"    ?superSubOb im:oLabel ?superSubObLabel.\n" +
			"}\n" +
			"where \n" +
			"    {?entity rdf:type sh:NodeShape.\n" +
			"    ?entity ?predicate ?object.\n" +
			"    filter (?predicate!=im:isA)\n" +
			"    Optional {?predicate rdfs:label ?predicateLabel}\n" +
			"    optional {\n" +
			"        ?object rdfs:label ?objectLabel.\n"+
	    "            filter(isIri(?object))}\n" +
			"    optional {\n" +
			"        ?object ?subPredicate ?subObject.\n" +
			"        filter (isBlank(?object))\n" +
			"        Optional { ?subPredicate rdfs:label ?subPredicateLabel}\n" +
			"        Optional { ?subObject rdfs:label ?subObjectLabel." +
			"                      filter(isIri(?subObject)) }\n" +
			"    } \n" +
			" }";
	}

	public static String getShapesSql(){
		return "PREFIX im: <http://endhealth.info/im#>\n" +
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
			"PREFIX sh: <http://www.w3.org/ns/shacl#>\n" +
			"CONSTRUCT {\n" +
			"    ?entity ?predicate ?object.\n" +
			"    ?predicate im:pLabel ?predicateLabel.\n" +
			"    ?object im:oLabel ?objectLabel.\n" +
			"    ?object ?subPredicate ?subObject.\n" +
			"    ?subPredicate im:pLabel ?subPredicateLabel.\n" +
			"    ?subObject im:oLabel ?subObjectLabel.\n" +
			"    ?superShape ?superPred ?superOb.\n" +
			"    ?superPred im:pLabel ?superPredLabel.\n" +
			"    ?superOb ?superSubPred ?superSubOb.\n" +
			"    ?superSubPred im:pLabel ?superSubPredLabel.\n" +
			"    ?superSubOb im:oLabel ?superSubObLabel.\n" +
			"}\n" +
			"where \n" +
			"    {?entity rdf:type sh:NodeShape.\n" +
			"    ?entity ?predicate ?object.\n" +
			"    filter (?predicate!=im:isA)\n" +
			"    Optional {?predicate rdfs:label ?predicateLabel}\n" +
			"    optional {\n" +
			"        ?object rdfs:label ?objectLabel.\n"+
			"            filter(isIri(?object))}\n" +
			"    optional {\n" +
			"        ?object ?subPredicate ?subObject.\n" +
			"        filter (isBlank(?object))\n" +
			"        Optional { ?subPredicate rdfs:label ?subPredicateLabel}\n" +
			"        Optional { ?subObject rdfs:label ?subObjectLabel.\n"+
	    "                    filter(isIri(?subObject))}\n" +
			"    } \n" +
			"    Optional {\n" +
			"        ?entity rdfs:subClassOf+ ?superShape.\n" +
			"        ?superShape rdf:type sh:NodeShape.\n"+
			"        ?superShape ?superPred ?superOb.\n" +
			"        filter(?superPred!=im:isA)\n" +
			"        Optional { ?superPred rdfs:label ?superPredLabel}\n" +
			"        Optional {\n" +
			"          ?superOb ?superSubPred ?superSubOb.\n" +
			"            filter (isBlank(?superOb))\n" +
			"            Optional { ?superSubPred rdfs:label ?superSubPredLabel}\n" +
			"            Optional { ?superSubOb rdfs:label ?superSubObLabel.\n" +
			"                        filter (isIri(?superSubOb))}\n" +
			"        }}}";

	}
}
