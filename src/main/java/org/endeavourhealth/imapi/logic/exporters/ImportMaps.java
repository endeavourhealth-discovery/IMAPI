package org.endeavourhealth.imapi.logic.exporters;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.ValidatingValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.FileRepository;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.filer.rdf4j.TTBulkFiler;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.*;

import java.io.IOException;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class ImportMaps implements AutoCloseable {
  private final FileRepository fileRepo = new FileRepository(TTBulkFiler.getDataPath());
  private final ValueFactory valueFactory = new ValidatingValueFactory(SimpleValueFactory.getInstance());
  private final Map<String, String> cachedNames = new HashMap<>();


  /**
   * Retrieves EMIS to Snomed code maps
   *
   * @throws TTFilerException when code maps are missing
   */
  public Map<String, Set<String>> importEmisToSnomed(Graph graph) throws TTFilerException, IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getCodeCoreMap(SCHEME.EMIS);
    return importEmisToSnomedRdf4j(graph);
  }

  public String getCoreName(String iri, Graph graph) throws IOException {
    if (cachedNames.get(iri) != null)
      return cachedNames.get(iri);
    String name;
    if (TTFilerFactory.isBulk()) {
      name = fileRepo.getCoreName(iri);
      cachedNames.put(iri, name);
      return name;
    } else {
      name = new EntityRepository().getEntityReferenceByIri(iri, graph).getName();
      cachedNames.put(iri, name);
      return name;
    }

  }


  /**
   * Returns A core entity iri and name from a core term
   *
   * @param term the code or description id or term code
   * @return iri and name of entity
   */
  public TTIriRef getReferenceFromCoreTerm(String term, Graph graph) throws IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getReferenceFromCoreTerm(term);
    else
      return new EntityRepository().getReferenceFromCoreTerm(term, graph);
  }

  public Map<String, String> getCodeToIri(Graph graph) throws IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getCodeToIri();
    else
      return new EntityRepository().getCodeToIri(graph);
  }


  public Map<String, String> getCodesToIri(String scheme, Graph graph) throws IOException {
    Map<String, String> codeToIri;
    if (TTFilerFactory.isBulk())
      codeToIri = fileRepo.getCodeToIri();
    else
      codeToIri = new EntityRepository().getCodesToIri(scheme, graph);
    Map<String, String> map = new HashMap<>();
    codeToIri.entrySet().stream().forEach(item -> {
      String entry = item.getKey();
      String value = item.getValue();
      if (entry.startsWith(scheme)) {
        String code = entry.split(scheme)[1];
        map.put(code, value);
      }
    });
    return map;
  }

  public Set<String> getCodes(String scheme, Graph graph) throws IOException {
    Map<String, String> codeToIri = getCodeToIri(graph);
    Set<String> codes = new HashSet<>();
    codeToIri.forEach((entry, value) -> {
      if (entry.startsWith(scheme)) {
        String code = entry.split(scheme)[1];
        codes.add(code);
      }
    });
    return codes;
  }

  public Set<Entity> getCoreFromCode(String code, List<SCHEME> schemes, Graph graph) {
    return new EntityRepository().getCoreFromCode(code, schemes, graph);
  }

  public Map<String, Set<String>> getAllMatchedLegacy(Graph graph) throws IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getAllMatchedLegacy();
    else
      return new EntityRepository().getAllMatchedLegacy(graph);
  }

  public Set<Entity> getCoreFromLegacyTerm(String term, SCHEME scheme, Graph graph) throws IOException {
    return new EntityRepository().getCoreFromLegacyTerm(term, scheme, graph);

  }


  /**
   * Retrieves entities from IM
   *
   * @return a set of snomed codes
   * @throws TTFilerException if using rdf4j
   */
  public Set<String> importEntities(Graph graph) throws TTFilerException, IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getAllEntities();
    else {
      Set<String> entities = new HashSet<>();
      return importAllRDF4J(entities, graph);
    }
  }


  /**
   * Gets all entities and includes their legacy map if they have one
   *
   * @return A TransformMap of all entites and the set of iris they match to
   * @throws IOException      if using the file repository
   * @throws TTFilerException if using the graph repository
   */
  public Map<String, Set<String>> getAllPlusMatches(Graph graph) throws IOException, TTFilerException {
    Set<String> all = importEntities(graph);
    Map<String, Set<String>> legacyCore = getAllMatchedLegacy(graph);
    Map<String, Set<String>> allAndMatched = new HashMap<>();
    for (String iri : all) {
      allAndMatched.put(iri, legacyCore.get(iri));
    }
    return allAndMatched;
  }

  /**
   * Retieves read to Snomed maps, using the Vision code scheme as a proxy for read
   *
   * @return the code to Snomed code one to many map
   * @throws TTFilerException when code maps are missing
   */
  public Map<String, Set<String>> importReadToSnomed(Graph graph) throws TTFilerException, IOException {
    Map<String, Set<String>> readToSnomed = new HashMap<>();
    if (TTFilerFactory.isBulk()) {
      return fileRepo.getCodeCoreMap(SCHEME.EMIS);
    }
    return importReadToSnomedRdf4j(readToSnomed, graph);
  }

  /**
   * Gets descendant codes for an iri and its terms;
   *
   * @param concept the iri for the parent concept
   * @return A map from code to many terms;
   * @throws TTFilerException when descendants of concept are missing. Set as specialConcept in TTBulkFiler
   */
  public Map<String, Set<String>> getDescendants(String concept, Graph graph) throws TTFilerException, IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getDescendants(concept);
    return getDescendantsRDF(concept, graph);
  }

  public Map<String, Set<String>> getDescendantsRDF(String concept, Graph graph) throws TTFilerException {
    Map<String, Set<String>> codeToTerm = new HashMap<>();
    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?child ?name
        WHERE {
          ?child ?scheme ?snomedNamespace
          ?child ?subClassOf ?concept.
          ?child ?label ?name.
        }
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
      qry.setBinding("scheme", IM.HAS_SCHEME.asDbIri());
      qry.setBinding("concept", valueFactory.createIRI(concept));
      qry.setBinding("snomedNamespace", SNOMED.NAMESPACE.asDbIri());
      qry.setBinding("subClassOf", RDFS.SUBCLASS_OF.asDbIri());
      qry.setBinding("label", RDFS.LABEL.asDbIri());
      try (TupleQueryResult rs = qry.evaluate();) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String child = bs.getValue("child").stringValue();
          String term = bs.getValue("name").stringValue();
          Set<String> maps = codeToTerm.computeIfAbsent(child, k -> new HashSet<>());
          maps.add(term);
        }

      } catch (RepositoryException e) {
        throw new TTFilerException("Unable to retrieve snomed codes");
      }
      return codeToTerm;
    }
  }


  private Set<String> importAllRDF4J(Set<String> entities, Graph graph) throws TTFilerException {

    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT distinct ?entity
        WHERE {
          ?entity ?rdfLabel ?label.
          filter (isIri(?entity))
        }
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
      qry.setBinding("rdfLabel", RDFS.LABEL.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          entities.add(bs.getValue("entity").stringValue());
        }
      }
    } catch (RepositoryException e) {
      throw new TTFilerException("Unable to retrieve entities");
    }
    return entities;
  }


  private Set<String> importSnomedRDF4J(Set<String> snomedCodes, Graph graph) throws TTFilerException {

    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?snomed
        WHERE {
          ?concept ?scheme ?snomedNamespace.
          ?concept ?code ?snomed}
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
      qry.setBinding("scheme", IM.HAS_SCHEME.asDbIri());
      qry.setBinding("code", IM.CODE.asDbIri());
      qry.setBinding("snomedNamespace", SNOMED.NAMESPACE.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          snomedCodes.add(bs.getValue("snomed").stringValue());
        }
      }
    } catch (RepositoryException e) {
      throw new TTFilerException("Unable to retrieve snomed codes");
    }
    return snomedCodes;
  }


  private Map<String, Set<String>> importReadToSnomedRdf4j(Map<String, Set<String>> readToSnomed, Graph graph) throws TTFilerException {

    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?code ?snomed
        WHERE {
          ?concept ?scheme ?vision .
          ?concept ?imCode ?code .
          ?concept ?matchedTo ?snomedIri .
          ?snomedIri ?scheme ?snomedNamedspace .
          ?snomedIri ?imCode ?snomed .
        }
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
      qry.setBinding("scheme", IM.HAS_SCHEME.asDbIri());
      qry.setBinding("snomedNamespace", SNOMED.NAMESPACE.asDbIri());
      qry.setBinding("vision", SCHEME.VISION.asDbIri());
      qry.setBinding("imCode", IM.CODE.asDbIri());
      qry.setBinding("matchedTo", IM.MATCHED_TO.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String read = bs.getValue("code").stringValue();
          String snomed = bs.getValue("snomed").stringValue();
          Set<String> maps = readToSnomed.computeIfAbsent(read, k -> new HashSet<>());
          maps.add(snomed);
        }
      }
    } catch (RepositoryException e) {
      throw new TTFilerException("unable to retrieve vision/read " + e);
    }
    return readToSnomed;
  }

  public Map<String, TTEntity> getEMISReadAsVision(Graph graph) throws IOException {
    if (TTFilerFactory.isBulk()) {
      Map<String, Set<String>> emisToCore = fileRepo.getCodeCoreMap(SCHEME.EMIS);
      Map<String, TTEntity> emisRead2 = new HashMap<>();
      for (Map.Entry<String, Set<String>> entry : emisToCore.entrySet()) {
        String code = entry.getKey();
        if (isRead(code)) {
          code = (code + ".....").substring(0, 5);
          TTEntity entity = emisRead2.computeIfAbsent(code, k -> new TTEntity());
          entity.setCode(code);
          entity.setScheme(TTIriRef.iri(SCHEME.VISION));
          entity.setIri(SCHEME.VISION + code.replace(".", ""));
          for (String snomed : entry.getValue()) {
            entity.addObject(TTIriRef.iri(IM.MATCHED_TO), TTIriRef.iri(snomed));
          }
        }
      }
      return emisRead2;

    } else
      return getEMISReadAsVisionRdf4j(graph);
  }

  private Map<String, TTEntity> getEMISReadAsVisionRdf4j(Graph graph) {
    Map<String, TTEntity> emisRead2 = new HashMap<>();
    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sql = """
        SELECT ?oldCode ?name ?snomedIri
        WHERE {
          ?concept ?scheme ?emis .
          ?concept ?label ?name.
          ?concept ?matchedTo ?snomedIri .
          OPTIONAL {
            ?concept ?hasTermCode ?tc.
            ?tc ?imCode ?oldCode)
          }
        }
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sql, graph);
      qry.setBinding("scheme", IM.HAS_SCHEME.asDbIri());
      qry.setBinding("emis", SCHEME.EMIS.asDbIri());
      qry.setBinding("label", RDFS.LABEL.asDbIri());
      qry.setBinding("matchedTo", IM.MATCHED_TO.asDbIri());
      qry.setBinding("hasTermCode", IM.HAS_TERM_CODE.asDbIri());
      qry.setBinding("imCode", IM.CODE.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String code = bs.getValue("oldCode").stringValue();
          String name = bs.getValue("name").stringValue();
          String snomedIri = bs.getValue("snomedIri").stringValue();
          if (isRead(code)) {
            code = (code + ".....").substring(0, 5);
            TTEntity entity = emisRead2.computeIfAbsent(code, k -> new TTEntity());
            entity.setName(name);
            entity.setCode(code);
            entity.setIri(SCHEME.VISION + code.replace(".", ""));
            entity.addObject(TTIriRef.iri(IM.MATCHED_TO), TTIriRef.iri(snomedIri));
          }
        }
      }
    }
    return emisRead2;
  }

  public boolean isRead(String s) {
    if (s.length() < 6)
      return !s.contains("DRG") && !s.contains("SHAPT") && !s.contains("EMIS") && !s.contains("-");
    else
      return false;
  }


  private Map<String, Set<String>> importEmisToSnomedRdf4j(Graph graph) throws TTFilerException {
    Map<String, Set<String>> emisToSnomed = new HashMap<>();
    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?code ?snomedIri  ?name
        WHERE {
          ?concept ?scheme ?emis .
          ?concept ?imCode ?code.
          ?concept ?label ?name.
          ?concept ?matchedTo ?snomedIri.
          ?snomedIri ?scheme ?snomedNamespace .
          ?snomedIri ?imCode ?snomed.
        }
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
      qry.setBinding("scheme", IM.HAS_SCHEME.asDbIri());
      qry.setBinding("snomedNamespace", SNOMED.NAMESPACE.asDbIri());
      qry.setBinding("emis", SCHEME.EMIS.asDbIri());
      qry.setBinding("imCode", IM.CODE.asDbIri());
      qry.setBinding("matchedTo", IM.MATCHED_TO.asDbIri());
      qry.setBinding("label", RDFS.LABEL.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String read = bs.getValue("code").stringValue();
          String snomed = bs.getValue("snomedIri").stringValue();
          Set<String> maps = emisToSnomed.computeIfAbsent(read, k -> new HashSet<>());
          maps.add(snomed);
        }
        return emisToSnomed;

      } catch (RepositoryException e) {
        throw new TTFilerException("unable to retrieve vision/read " + e);
      }
    }
  }


  /**
   * Extracts term codes from Snomed entities
   *
   * @return TransformMap of description code to entity
   */
  public Map<String, String> getDescriptionIds(Graph graph) throws TTFilerException {
    Map<String, String> termMap = new HashMap<>();
    try (RepositoryConnection conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?snomed ?descid
        WHERE {
          ?snomed ?scheme ?snomedNamespace .
          ?snomed im:hasTermCode ?node.
          ?node im:code ?descid.
        }
        """;
      TupleQuery qry = IMDB.prepareTupleSparql(conn, sparql, graph);
      qry.setBinding("scheme", IM.HAS_SCHEME.asDbIri());
      qry.setBinding("snomedNamespace", SNOMED.NAMESPACE.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          termMap.put(bs.getValue("descid").stringValue(), bs.getValue("snomed").stringValue());
        }
      }
    } catch (RepositoryException e) {
      throw new TTFilerException("Unable to retrieve snomed term codes");
    }
    return termMap;
  }


  public Set<Entity> getLegacyFromTermCode(String originalCode, SCHEME scheme, Graph graph) throws IOException {
    return new EntityRepository().getReferenceFromTermCode(originalCode, scheme, graph);
  }

  @Override
  public void close() throws Exception {
    cachedNames.clear();
  }
}
