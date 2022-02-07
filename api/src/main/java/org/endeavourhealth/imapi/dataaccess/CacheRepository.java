package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

/**
 * A data access class with methods for extracting entities for the caching mechanism
 */
public class CacheRepository {


	public Set<TTBundle> getSchema(){
		String sql= getSchemaSql();
		Set<TTEntity> shapes = new HashSet<>();
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry = conn.prepareGraphQuery(sql);
			try (GraphQueryResult gs = qry.evaluate()) {
				Map<String, TTValue> valueMap = new HashMap<>();
				Map<String, TTNode> subjectMap = new HashMap<>();
				for (org.eclipse.rdf4j.model.Statement st : gs) {
					processStatement(shapes, valueMap, subjectMap, st);
				}
			}
			Set<TTIriRef> iris= new HashSet<>();
			shapes.forEach(e-> iris.addAll(TTManager.getIrisFromNode(e)));
			Map<String,String> iriNames= EntityRepository2.getIriNames(conn,iris);
			Set<TTBundle> result= new HashSet<>();
			for (TTEntity shape:shapes){
				TTBundle bundle= new TTBundle();
				bundle.setEntity(shape);
				result.add(bundle);
				Set<TTIriRef> shapeIris= TTManager.getIrisFromNode(shape);
				for (TTIriRef iri:shapeIris){
					iri.setName(iriNames.get(iri.getIri()));
					bundle.addPredicate(iri);
				}
			}
			return result;
		}

	}

	private String getSchemaSql() {
		return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
			"PREFIX im: <http://endhealth.info/im#>\n" +
			"PREFIX sh: <http://www.w3.org/ns/shacl#>Construct {\n" +
			"    ?shape ?p ?o.\n" +
			"     ?o ?p2 ?o2.\n" +
			"}\n" +
			"where  {\n" +
			" ?shape rdf:type sh:NodeShape.\n" +
			"    \t?shape ?p ?o.\n" +
			"    filter (?p != im:isA)\n" +
			"    Optional { ?o ?p2 ?o2\n" +
			"        filter (isBlank(?o) &&(?p2 in(sh:path, sh:class,sh:node,sh:datatype,sh:order,sh:nodeKind))) \n" +
			"            }}";
	}

	private void processStatement(Set<TTEntity> entities,Map<String, TTValue> valueMap, Map<String,TTNode> subjectMap, Statement st) {
		Resource s = st.getSubject();
		IRI p= st.getPredicate();
		Value o= st.getObject();
		String subject= s.stringValue();
		TTIriRef predicate= TTIriRef.iri(p.stringValue());
		String value = o.stringValue();
		TTNode node;
		if (s.isIRI()) {
			if (subjectMap.get(subject)==null) {
				TTEntity entity = new TTEntity().setIri(subject);
				subjectMap.put(subject, entity);
				entities.add(entity);
			}
			node=subjectMap.get(subject);
		}
		else {
			valueMap.putIfAbsent(subject,new TTNode());
			node=valueMap.get(subject).asNode();
			}
		if (o.isLiteral()) {
			node.set(predicate, literal(value));
		}
		else if (o.isIRI()) {
			node.addObject(predicate, TTIriRef.iri(value));
		}
		else {
			valueMap.putIfAbsent(value,new TTNode());
			node.addObject(predicate,valueMap.get(value).asNode());
		}
	}



}
