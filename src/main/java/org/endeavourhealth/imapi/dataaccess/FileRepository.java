package org.endeavourhealth.imapi.dataaccess;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SCHEME;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileRepository {


  private final Map<SCHEME, Map<String, Set<String>>> codeCoreMap = new EnumMap<>(SCHEME.class);
  private final Map<SCHEME, Map<String, Set<String>>> termCoreMap = new EnumMap<>(SCHEME.class);
  private final Map<SCHEME, Map<String, Set<String>>> codes = new EnumMap<>(SCHEME.class);
  private final Map<String, String> coreTerms = new HashMap<>();
  private final Map<SCHEME, Map<String, String>> termCodes = new EnumMap<>(SCHEME.class);
  private final Map<SCHEME, Map<String, Set<String>>> codeIds = new EnumMap<>(SCHEME.class);
  private final Map<String, String> coreIris = new HashMap<>();
  @Setter
  @Getter
  private String dataPath;

  public FileRepository(String dataPath) {
    this.dataPath = dataPath;
  }

  public String getCoreName(String iri) throws IOException {
    if (coreIris.isEmpty()) {
      fetchCoreIris();
    }
    return coreIris.get(iri);
  }

  private void fetchCoreIris() throws IOException {
    String fileName = getFile("CoreIris");
    readFileToStringMap(fileName, coreIris);
  }

  public Map<String, String> getCodeToIri() throws IOException {
    Map<String, String> codeToIri = new HashMap<>();
    String fileName = getFile("CodeMap");
    readFileToStringMap(fileName, codeToIri);
    return codeToIri;
  }

  public void fetchRelationships(Map<String, Map<String, Set<String>>> relationshipMap,
                                 Set<String> blockingIris) throws IOException {
    String fileName = getFile("SubTypes");
    AtomicInteger count = new AtomicInteger();
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
      lines.forEach(line -> {
        count.getAndIncrement();
        String[] fields = line.split("\t");
        String child = fields[0];
        String relationship = fields[1];
        String parent = fields[2];
        if (relationship.equals(RDFS.SUB_PROPERTY_OF.toString()))
          relationship = RDFS.SUBCLASS_OF.toString();
        if (relationship.equals(IM.LOCAL_SUBCLASS_OF.toString()))
          relationship = RDFS.SUBCLASS_OF.toString();
        if (!blockingIris.contains(parent)) {
          relationshipMap.computeIfAbsent(relationship, r -> new HashMap<>());
          Map<String, Set<String>> parentMap = relationshipMap.get(relationship);
          Set<String> parents = parentMap.computeIfAbsent(child, k -> new HashSet<>());
          parents.add(parent);
        }
        if (count.get() % 1_000_000 == 0)
          log.info("{} relationships collected", count);
      });
    }
  }

  public Set<TTIriRef> getCoreFromCodeId(String codeId, List<SCHEME> schemes) throws IOException {
    for (SCHEME scheme : schemes) {
      if (codeIds.get(scheme) == null) {
        fetchCodeIds(scheme);
      }
      if (codeIds.get(scheme).get(codeId) != null) {
        Set<TTIriRef> result = new HashSet<>();
        codeIds.get(scheme).get(codeId).forEach(c -> result.add(TTIriRef.iri(c)));
        return result;
      }
    }
    return Collections.emptySet();
  }


  /**
   * Returns A core entity iri and name from a core term
   *
   * @param term the code or description id or term code
   * @return iri and name of entity
   */
  public TTIriRef getReferenceFromCoreTerm(String term) throws IOException {
    if (coreTerms.isEmpty()) {
      fetchCoreTerms();
    }
    if (coreTerms.get(term) != null)
      return TTIriRef.iri(coreTerms.get(term));
    else
      return null;
  }


  public Map<String, Set<String>> getAllMatchedLegacy() throws IOException {
    Map<String, Set<String>> legacyMap = new HashMap<>();
    String fileName = getFile("LegacyCore");
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = reader.readLine();
      while (line != null && !line.isEmpty()) {
        String[] fields = line.split("\t");
        String legacy = fields[0];
        if (fields.length < 2) {
          log.info("invalid line {}", line);
          String x = reader.readLine();
          log.info(x);
          line = String.join("", line, x);
          fields = line.split("\t");
        }
        String core = fields[1];
        Set<String> coreSet = legacyMap.computeIfAbsent(legacy, l -> new HashSet<>());
        coreSet.add(core);
        line = reader.readLine();
      }
    }
    return legacyMap;
  }

  private void fetchCoreTerms() throws IOException {
    String fileName = getFile("CoreTerms");
    readFileToStringMap(fileName, coreTerms);
  }

  public Set<TTIriRef> getCoreFromCode(String originalCode, List<SCHEME> schemes) {
    try {
      for (SCHEME scheme : schemes) {
        if (codeCoreMap.get(scheme) == null)
          fetchCodeCoreMap(scheme);
        if (codeCoreMap.get(scheme).get(originalCode) != null) {
          return codeCoreMap.get(scheme).get(originalCode).stream().map(TTIriRef::iri).collect(Collectors.toSet());
        }
      }
      return Collections.emptySet();
    } catch (Exception e) {
      log.error("unable to retrieve core from code : {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  public Set<TTIriRef> getCoreFromLegacyTerm(String originalTerm, SCHEME scheme) throws IOException {
    if (termCoreMap.get(scheme) == null)
      fetchTermCoreMap(scheme);
    if (termCoreMap.get(scheme).get(originalTerm) != null)
      return termCoreMap.get(scheme).get(originalTerm).stream().map(TTIriRef::iri).collect(Collectors.toSet());
    else
      return Collections.emptySet();
  }

  public Set<String> getAllEntities() throws IOException {
    Set<String> entities = new HashSet<>();
    String fileName = getFile("Entities");
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
      lines.forEach(entities::add);
    }
    return entities;
  }


  public void fetchCodeMap(SCHEME scheme) throws IOException {
    Map<String, Set<String>> codeSet = codes.computeIfAbsent(scheme, s -> new HashMap<>());
    String fileName = getSchemeFile("CodeMap", scheme);
    readFileToSetMap(fileName, codeSet);
  }

  public void fetchCodeIds(SCHEME scheme) throws IOException {
    Map<String, Set<String>> codeSet = codeIds.computeIfAbsent(scheme, s -> new HashMap<>());
    String fileName = getSchemeFile("CodeIds", scheme);
    readFileToSetMap(fileName, codeSet);
  }


  public void fetchTermCodes(SCHEME scheme) throws IOException {
    Map<String, String> iris = termCodes.computeIfAbsent(scheme, s -> new HashMap<>());
    String fileName = getSchemeFile("TermCodes", scheme);
    readFileToStringMap(fileName, iris);
  }

  public Map<String, Set<String>> getDescendants(String concept) throws IOException {
    return fetchDescendants(concept);
  }

  private Map<String, Set<String>> fetchDescendants(String concept) throws IOException {
    String fileName = getFile("Descendants");
    Map<String, Set<String>> children = new HashMap<>();
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
      lines.forEach(line -> {
        String[] fields = line.split("\t");
        String parent = fields[0];
        String child = fields[1];
        String childName = fields[2];
        if (parent.equals(concept)) {
          Set<String> childTerms = children.computeIfAbsent(child, c -> new HashSet<>());
          childTerms.add(childName);
        }
      });
    }
    return children;

  }

  public Map<String, Set<String>> getCodeCoreMap(SCHEME scheme) throws IOException {
    if (codeCoreMap.get(scheme) != null)
      return codeCoreMap.get(scheme);
    else
      return fetchCodeCoreMap(scheme);
  }

  public Map<String, Set<String>> fetchCodeCoreMap(SCHEME scheme) throws IOException {
    Map<String, Set<String>> coreMap = codeCoreMap.computeIfAbsent(scheme, s -> new HashMap<>());
    String fileName = getSchemeFile("CodeCoreMap", scheme);
    readFileToSetMap(fileName, coreMap);
    return codeCoreMap.get(scheme);
  }

  public void fetchTermCoreMap(SCHEME scheme) throws IOException {
    Map<String, Set<String>> coreMap = termCoreMap.computeIfAbsent(scheme, s -> new HashMap<>());
    String fileName = getSchemeFile("TermCoreMap", scheme);
    readFileToSetMap(fileName, coreMap);
  }

  private void readFileToSetMap(String fileName, Map<String, Set<String>> coreMap) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
      lines.forEach(line -> {
        String[] fields = line.split("\t");
        String term = fields[0];
        String core = fields[1];
        Set<String> coreSet = coreMap.computeIfAbsent(term, c -> new HashSet<>());
        coreSet.add(core);
      });
    }
  }

  private void readFileToStringMap(String fileName, Map<String, String> stringMap) throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1)) {
      lines.forEach(line -> {
        String[] fields = line.split("\t");
        String code = fields[0];
        String iri = fields[1];
        stringMap.put(code, iri);
      });
    }
  }


  private String getSchemeFile(String fileType, SCHEME scheme) {
    return dataPath + "/" + fileType + "-" + scheme + ".txt";
  }

  private String getFile(String fileType) {
    return dataPath + "/" + fileType + ".txt";
  }

}
