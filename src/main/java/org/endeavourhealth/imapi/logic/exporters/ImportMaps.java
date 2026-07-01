package org.endeavourhealth.imapi.logic.exporters;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.ValidatingValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.FileRepository;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.filer.rdf4j.TTBulkFiler;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.IOException;
import java.util.*;

public class ImportMaps implements AutoCloseable {
  private final FileRepository fileRepo = new FileRepository(TTBulkFiler.getDataPath());
  private final ValueFactory valueFactory = new ValidatingValueFactory(SimpleValueFactory.getInstance());
  private final Map<String, String> cachedNames = new HashMap<>();


  /**
   * Retrieves EMIS to Snomed code maps
   *
   * @throws TTFilerException when code maps are missing
   */
  public Map<String, Set<String>> importEmisToSnomed() throws TTFilerException, IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getCodeCoreMap(NamespaceVocab.EMIS);
    return importEmisToSnomedRdf4j();
  }

  public String getCoreName(String iri) throws IOException {
    if (cachedNames.get(iri) != null)
      return cachedNames.get(iri);
    String name;
    if (TTFilerFactory.isBulk()) {
      name = fileRepo.getCoreName(iri);
      cachedNames.put(iri, name);
      return name;
    } else {
      name = new EntityRepository().getEntityReferenceByIri(iri).getName();
      cachedNames.put(iri, name);
      return name;
    }

  }

  public String getIriFromLegacyCode(String scheme, String legacyCode) {
    if (TTFilerFactory.isBulk())
      throw new UnsupportedOperationException("Cannot use bulk file repository for this method");
    else return new EntityRepository().getIriFromLegacy(scheme, legacyCode);
  }


  /**
   * Returns A core entity iri and name from a core term
   *
   * @param term the code or description id or term code
   * @return iri and name of entity
   */
  public TTIriRef getReferenceFromCoreTerm(String term) throws IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getReferenceFromCoreTerm(term);
    else
      return new EntityRepository().getReferenceFromCoreTerm(term);
  }

  public Map<String, String> getCodeToIri() throws IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getCodeToIri();
    else
      return new EntityRepository().getCodeToIri();
  }


  public Map<String, String> getCodesToIri(NAMESPACE scheme) throws IOException {
    Map<String, String> codeToIri;
    if (TTFilerFactory.isBulk())
      codeToIri = fileRepo.getCodeToIri();
    else
      codeToIri = new EntityRepository().getCodesToIri(scheme);
    Map<String, String> map = new HashMap<>();
    codeToIri.forEach((entry, value) -> {
      if (entry.startsWith(scheme.toString())) {
        String code = entry.split(scheme.toString())[1];
        map.put(code, value);
      }
    });
    return map;
  }

  public Set<String> getCodes(NAMESPACE scheme) throws IOException {
    Map<String, String> codeToIri = getCodeToIri();
    Set<String> codes = new HashSet<>();
    codeToIri.forEach((entry, value) -> {
      if (entry.startsWith(scheme.toString())) {
        String code = entry.split(scheme.toString())[1];
        codes.add(code);
      }
    });
    return codes;
  }

  public Set<Entity> getCoreFromCode(String code, List<NAMESPACE> schemes) {
    return new EntityRepository().getCoreFromCode(code, schemes);
  }

  public Map<String, Set<String>> getAllMatchedLegacy() throws IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getAllMatchedLegacy();
    else
      return new EntityRepository().getAllMatchedLegacy();
  }

  public Set<Entity> getCoreFromLegacyTerm(String term, NAMESPACE scheme) {
    return new EntityRepository().getCoreFromLegacyTerm(term, scheme);

  }


  /**
   * Retrieves entities from IM
   *
   * @return a set of snomed codes
   * @throws TTFilerException if using rdf4j
   */
  public Set<String> importEntities() throws TTFilerException, IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getAllEntities();
    else {
      Set<String> entities = new HashSet<>();
      return importAllRDF4J(entities);
    }
  }


  /**
   * Gets all entities and includes their legacy map if they have one
   *
   * @return A TransformMap of all entites and the set of iris they match to
   * @throws IOException      if using the file repository
   * @throws TTFilerException if using the graph repository
   */
  public Map<String, Set<String>> getAllPlusMatches() throws IOException, TTFilerException {
    Set<String> all = importEntities();
    Map<String, Set<String>> legacyCore = getAllMatchedLegacy();
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
  public Map<String, Set<String>> importReadToSnomed() throws TTFilerException, IOException {
    Map<String, Set<String>> readToSnomed = new HashMap<>();
    if (TTFilerFactory.isBulk()) {
      return fileRepo.getCodeCoreMap(NamespaceVocab.EMIS);
    }
    return importReadToSnomedRdf4j(readToSnomed);
  }

  /**
   * Gets descendant codes for an iri and its terms;
   *
   * @param concept the iri for the parent concept
   * @return A map from code to many terms;
   * @throws TTFilerException when descendants of concept are missing. Set as specialConcept in TTBulkFiler
   */
  public Map<String, Set<String>> getDescendants(String concept) throws TTFilerException, IOException {
    if (TTFilerFactory.isBulk())
      return fileRepo.getDescendants(concept);
    return getDescendantsRDF(concept);
  }

  public Map<String, Set<String>> getDescendantsRDF(String concept) throws TTFilerException {
    Map<String, Set<String>> codeToTerm = new HashMap<>();
    try (IMDB conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?child ?name
        WHERE {
          ?child ?scheme ?snomedNamespace.
          ?child ?subClassOf ?concept.
          ?child ?label ?name.
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("scheme", EnumUtils.asDbIri(ImVocab.HAS_SCHEME));
      qry.setBinding("concept", valueFactory.createIRI(concept));
      qry.setBinding("snomedNamespace", EnumUtils.asDbIri(NamespaceVocab.SNOMED));
      qry.setBinding("subClassOf", EnumUtils.asDbIri(RdfsVocab.SUBCLASS_OF));
      qry.setBinding("label", EnumUtils.asDbIri(RdfsVocab.LABEL));
      try (TupleQueryResult rs = qry.evaluate()) {
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


  private Set<String> importAllRDF4J(Set<String> entities) throws TTFilerException {

    try (IMDB conn = IMDB.getConnection()) {
      String sparql = """
        SELECT distinct ?entity
        WHERE {
          ?entity ?rdfLabel ?label.
          filter (isIri(?entity))
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("rdfLabel", EnumUtils.asDbIri(RdfsVocab.LABEL));
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


  private Set<String> importSnomedRDF4J(Set<String> snomedCodes) throws TTFilerException {

    try (IMDB conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?snomed
        WHERE {
          ?concept ?scheme ?snomedNamespace.
          ?concept ?code ?snomed}
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("scheme", EnumUtils.asDbIri(ImVocab.HAS_SCHEME));
      qry.setBinding("code", EnumUtils.asDbIri(ImVocab.CODE));
      qry.setBinding("snomedNamespace", EnumUtils.asDbIri(NamespaceVocab.SNOMED));
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


  private Map<String, Set<String>> importReadToSnomedRdf4j(Map<String, Set<String>> readToSnomed) throws TTFilerException {

    try (IMDB conn = IMDB.getConnection()) {
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
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("scheme", EnumUtils.asDbIri(ImVocab.HAS_SCHEME));
      qry.setBinding("snomedNamespace", EnumUtils.asDbIri(NamespaceVocab.SNOMED));
      qry.setBinding("vision", EnumUtils.asDbIri(NamespaceVocab.VISION));
      qry.setBinding("imCode", EnumUtils.asDbIri(ImVocab.CODE));
      qry.setBinding("matchedTo", EnumUtils.asDbIri(ImVocab.MATCHED_TO));
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

  public Map<String, TTEntityJava> getEMISReadAsVision() throws IOException {
    if (TTFilerFactory.isBulk()) {
      Map<String, Set<String>> emisToCore = fileRepo.getCodeCoreMap(NamespaceVocab.EMIS);
      Map<String, TTEntityJava> emisRead2 = new HashMap<>();
      for (Map.Entry<String, Set<String>> entry : emisToCore.entrySet()) {
        String code = entry.getKey();
        if (isRead(code)) {
          code = (code + ".....").substring(0, 5);
          TTEntityJava entity = emisRead2.computeIfAbsent(code, k -> new TTEntityJava());
          entity.setCode(code);
          entity.setScheme(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.VISION));
          entity.setIri(NamespaceVocab.VISION + code.replace(".", ""));
          for (String snomed : entry.getValue()) {
            entity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.MATCHED_TO), TTIriRefExtensionsKt.iri(new TTIriRef(), snomed));
          }
        }
      }
      return emisRead2;

    } else
      return getEMISReadAsVisionRdf4j();
  }

  private Map<String, TTEntityJava> getEMISReadAsVisionRdf4j() {
    Map<String, TTEntityJava> emisRead2 = new HashMap<>();
    try (IMDB conn = IMDB.getConnection()) {
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
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("scheme", EnumUtils.asDbIri(ImVocab.HAS_SCHEME));
      qry.setBinding("emis", EnumUtils.asDbIri(NamespaceVocab.EMIS));
      qry.setBinding("label", EnumUtils.asDbIri(RdfsVocab.LABEL));
      qry.setBinding("matchedTo", EnumUtils.asDbIri(ImVocab.MATCHED_TO));
      qry.setBinding("hasTermCode", EnumUtils.asDbIri(ImVocab.HAS_TERM_CODE));
      qry.setBinding("imCode", EnumUtils.asDbIri(ImVocab.CODE));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String code = bs.getValue("oldCode").stringValue();
          String name = bs.getValue("name").stringValue();
          String snomedIri = bs.getValue("snomedIri").stringValue();
          if (isRead(code)) {
            code = (code + ".....").substring(0, 5);
            TTEntityJava entity = emisRead2.computeIfAbsent(code, k -> new TTEntityJava());
            entity.setName(name);
            entity.setCode(code);
            entity.setIri(NamespaceVocab.VISION + code.replace(".", ""));
            entity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.MATCHED_TO), TTIriRefExtensionsKt.iri(new TTIriRef(), snomedIri));
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


  private Map<String, Set<String>> importEmisToSnomedRdf4j() throws TTFilerException {
    Map<String, Set<String>> emisToSnomed = new HashMap<>();
    try (IMDB conn = IMDB.getConnection()) {
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
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("scheme", EnumUtils.asDbIri(ImVocab.HAS_SCHEME));
      qry.setBinding("snomedNamespace", EnumUtils.asDbIri(NamespaceVocab.SNOMED));
      qry.setBinding("emis", EnumUtils.asDbIri(NamespaceVocab.EMIS));
      qry.setBinding("imCode", EnumUtils.asDbIri(ImVocab.CODE));
      qry.setBinding("matchedTo", EnumUtils.asDbIri(ImVocab.MATCHED_TO));
      qry.setBinding("label", EnumUtils.asDbIri(RdfsVocab.LABEL));
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
  public Map<String, String> getDescriptionIds() throws TTFilerException {
    Map<String, String> termMap = new HashMap<>();
    try (IMDB conn = IMDB.getConnection()) {
      String sparql = """
        SELECT ?snomed ?descid
        WHERE {
          ?snomed ?scheme ?snomedNamespace .
          ?snomed im:hasTermCode ?node.
          ?node im:code ?descid.
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("scheme", EnumUtils.asDbIri(ImVocab.HAS_SCHEME));
      qry.setBinding("snomedNamespace", EnumUtils.asDbIri(NamespaceVocab.SNOMED));
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


  public Set<Entity> getLegacyFromTermCode(String originalCode, NAMESPACE scheme) {
    return new EntityRepository().getReferenceFromTermCode(originalCode, scheme);
  }

  @Override
  public void close() {
    cachedNames.clear();
  }
}
