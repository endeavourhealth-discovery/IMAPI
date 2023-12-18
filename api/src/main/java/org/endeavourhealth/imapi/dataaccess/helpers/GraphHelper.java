package org.endeavourhealth.imapi.dataaccess.helpers;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.HashMap;
import java.util.Map;

/**
 * Query of static methods fpr handling generic sparql query processes
 */
public class GraphHelper {

	private GraphHelper() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Runs a sparql construct query and processes the results into a TTEntity map with an iri TTEntity map
	 * @param qry Fully formed construct query with bound variables
	 * @return a map of one or many iris to TTEntity being determined by the query definition
	 */
	public  static TTEntityMap getEntityMap(GraphQuery qry) {
		TTEntityMap entityMap= new TTEntityMap();
		try (GraphQueryResult gs = qry.evaluate()) {
			Map<String, TTValue> valueMap = new HashMap<>();
			for (Statement st : gs) {
				processStatement(entityMap,valueMap,st);
			}
			return entityMap;
		}
	}

	/**
	 * Generic TTNode/ TTEntity population from a set of triple statements.
	 * <p>To use this, call sparql construct query that returns a set of entities making sure it includes blank nodes
	 * and then for each statement pass the statement in </p>
	 * @param entityMap and iri to TTEntity map for all the entities in the result set
	 * @param tripleMap  a map between a value and a TT object from the statement
	 * @param st The rdf4j triple statement
	 */
	public static void processStatement(TTEntityMap entityMap,
																			Map<String, TTValue> tripleMap, Statement st) {
		Resource s= st.getSubject();
		IRI p= st.getPredicate();
		Value o =  st.getObject();
		String subject= s.stringValue();
		String predicate= p.stringValue();
		String value = o.stringValue();
		if (predicate.equals(IM.PLABEL.iri)){
			entityMap.addPredicate(subject,value);
		}
		else if (predicate.equals(IM.OLABEL.iri)){
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

}
