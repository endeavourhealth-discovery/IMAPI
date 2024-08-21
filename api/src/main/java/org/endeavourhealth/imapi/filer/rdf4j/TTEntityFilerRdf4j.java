package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.ValidatingValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.*;

public class TTEntityFilerRdf4j implements TTEntityFiler {
  private static final Logger LOG = LoggerFactory.getLogger(TTEntityFilerRdf4j.class);
  private static final ValueFactory valueFactory = new ValidatingValueFactory(SimpleValueFactory.getInstance());
  private final Map<String, String> prefixMap;
  private final Update deleteTriples;
  String blockers = "<http://snomed.info/sct#138875005>,<" + IM.NAMESPACE + "Concept>";
  private RepositoryConnection conn;

  public TTEntityFilerRdf4j(RepositoryConnection conn, Map<String, String> prefixMap) {
    this.conn = conn;
    this.prefixMap = prefixMap;
    String sparql = """
      DELETE {
        ?concept ?p1 ?o1.
        ?o1 ?p2 ?o2.
        ?o2 ?p3 ?o3.
        ?o3 ?p4 ?o4.
        ?o4 ?p5 ?o5.
      }
      where {
        GRAPH ?graph {
          ?concept ?p1 ?o1.
          OPTIONAL {
            ?o1 ?p2 ?o2.
            filter (isBlank(?o1))
            OPTIONAL {
              ?o2 ?p3 ?o3
              filter (isBlank(?o2))
              OPTIONAL {
                ?o3 ?p4 ?o4.
                filter(isBlank(?o3))
                OPTIONAL {
                  ?o4 ?p5 ?o5
                  filter(isBlank(?o4))
                }
              }
            }
          }
        }
      }
      """;
    deleteTriples = conn.prepareUpdate(sparql);
  }

  public TTEntityFilerRdf4j() {
    this(ConnectionManager.getIMConnection(), new HashMap<>());
  }

  @Override
  public void fileEntity(TTEntity entity, TTIriRef graph) throws TTFilerException {

    if (entity.get(TTIriRef.iri(RDFS.LABEL)) != null) {
      if (entity.get(TTIriRef.iri(IM.HAS_STATUS)) == null)
        entity.set(TTIriRef.iri(IM.HAS_STATUS), IM.ACTIVE);
      if (entity.get(TTIriRef.iri(IM.HAS_SCHEME)) == null)
        entity.set(TTIriRef.iri(IM.HAS_SCHEME), graph);
    }
    if (entity.getCrud().equals(TTIriRef.iri(IM.UPDATE_PREDICATES)))
      updatePredicates(entity, graph);
    else if (entity.getCrud().equals(TTIriRef.iri(IM.ADD_QUADS)))
      addQuads(entity, graph);
    else if (entity.getCrud().equals(TTIriRef.iri(IM.UPDATE_ALL)))
      replacePredicates(entity, graph);
    else if (entity.getCrud().equals(TTIriRef.iri(IM.DELETE_ALL)))
      deleteTriples(entity, graph);
    else
      throw new TTFilerException("Entity " + entity.getIri() + " has no crud assigned");

  }

  @Override
  public void updateIsAs(String entity) {
    throw new UnsupportedOperationException("TTEntityFilerRdf4j does not support updateIsAs");
  }

  public void deleteIsas(Set<String> entities) {
    LOG.info("Deleting descendant and ascendant isas");
    for (String entity : entities) {
      String deleteSql = """
        DELETE {
          ?descendant ?isA ?allAncestors.
          ?entity ?isA ?ancestors.
        }
        WHERE {
          ?descendant ?isA ?entity.
          filter (?entity = ?entity)
        }
        """;
      Update deleteIsas = conn.prepareUpdate(deleteSql);
      deleteIsas.setBinding("isA", iri(IM.IS_A));
      deleteIsas.setBinding("entity", iri(entity));
      deleteIsas.execute();
    }

  }

