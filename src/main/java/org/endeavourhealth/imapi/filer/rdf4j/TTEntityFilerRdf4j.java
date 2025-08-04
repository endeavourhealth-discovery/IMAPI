package org.endeavourhealth.imapi.filer.rdf4j;

import lombok.extern.slf4j.Slf4j;
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
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.databases.BaseDB;
import org.endeavourhealth.imapi.filer.TTEntityFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.types.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.*;

@Slf4j
public class TTEntityFilerRdf4j implements TTEntityFiler {
  private static final ValueFactory valueFactory = new ValidatingValueFactory(SimpleValueFactory.getInstance());
  private final Map<String, String> prefixMap;
  private final Update deleteTriples;
  private final BaseDB conn;
  private final Graph graph;
  String blockers = "<http://snomed.info/sct#138875005>,<" + Namespace.IM + "Concept>";

  public TTEntityFilerRdf4j(BaseDB conn, Map<String, String> prefixMap, Graph graph) {
    this.conn = conn;
    this.prefixMap = prefixMap;
    this.graph = graph;
    String sparql = """
      DELETE {
        ?concept ?p1 ?o1.
        ?o1 ?p2 ?o2.
        ?o2 ?p3 ?o3.
        ?o3 ?p4 ?o4.
        ?o4 ?p5 ?o5.
      }
      where {
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
      """;
    deleteTriples = conn.prepareDeleteSparql(sparql);
  }

  public TTEntityFilerRdf4j(BaseDB conn, Graph graph) {
    this(conn, new HashMap<>(), graph);
  }

  @Override
  public void fileEntity(TTEntity entity) throws TTFilerException {

    if (entity.get(TTIriRef.iri(RDFS.LABEL)) != null
      && entity.get(TTIriRef.iri(IM.HAS_STATUS)) == null)
      entity.set(IM.HAS_STATUS.asIri(), IM.ACTIVE.asIri());
    if (entity.getCrud().equals(TTIriRef.iri(IM.UPDATE_PREDICATES))) {
      updatePredicates(entity);
    } else if (entity.getCrud().equals(TTIriRef.iri(IM.ADD_QUADS))) {
      addQuads(entity);
    } else if (entity.getCrud().equals(TTIriRef.iri(IM.REPLACE_ALL_PREDICATES))) {
      replaceAllPredicates(entity);
    } else if (entity.getCrud().equals(TTIriRef.iri(IM.DELETE_ALL))) {
      deleteTriples(entity);
    } else {
      if (entity.getPredicateMap().isEmpty()) return;
      throw new TTFilerException("Entity " + entity.getIri() + " has no crud assigned");
    }

  }

  @Override
  public void updateIsAs(TTEntity entity) {
    Set<String> isAs = new HashSet<>();
    if (entity.has(IM.IS_CHILD_OF.asIri()))
      entity.get(IM.IS_CHILD_OF.asIri()).stream().forEach(childOf -> isAs.add(childOf.asIriRef().getIri()));

    if (entity.has(RDFS.SUBCLASS_OF.asIri()))
      entity.get(RDFS.SUBCLASS_OF.asIri()).stream().forEach(childOf -> isAs.add(childOf.asIriRef().getIri()));

    deleteAscendantIsas(entity.getIri());
    if (!isAs.isEmpty()) saveAscendantIsas(entity.getIri(), isAs);
  }

  public void saveAscendantIsas(String entityIri, Set<String> isAs) {
    StringJoiner iriLine = new StringJoiner(" ");
    for (String stringIri : isAs) {
      iri(stringIri);
      iriLine.add("<" + stringIri + ">");
    }

    String sql = """
      PREFIX im: <http://endhealth.info/im#>
      INSERT {
        ?entity im:isA ?ancestor.
      }
      WHERE {
        VALUES ?ancestor { %s }
      }
      """.formatted(iriLine.toString());

    Update saveIsas = conn.prepareInsertSparql(sql, graph);
    saveIsas.setBinding("entity", iri(entityIri));
    saveIsas.execute();
  }

  public void deleteAscendantIsas(String entity) {
    log.info("Deleting ascendant isas");
    String deleteSql = """
      PREFIX im: <http://endhealth.info/im#>
      DELETE {
           ?entity im:isA ?anyAncestor.
       }
       WHERE {
           ?entity im:isA ?anyAncestor.
       }
      """;
    Update deleteIsas = conn.prepareDeleteSparql(deleteSql);
    deleteIsas.setBinding("entity", iri(entity));
    deleteIsas.execute();
  }

