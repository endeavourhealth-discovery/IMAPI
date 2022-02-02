package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class EntityRepositoryImpl3 {

	/**
	 * Returns a full entity (without isa predicates) including blank nodes to level 5
	 * and including named IRIs
	 * @param iri of the entity
	 * @return the entity
	 */
	public TTEntity getFullEntity(String iri){
		StringJoiner sql = getFullSparql();

		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql.toString());
			qry.setBinding("entity", iri(iri));
			try (GraphQueryResult gs = qry.evaluate()) {
				Map<String, TTValue> tripleMap = new HashMap<>();
				TTEntity entity= new TTEntity().setIri(iri);
				for (org.eclipse.rdf4j.model.Statement st : gs) {
					processTriple(entity,tripleMap, st);
				}
				return entity;
			}
		}
	}

	private StringJoiner getFullSparql() {
		int depth = 5;

		StringJoiner sql = new StringJoiner("\n");
		sql.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
		sql.add("CONSTRUCT {")
			.add("  ?entity ?1predicate ?1Level.")
			.add("  ?1Level rdfs:label ?1Name.");
		for (int i = 1; i < depth; i++) {
			sql.add("  ?" + i + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
				.add("  ?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName.")
				.add("  ?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name.");
		}
		sql.add("} WHERE {");

		sql.add("  ?entity ?1predicate ?1Level.")
			.add("  ?1predicate rdfs:label ?1pName.");
		sql.add("   FILTER (?1predicate!=<"+ IM.IS_A.getIri()+">)");
		sql.add("  OPTIONAL {?1Level rdfs:label ?1Name.")
			.add("    FILTER (!isBlank(?1Level))}");
		for (int i = 1; i < depth; i++) {
			sql.add("  OPTIONAL {?" + (i) + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
				.add("    FILTER (isBlank(?" + i + "Level))")
				.add("  OPTIONAL {?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName}")
				.add("  OPTIONAL {?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name")
				.add("    FILTER (!isBlank(?" + (i + 1) + "Level))}");
		}
		sql.add("}}}}}");
		return sql;
	}

	private void processTriple(TTEntity entity,Map<String, TTValue> tripleMap,Statement st) {
		String entityIri= entity.getIri();
		Resource s= st.getSubject();
		IRI p= st.getPredicate();
		Value o =  st.getObject();
		String subject= s.stringValue();
		String predicate= p.stringValue();
		String value = o.stringValue();
		tripleMap.putIfAbsent(predicate, TTIriRef.iri(predicate));
		TTNode node;
		if (predicate.equals(RDFS.LABEL.getIri())) {
			if (s.isIRI()) {
				if (!subject.equals(entityIri)) {
					tripleMap.putIfAbsent(subject, TTIriRef.iri(subject));
					tripleMap.get(subject).asIriRef().setName(value);
				}
			} else {
				tripleMap.putIfAbsent(subject, new TTNode());
				tripleMap.get(subject).asNode().set(RDFS.LABEL, TTLiteral.literal(value));
			}
		}
		else {
			if (s.isIRI()) {
				node = entity;
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
