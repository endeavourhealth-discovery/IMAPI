package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.HashMap;
import java.util.Map;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class ShapeRepository {

	/**
	 * Gets all shapes from the information module e.g. for use to populate the cache
	 * @return maps from iri to shapes and predicate names for the Node shape predicates.
	 * All iris referenced include their labels as names, except for the mode predicates themselves
	 */
	public static TTEntityMap getShapes(){
		TTEntityMap entityMap= new TTEntityMap();
		Map<String,String> predNames= new HashMap<>();
		String sql = getAllShapesSql();
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			try (GraphQueryResult gs = qry.evaluate()) {
				Map<String, TTValue> valueMap = new HashMap<>();
				for (org.eclipse.rdf4j.model.Statement st : gs) {
					processStatement(entityMap,valueMap,st);
				}
				return entityMap;
			}
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
		TTEntityMap entityMap= new TTEntityMap();
		Map<String,String> predNames= new HashMap<>();
		String sql = getShapesSql();
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			qry.setBinding("entity", Values.iri(focusIri));
			try (GraphQueryResult gs = qry.evaluate()) {
				Map<String, TTValue> valueMap = new HashMap<>();
				for (org.eclipse.rdf4j.model.Statement st : gs) {
					processStatement(entityMap,valueMap,st);
				}
				return entityMap;
			}
		}
	}


	private static void processStatement(TTEntityMap entityMap,
																			 Map<String,TTValue> tripleMap,Statement st) {
		Resource s= st.getSubject();
		IRI p= st.getPredicate();
		Value o =  st.getObject();
		String subject= s.stringValue();
		String predicate= p.stringValue();
		String value = o.stringValue();
		if (predicate.equals(IM.PLABEL.getIri())){
			entityMap.addPredicate(subject,value);
		}
		else if (predicate.equals(IM.OLABEL.getIri())){
			tripleMap.putIfAbsent(subject, TTIriRef.iri(subject));
			tripleMap.get(subject).asIriRef().setName(value);
		}
		else {
			TTNode node;
			tripleMap.putIfAbsent(predicate,TTIriRef.iri(predicate));
			if (s.isIRI()) {
				entityMap.getEntities().putIfAbsent(subject, new TTEntity().setIri(subject));
				node = entityMap.getEntities().get(subject);
			}
			else {
				tripleMap.putIfAbsent(subject,new TTNode());
				node= tripleMap.get(subject).asNode();
			}
			if (o.isBNode()){
				tripleMap.putIfAbsent(value,new TTNode());
				node.addObject(tripleMap.get(predicate).asIriRef(),tripleMap.get(value));
			}
			else if (o.isIRI()){
				tripleMap.putIfAbsent(value,TTIriRef.iri(value));
				node.addObject(tripleMap.get(predicate).asIriRef(),tripleMap.get(value));
			}
			else {
				tripleMap.putIfAbsent(value,TTLiteral.literal(value));
				node.set(tripleMap.get(predicate).asIriRef(),tripleMap.get(value).asLiteral());
			}
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
			"        ?object rdfs:label ?objectLabel}\n" +
			"    optional {\n" +
			"        ?object ?subPredicate ?subObject.\n" +
			"        filter (isBlank(?object))\n" +
			"        Optional { ?subPredicate rdfs:label ?subPredicateLabel}\n" +
			"        Optional { ?subObject rdfs:label ?subObjectLabel}\n" +
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
			"        ?object rdfs:label ?objectLabel}\n" +
			"    optional {\n" +
			"        ?object ?subPredicate ?subObject.\n" +
			"        filter (isBlank(?object))\n" +
			"        Optional { ?subPredicate rdfs:label ?subPredicateLabel}\n" +
			"        Optional { ?subObject rdfs:label ?subObjectLabel}\n" +
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
			"            Optional { ?superSubOb rdfs:label ?superSubObLabel}\n" +
			"        }}}";

	}
}
