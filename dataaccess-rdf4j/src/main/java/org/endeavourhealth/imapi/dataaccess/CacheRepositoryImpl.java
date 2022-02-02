package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

/**
 * A data access class with methods for extracting entities for the caching mechanism
 */
public class CacheRepositoryImpl {

	/**
	 * Returns a single shape entity from IM
	 * @param iri iri of the main (child shape)
	 * @return a set of shapes with the superclass shapes
	 */
	public Map<String,TTEntity> getShapeAndSupers(String iri) {
		String sql = getShapeSql();
		Set<TTEntity> shapes = new HashSet<>();
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			qry.setBinding("shape",iri(iri));
			try (GraphQueryResult gs = qry.evaluate()) {
				Map<String, TTValue> valueMap = new HashMap<>();
				Map<String, TTNode> subjectMap = new HashMap<>();
				for (org.eclipse.rdf4j.model.Statement st : gs) {
					processTriples(shapes, valueMap, subjectMap, st);
				}
			}
		}
		if (shapes.isEmpty())
			return null;
		Map<String,TTEntity> shapeMap= new HashMap<>();
		shapes.forEach(s-> shapeMap.put(s.getIri(),s));
		return shapeMap;
	}

	public Set<TTEntity> getSchema(){
		String sql= getSchemaSql();
		Set<TTEntity> shapes = new HashSet<>();
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			try (GraphQueryResult gs = qry.evaluate()) {
				Map<String, TTValue> valueMap = new HashMap<>();
				Map<String, TTNode> subjectMap = new HashMap<>();
				for (org.eclipse.rdf4j.model.Statement st : gs) {
					processTriples(shapes, valueMap, subjectMap, st);
				}
			}
			return shapes;
		}

	}

	private String getShapeSql(){
		return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
			"PREFIX im: <http://endhealth.info/im#>\n" +
			"PREFIX sh: <http://www.w3.org/ns/shacl#>\n" +
			"Construct {\n" +
			"    ?shape ?p ?o.\n" +
			"    ?o ?p2 ?o2.\n" +
			"    ?super ?sp ?so.\n" +
			"    ?so ?sp2 ?so2.\n" +
			"}\n" +
			"where \n" +
			"\t{?shape ?p ?o.\n" +
			"    filter (?p != im:isA)\n" +
			"       Optional { ?o ?p2 ?o2\n" +
			"        filter (isBlank(?o))}\n" +
			"    Optional { ?shape (rdfs:subClassOf)+ ?super.\n" +
			"        ?super rdf:type sh:NodeShape." +
			"        ?super ?sp ?so.\n" +
			"        filter(?sp!=im:isA)\n" +
			"        Optional {?so ?sp2 ?so2.\n" +
			"            filter(isBlank(?so))}\n" +
			"        }\n" +
			"}";
	}

	private String getSchemaSql() {
		return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
			"PREFIX im: <http://endhealth.info/im#>\n" +
			"PREFIX sh: <http://www.w3.org/ns/shacl#>" +
			"Construct {\n" +
			"    ?shape ?p ?o.\n" +
			"    ?o ?p2 ?o2.\n" +
			"}\n" +
			"where \n" +
			"\t{\t?shape rdf:type sh:NodeShape.\n" +
			"    \t?shape ?p ?o.\n" +
			"    filter (?p != im:isA)\n" +
			"       Optional { ?o ?p2 ?o2\n" +
			"        filter (isBlank(?o))}    \n" +
			"}";
	}

	private void processTriples(Set<TTEntity> entities, Map<String, TTValue> valueMap, Map<String,TTNode> subjectMap, Statement st) {
		Resource subject = st.getSubject();
		TTIriRef predicate = TTIriRef.iri(st.getPredicate().stringValue());
		Value object = st.getObject();
		TTNode node = subjectMap.get(subject.stringValue());
		if (node == null) {
			if (subject.isIRI()) {
				TTEntity entity = new TTEntity().setIri(subject.stringValue());
				subjectMap.put(subject.stringValue(), entity);
				entities.add(entity);
			}
			else
				subjectMap.put(subject.stringValue(), new TTNode());
		}
		node= subjectMap.get(subject.stringValue());
		if (object.isLiteral()) {
			Literal l = (Literal) object;
			node.set(predicate, literal(l.stringValue(), l.getDatatype().stringValue()));
		}
		else if (object.isIRI()) {
			node.addObject(predicate, TTIriRef.iri(object.stringValue()));
		}
		else {
			if (valueMap.get(object.stringValue()) == null)
				if (subjectMap.get(object.stringValue())!=null)
					valueMap.put(object.stringValue(),subjectMap.get(object.stringValue()));
				else {
					valueMap.put(object.stringValue(), new TTNode());
					subjectMap.put(object.stringValue(), valueMap.get(object.stringValue()).asNode());
				}
			subjectMap.put(object.stringValue(), valueMap.get(object.stringValue()).asNode());
			node.addObject(predicate,valueMap.get(object.stringValue()).asNode());
		}
	}


}
