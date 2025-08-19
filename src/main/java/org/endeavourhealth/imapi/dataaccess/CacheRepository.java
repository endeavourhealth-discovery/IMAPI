package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

/**
 * A data access class with methods for extracting entities for the caching mechanism
 */
public class CacheRepository {

  public Set<TTBundle> getSchema() throws Exception {
    String sql = """
      CONSTRUCT {
        ?shape ?p ?o.
        ?o ?p2 ?o2.
      }
      WHERE {
        ?shape rdf:type sh:NodeShape.
        ?shape ?p ?o.
        filter (?p != im:isA)
        OPTIONAL { ?o ?p2 ?o2
          FILTER (
            isBlank(?o) &&
            (?p2 in(sh:path, sh:class,sh:node,sh:datatype,sh:order,sh:nodeKind))
          )
        }
      }
      """;
    Set<TTEntity> shapes = new HashSet<>();
    try (IMDB conn = IMDB.getConnection()) {
      GraphQuery qry = conn.prepareGraphSparql(sql);
      try (GraphQueryResult gs = qry.evaluate()) {
        Map<String, TTValue> valueMap = new HashMap<>();
        Map<String, TTNode> subjectMap = new HashMap<>();
        for (org.eclipse.rdf4j.model.Statement st : gs) {
          processStatement(shapes, valueMap, subjectMap, st);
        }
      }
      Set<TTIriRef> iris = new HashSet<>();
      shapes.forEach(e -> iris.addAll(TTManager.getIrisFromNode(e)));
      EntityRepository.getIriNames(conn, iris);
      Set<TTBundle> result = new HashSet<>();
      for (TTEntity shape : shapes) {
        TTBundle bundle = new TTBundle();
        bundle.setEntity(shape);
        result.add(bundle);
        Set<TTIriRef> shapeIris = TTManager.getIrisFromNode(shape);
        for (TTIriRef iri : shapeIris) {
          bundle.addPredicate(iri);
        }
      }
      return result;
    }

  }

  private void processStatement(Set<TTEntity> entities, Map<String, TTValue> valueMap, Map<String, TTNode> subjectMap, Statement st) {
    Resource s = st.getSubject();
    IRI p = st.getPredicate();
    Value o = st.getObject();
    String subject = s.stringValue();
    TTIriRef predicate = TTIriRef.iri(p.stringValue());
    String value = o.stringValue();
    TTNode node;
    if (s.isIRI()) {
      subjectMap.computeIfAbsent(subject, k -> {
        TTEntity entity = new TTEntity().setIri(k);
        entities.add(entity);
        return entity;
      });
      node = subjectMap.get(subject);
    } else {
      valueMap.putIfAbsent(subject, new TTNode());
      node = valueMap.get(subject).asNode();
    }
    if (o.isLiteral()) {
      node.set(predicate, literal(value));
    } else if (o.isIRI()) {
      node.addObject(predicate, TTIriRef.iri(value));
    } else {
      valueMap.putIfAbsent(value, new TTNode());
      node.addObject(predicate, valueMap.get(value).asNode());
    }
  }


}