  public Set<TTEntity> getDescendants(Set<String> entities) {
    Set<TTEntity> descendants = new HashSet<>();
    Map<String, TTEntity> entityMap = new HashMap<>();
    for (String entity : entities) {
      String sparql = """
        SELECT ?descendant ?superclass
        WHERE {
          ?descendant ?isA ?entity.
          ?descendant ?subClassOf ?superclass.
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(sparql);
      qry.setBinding("entity", iri(entity));
      qry.setBinding("subClassOf", iri(RDFS.SUBCLASS_OF));
      qry.setBinding("isA", iri(IM.IS_A));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String descendantIri = bs.getValue("descendant").stringValue();
          TTEntity descendant = entityMap.get(descendantIri);
          if (descendant == null) {
            descendant = new TTEntity();
            descendant.setIri(descendantIri);
            entityMap.put(descendantIri, descendant);
          }
          descendant.addObject(TTIriRef.iri(RDFS.SUBCLASS_OF), TTIriRef.iri(bs.getValue("superclass").stringValue()));
        }
      }
    }
    return descendants;

  }

  public Set<String> getIsAs(String superClass) {
    Set<String> isAs = new HashSet<>();
    StringJoiner getIsas = new StringJoiner("\n");
    getIsas
      .add("Select distinct ?ancestor")
      .add("Where {")
      .add("<" + superClass + "> <" + IM.IS_A + "> ?ancestor")
      .add("filter (?ancestor not in (" + blockers + "))}");
    TupleQuery qry = conn.prepareTupleQuery(getIsas.toString());
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        isAs.add(bs.getValue("ancestor").stringValue());
      }
    }
    return isAs;
  }

  @Override
  public void fileIsAs(Map<String, Set<String>> isAs) {
    int count = 0;
    for (Map.Entry<String, Set<String>> child : isAs.entrySet()) {
      count++;
      StringJoiner addSql = new StringJoiner("\n")
        .add("INSERT DATA {");
      for (String ancestor : isAs.get(child.getKey())) {
        addSql.add("<" + child.getKey() + "> <" + IM.IS_A + "> <" + ancestor + ">.");
      }
      addSql.add("}");
      Update addIsAs = conn.prepareUpdate(addSql.toString());
      addIsAs.execute();
      if (count % 100 == 0) {
        LOG.info("isas added for {} entities", count);
      }
    }
  }


  private void replacePredicates(TTEntity entity, TTIriRef graph) throws TTFilerException {
    deleteTriples(entity, graph);
    addQuads(entity, graph);
  }

  private void addQuads(TTEntity entity, TTIriRef graph) throws TTFilerException {
    try {
      ModelBuilder builder = new ModelBuilder();
      builder = builder.namedGraph(graph.getIri());
      for (Map.Entry<TTIriRef, TTArray> entry : entity.getPredicateMap().entrySet()) {
        addTriple(builder, toIri(entity.getIri()), toIri(entry.getKey().getIri()), entry.getValue());
      }
      conn.add(builder.build());
    } catch (RepositoryException | TTFilerException e) {
      throw new TTFilerException("Failed to file entities " + e.getMessage());
    }

  }

  private void deleteTriples(TTEntity entity, TTIriRef graph) throws TTFilerException {
    try {
      deleteTriples.setBinding("concept", valueFactory.createIRI(entity.getIri()));
      deleteTriples.setBinding("graph", valueFactory.createIRI(graph.getIri()));
      deleteTriples.execute();
    } catch (Exception e) {
      throw new TTFilerException("Failed to delete triples : " + e.getMessage());
    }

  }


  private void deletePredicates(TTEntity entity, TTIriRef graph) throws TTFilerException {
    StringBuilder predList = new StringBuilder();
    int i = 0;
    Map<TTIriRef, TTArray> predicates = entity.getPredicateMap();
    for (Map.Entry<TTIriRef, TTArray> po : predicates.entrySet()) {
      String predicateIri = po.getKey().getIri();
      i++;
      if (i > 1)
        predList.append(", ");
      predList.append("<").append(predicateIri).append(">");
    }
    String spq = """
        DELETE {
          ?concept ?p1 ?o1.
          ?o1 ?p2 ?o2.
          ?o2 ?p3 ?o3.
          ?o3 ?p4 ?o4.
        }
        WHERE {
          graph ?graphIri {
            {
              ?concept ?p1 ?o1.
              filter(?p1 in(?predList))
              OPTIONAL {
                ?o1 ?p2 ?o2.
                filter (isBlank(?o1))
                OPTIONAL {
                  ?o2 ?p3 ?o3
                  filter (isBlank(?o2))
                  OPTIONAL {
                    ?o3 ?p4 ?o4.
                    filter(!isBlank(?o3))
                  }
                }
              }
            }
          }
        }
      """;
    Update deletePredicates = conn.prepareUpdate(spq);
    deletePredicates.setBinding("graphIri", iri(graph.getIri()));
    deletePredicates.setBinding("predList", literal(predList.toString()));
    deletePredicates.setBinding("concept", valueFactory.createIRI(entity.getIri()));
    try {
      deletePredicates.execute();
    } catch (RepositoryException e) {
      throw new TTFilerException("Failed to delete triples");
    }

  }

  private void updatePredicates(TTEntity entity, TTIriRef graph) throws TTFilerException {

    //Deletes the previous predicate values and adds in the new ones
    deletePredicates(entity, graph);
    addQuads(entity, graph);
  }

  private void addTriple(ModelBuilder builder, Resource subject, IRI predicate, TTArray array) throws TTFilerException {
    for (TTValue value : array.iterator()) {
      addTriple(builder, subject, predicate, value);
    }
  }

  private void addTriple(ModelBuilder builder, Resource subject, IRI predicate, TTValue value) throws TTFilerException {
    if (value.isLiteral()) {
      if (null != value.asLiteral().getValue())
        builder.add(subject, predicate, value.asLiteral().getType() == null
          ? literal(value.asLiteral().getValue())
          : literal(value.asLiteral().getValue(), toIri(value.asLiteral().getType().getIri())));
    } else if (value.isIriRef()) {
      builder.add(subject, predicate, toIri(value.asIriRef().getIri()));
    } else if (value.isNode()) {
      TTNode node = value.asNode();
      BNode bNode = bnode();
      builder.add(subject, predicate, bNode);
      for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
        addTriple(builder, bNode, toIri(entry.getKey().getIri()), entry.getValue());
      }
    } else {
      throw new TTFilerException("Arrays of arrays not allowed ");
    }
  }

  private IRI toIri(String iri) throws TTFilerException {
    iri = expand(iri);
    if (iri.startsWith("urn")) {
      return iri(iri);
    }

    try {

      String decodedURL = URLDecoder.decode(iri, StandardCharsets.UTF_8);
      URL url = new URL(decodedURL);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
      String result = uri.toASCIIString();


      if (!iri.equals(result))
        LOG.trace("Encoded iri [{}] => [{}]", iri, result);

      return iri(result);
    } catch (MalformedURLException | URISyntaxException e) {
      throw new TTFilerException("Unable to encode iri", e);
    }
  }

  public String expand(String iri) throws TTFilerException {
    if (prefixMap == null)
      return iri;
    try {
      int colonPos = iri.indexOf(":");
      String prefix = iri.substring(0, colonPos);
      String path = prefixMap.get(prefix);
      if (path == null)
        return iri;
      else
        return path + iri.substring(colonPos + 1);
    } catch (StringIndexOutOfBoundsException e) {
      LOG.debug("invalid iri [{}]", iri);
      throw new TTFilerException("Invalid iri format (" + iri + ")");
    }
  }

}
