package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.GraphHelper;
import org.endeavourhealth.imapi.model.tripletree.TTEntityMap;

/**
 * Data access class for accessing information about rdf properties
 */
public class PropertyRepository {

	private PropertyRepository() {
		throw new IllegalStateException("Utility class");
	}

	public static TTEntityMap getProperty(String focusIri){
		String sql = getPropertiesSql();
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			qry.setBinding("entity", Values.iri(focusIri));
			return GraphHelper.getEntityMap(qry);
		}
	}
	/**
	 * Gets all properties from the information module e.g. for use to populate the cache
	 * @return maps from iri to shapes and predicate names for the property entity predicates.
	 * All iris referenced include their labels as names, except for the mode predicates themselves
	 */
	public static TTEntityMap getProperties(){
		String sql = getAllPropertiesSql();
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			return GraphHelper.getEntityMap(qry);
		}
	}

	public static String getPropertiesSql(){
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
			"    ?subObject ?subObPred  ?subObOb.\n"+
			"    ?superShape ?superPred ?superOb.\n" +
			"    ?superPred im:pLabel ?superPredLabel.\n" +
			"    ?superOb ?superSubPred ?superSubOb.\n" +
			"    ?superSubPred im:pLabel ?superSubPredLabel.\n" +
			"    ?superSubOb im:oLabel ?superSubObLabel.\n" +
			"    ?superSubOb ?superSubObPred ?superSubObOb.\n"+
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
			"        Optional { ?subObject rdfs:label ?subObjectLabel" +
			"                   filter(isIri(?subObject)) }\n" +
			"        Optional { ?subObject ?subObPred ?subObOb.\n"+
			"                   filter(isBlank(?subObject)) }\n" +
			"    } \n" +
			"    Optional {\n" +
			"        ?entity rdfs:subPropertyOf+ ?superShape.\n" +
			"        ?superShape rdf:type sh:NodeShape.\n"+
			"        ?superShape ?superPred ?superOb.\n" +
			"        filter(?superPred!=im:isA)\n" +
			"        Optional { ?superPred rdfs:label ?superPredLabel}\n" +
			"        Optional {\n" +
			"          ?superOb ?superSubPred ?superSubOb.\n" +
			"            filter (isBlank(?superOb))\n" +
			"            Optional { ?superSubPred rdfs:label ?superSubPredLabel}\n" +
			"            Optional { ?superSubOb rdfs:label ?superSubObLabel." +
			"                       filter(isIri(?superSubOb)}\n" +
			"            Optional { ?superSubOb ?superSubObPred ?superSubObOb.\n" +
			"                       filter(isBlank(?superSubOb)) }\n"+
			"        }}}";

	}


	private static String getAllPropertiesSql(){
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
			"    ?subObject ?subObPred ?subObOb.\n" +
			"}\n" +
			"where \n" +
			"    {?entity rdf:type rdf:PropertyRef.\n" +
			"    ?entity ?predicate ?object.\n" +
			"    filter (?predicate!=im:isA)\n" +
			"    Optional {?predicate rdfs:label ?predicateLabel}\n" +
			"    optional {\n" +
			"        ?object rdfs:label ?objectLabel." +
			"          filter (isIri(?object)) }\n" +
			"    optional {\n" +
			"        ?object ?subPredicate ?subObject.\n" +
			"        filter (isBlank(?object))\n" +
			"        Optional { ?subPredicate rdfs:label ?subPredicateLabel}\n" +
			"        Optional { ?subObject rdfs:label ?subObjectLabel.\n" +
			"                    filter(isIri(?subObject))}\n" +
			"        Optional { ?subObject ?subObPred ?subObOb.\n" +
			"                   filter (isBlank(?subObject))}\n" +
			"    } \n" +
			" }";
	}
}