  @Override
  public void deleteIsAs(Set<String> entities) {
    log.info("Deleting descendant and ascendant isas");
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
      Update deleteIsas = conn.prepareDeleteSparql(deleteSql);
      deleteIsas.setBinding("isA", IM.IS_A.asDbIri());
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
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("entity", iri(entity));
      qry.setBinding("subClassOf", RDFS.SUBCLASS_OF.asDbIri());
      qry.setBinding("isA", IM.IS_A.asDbIri());
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
          descendant.addObject(RDFS.SUBCLASS_OF.asIri(), TTIriRef.iri(bs.getValue("superclass").stringValue()));
        }
      }
    }
    return descendants;

  }

  public Set<String> getIsAs(String superClass) {
    Set<String> isAs = new HashSet<>();
    StringJoiner getIsas = new StringJoiner("\n");
    getIsas.add("Select distinct ?ancestor").add("Where {").add("<" + superClass + "> <" + IM.IS_A + "> ?ancestor").add("filter (?ancestor not in (" + blockers + "))}");
    TupleQuery qry = conn.prepareTupleSparql(getIsas.toString());
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
      StringJoiner addSql = new StringJoiner("\n").add("INSERT DATA {");
      for (String ancestor : isAs.get(child.getKey())) {
        addSql.add("<" + child.getKey() + "> <" + IM.IS_A + "> <" + ancestor + ">.");
      }
      addSql.add("}");
      Update addIsAs = conn.prepareInsertSparql(addSql.toString(), graph);
      addIsAs.execute();
      if (count % 100 == 0) {
        log.info("isas added for {} entities", count);
      }
    }
  }


  private void replaceAllPredicates(TTEntity entity) throws TTFilerException {
    deleteTriples(entity);
    addQuads(entity);
  }

  private void addQuads(TTEntity entity) throws TTFilerException {
    try {
      ModelBuilder builder = new ModelBuilder();
      builder.namedGraph(graph.asDbIri());
      for (Map.Entry<TTIriRef, TTArray> entry : entity.getPredicateMap().entrySet()) {
        addTriple(builder, toIri(entity.getIri()), toIri(entry.getKey().getIri()), entry.getValue());
      }
      conn.add(builder.build());
    } catch (RepositoryException | TTFilerException e) {
      throw new TTFilerException("Failed to file entities " + e.getMessage());
    }

  }

  private void deleteTriples(TTEntity entity) throws TTFilerException {
    try {
      deleteTriples.setBinding("concept", valueFactory.createIRI(entity.getIri()));
      deleteTriples.execute();
    } catch (Exception e) {
      throw new TTFilerException("Failed to delete triples : " + e.getMessage());
    }

  }


  private void deletePredicates(TTEntity entity) throws TTFilerException {
    StringBuilder predList = new StringBuilder();
    int i = 0;
    Map<TTIriRef, TTArray> predicates = entity.getPredicateMap();
    for (Map.Entry<TTIriRef, TTArray> po : predicates.entrySet()) {
      String predicateIri = po.getKey().getIri();
      i++;
      if (i > 1) predList.append(", ");
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
          {
            ?concept ?p1 ?o1.
            filter(?p1 in(%s))
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
      """.formatted(predList);
    Update deletePredicates = conn.prepareDeleteSparql(spq);
    deletePredicates.setBinding("concept", iri(entity.getIri()));
    try {
      deletePredicates.execute();
    } catch (RepositoryException e) {
      throw new TTFilerException("Failed to delete triples");
    }

  }

  private void updatePredicates(TTEntity entity) throws TTFilerException {

    //Deletes the previous predicate values and adds in the new ones
    deletePredicates(entity);
    addQuads(entity);
  }

  private void addTriple(ModelBuilder builder, Resource subject, IRI predicate, TTArray array) throws TTFilerException {
    for (TTValue value : array.iterator()) {
      addTriple(builder, subject, predicate, value);
    }
  }

  private void addTriple(ModelBuilder builder, Resource subject, IRI predicate, TTValue value) throws TTFilerException {
    if (value.isLiteral()) {
      if (null != value.asLiteral().getValue())
        builder.add(subject, predicate, value.asLiteral().getType() == null ? literal(value.asLiteral().getValue()) : literal(value.asLiteral().getValue(), toIri(value.asLiteral().getType().getIri())));
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
      URL url = new URI(decodedURL).toURL();
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
      String result = uri.toASCIIString();


      if (!iri.equals(result)) log.trace("Encoded iri [{}] => [{}]", iri, result);

      return iri(result);
    } catch (MalformedURLException | URISyntaxException e) {
      throw new TTFilerException("Unable to encode iri: " + iri, e);
    }
  }

  public String expand(String iri) throws TTFilerException {
    if (prefixMap == null) return iri;
    try {
      int colonPos = iri.indexOf(":");
      String prefix = iri.substring(0, colonPos);
      String path = prefixMap.get(prefix);
      if (path == null) return iri;
      else return path + iri.substring(colonPos + 1);
    } catch (StringIndexOutOfBoundsException e) {
      log.debug("invalid iri [{}]", iri);
      throw new TTFilerException("Invalid iri format (" + iri + ")");
    }
  }

}
