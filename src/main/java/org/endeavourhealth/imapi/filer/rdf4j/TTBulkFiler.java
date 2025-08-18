package org.endeavourhealth.imapi.filer.rdf4j;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTToNQuad;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Slf4j
public class TTBulkFiler implements TTDocumentFiler {
  private static final Set<String> specialChildren = new HashSet<>(List.of(Namespace.SNOMED + "92381000000106"));
  private static String dataPath;
  private static String configTTl;
  private static String preload;
  private static int privacyLevel = 0;
  private static int statementCount;
  private FileWriter codeMap;
  private FileWriter coreTerms;
  private FileWriter quads;
  private FileWriter subtypes;
  private FileWriter descendants;
  private FileWriter legacyCore;
  private FileWriter allEntities;
  private FileWriter coreIris;
  private Map<Namespace, FileWriter> termCoreMap;
  private Map<Namespace, FileWriter> codeCoreMap;
  private Map<Namespace, FileWriter> codeIds;

  private Graph insertGraph;

  public TTBulkFiler(Graph insertGraph) {
    this.insertGraph = insertGraph;
  }

  private static void setStatus(TTEntity entity) {
    if (entity.get(iri(RDFS.LABEL)) != null) {
      if (entity.get(iri(IM.HAS_STATUS)) == null)
        entity.set(iri(IM.HAS_STATUS), iri(IM.ACTIVE));
    }
  }

  public static void createRepository() throws TTFilerException {
    log.info("Fast import of {} quad data", new Date());
    String pathDelimiter = "/";
    try {
      String config = configTTl;
      String data = dataPath;
      String preloadPath = preload;
      String command;
      if (!SystemUtils.OS_NAME.contains("Windows"))
        command = "importrdf preload -c " + config + "/config.ttl --force -q "
          + data + " " + data + "/BulkImport*.nq";
      else
        command = "importrdf preload -c " + config + "\\config.ttl --force -q "
          + data + " " + data + "\\BulkImport*.nq";
      String startCommand = SystemUtils.OS_NAME.contains("Windows") ? "cmd /c " : "bash ";

      log.info("Executing command [{}{}]", startCommand, command);

      Process process = Runtime.getRuntime()
        .exec(startCommand + command,
          null, new File(preloadPath));
      BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader e = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      String line = r.readLine();
      while (line != null) {
        log.info(line);
        line = r.readLine();
      }

      boolean error = false;
      line = e.readLine();
      while (line != null) {
        error = true;
        log.error(line);
        line = e.readLine();
      }

      process.waitFor();

      if (error || process.exitValue() != 0) {
        throw new TTFilerException("Bulk import failed");
      }
      File directory = new File(data + pathDelimiter);
      try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory.toPath())) {
        for (Path file : directoryStream) {
          if (!Files.isDirectory(file)) {
            Files.delete(file);
          }
        }
      }
    } catch (IOException e) {
      throw new TTFilerException(e.getMessage());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new TTFilerException(e.getMessage());
    }
  }

  public static String getDataPath() {
    return dataPath;
  }

  public static void setDataPath(String dataPath) {
    TTBulkFiler.dataPath = dataPath;
  }

  public static String getConfigTTl() {
    return configTTl;
  }

  public static void setConfigTTl(String configTTl) {
    TTBulkFiler.configTTl = configTTl;
  }

  public static String getPreload() {
    return preload;
  }

  public static void setPreload(String preload) {
    TTBulkFiler.preload = preload;
  }

  public static int getPrivacyLevel() {
    return privacyLevel;
  }

  public static void setPrivacyLevel(int privacyLevel) {
    TTBulkFiler.privacyLevel = privacyLevel;
  }

  public static int getStatementCount() {
    return statementCount;
  }

  public static void setStatementCount(int statementCount) {
    TTBulkFiler.statementCount = statementCount;
  }

  private static void incrementStatementCount() {
    statementCount++;
  }

  public void fileDocument(TTDocument document) throws TTFilerException {
    if (document.getEntities() == null)
      return;
    if (document.getEntities().isEmpty())
      return;

    validateDocument(document);

    writeGraph(document, insertGraph);
  }

  private void validateDocument(TTDocument document) throws TTFilerException {
    for (TTEntity entity : document.getEntities()) {
      if (entity.getScheme() == null) {
        if (entity.getCrud() == null) {
          throw new TTFilerException("Missing entity scheme for entity " + entity.getIri());
        } else if (!Set.of(IM.ADD_QUADS.toString(), IM.UPDATE_PREDICATES.toString()).contains(entity.getCrud().getIri()))
          throw new TTFilerException("Missing entity scheme for entity " + entity.getIri());
      }
    }
  }

  private void writeGraph(TTDocument document, Graph graph) throws TTFilerException {
    try {
      createFileWriters();

      int counter = 0;
      TTToNQuad converter = new TTToNQuad();
      log.info("Writing document entities...");
      for (TTEntity entity : document.getEntities()) {
        counter++;
        if (entity.get(IM.PRIVACY_LEVEL.asIri()) != null && (entity.get(IM.PRIVACY_LEVEL.asIri()).asLiteral().intValue() > getPrivacyLevel()))
          continue;

        allEntities.write(entity.getIri() + "\n");
        if (Graph.IM.equals(graph))
          coreIris.write(entity.getIri() + "\t" + entity.getName() + "\n");
        if (entity.getScheme() == null) {
          transformAndWriteQuads(converter, entity, graph);
        } else {
          addToMaps(entity);
          addSubtypes(entity);
          addTerms(entity);
          setStatus(entity);
          transformAndWriteQuads(converter, entity, graph);
          if (counter % 100000 == 0) {
            log.info("{} entities written", counter);
          }
        }

      }
      log.debug("{} entities written to file", counter);
      log.info("Finished - total of {} statements,  {}", statementCount, new Date());
    } catch (Exception e) {
      throw new TTFilerException(e.getMessage());
    } finally {
      closeFileWriters();
    }
  }

  private void transformAndWriteQuads(TTToNQuad converter, TTEntity entity, Graph graph) throws IOException {
    List<String> quadList = converter.transformEntity(entity, graph);
    for (String quad : quadList) {
      quads.write(quad + "\n");
      incrementStatementCount();
    }
  }

  private void createFileWriters() throws IOException {
    quads = new FileWriter(dataPath + "/BulkImport.nq", true);
    codeMap = new FileWriter(dataPath + "/CodeMap.txt", true);
    subtypes = new FileWriter(dataPath + "/SubTypes.txt", true);
    allEntities = new FileWriter(dataPath + "/Entities.txt", true);
    descendants = new FileWriter(dataPath + "/Descendants.txt", true);
    coreTerms = new FileWriter(dataPath + "/CoreTerms.txt", true);
    legacyCore = new FileWriter(dataPath + "/LegacyCore.txt", true);
    coreIris = new FileWriter(dataPath + "/coreIris.txt", true);

    termCoreMap = new EnumMap<>(Namespace.class);
    codeCoreMap = new EnumMap<>(Namespace.class);
    codeIds = new EnumMap<>(Namespace.class);
  }

  private FileWriter getTermCoreMap(Namespace namespace) throws IOException {
    FileWriter result = termCoreMap.get(namespace);
    if (result == null) {
      result = new FileWriter(dataPath + "/TermCoreMap-" + namespace.name() + ".txt", true);
      termCoreMap.put(namespace, result);
    }

    return result;
  }

  private FileWriter getCodeCoreMap(Namespace namespace) throws IOException {
    FileWriter result = codeCoreMap.get(namespace);
    if (result == null) {
      result = new FileWriter(dataPath + "/CodeCoreMap-" + namespace.name() + ".txt", true);
      codeCoreMap.put(namespace, result);
    }

    return result;
  }

  private FileWriter getCodeIds(Namespace namespace) throws IOException {
    FileWriter result = codeIds.get(namespace);
    if (result == null) {
      result = new FileWriter(dataPath + "/CodeIds-" + namespace.name() + ".txt", true);
      codeIds.put(namespace, result);
    }

    return result;
  }

  private void closeFileWriters() {
    try {
      quads.close();
      codeMap.close();
      subtypes.close();
      descendants.close();
      coreTerms.close();
      legacyCore.close();
      allEntities.close();
      coreIris.close();

      for (FileWriter fileWriter : codeCoreMap.values())
        fileWriter.close();

      for (FileWriter fileWriter : codeIds.values())
        fileWriter.close();

      for (FileWriter fileWriter : termCoreMap.values())
        fileWriter.close();
    } catch (IOException e) {
      log.warn("Failed to close one or more file writers");
    }
  }

  private void addTerms(TTEntity entity) throws IOException {
    if (entity.getScheme().getIri().equals(Namespace.IM.toString()) && entity.getName() != null)
      coreTerms.write(entity.getName() + "\t" + entity.getIri() + "\n");
  }

  private void addSubtypes(TTEntity entity) throws IOException {
    for (TTIriRef relationship : List.of(iri(RDFS.SUBCLASS_OF), iri(RDFS.SUB_PROPERTY_OF),
      iri(IM.LOCAL_SUBCLASS_OF))) {
      if (entity.get(relationship) != null)
        for (TTValue parent : entity.get(relationship).getElements()) {
          subtypes.write(entity.getIri() + "\t" + relationship.getIri() + "\t" + parent.asIriRef().getIri() + "\n");
          if (specialChildren.contains(parent.asIriRef().getIri()))
            descendants.write(parent.asIriRef().getIri() + "\t" + entity.getIri() + "\t" + entity.getName() + "\n");
        }
    }
  }

  private void addToMaps(TTEntity entity) throws IOException {
    addCodeToMaps(entity);
    addCodeIdToMaps(entity);
    addTermCodeToMaps(entity);
    addMatchToToMaps(entity);
  }

  private void addCodeToMaps(TTEntity entity) throws IOException {
    Namespace namespace = Namespace.from(entity.getScheme().getIri());

    if (entity.get(TTIriRef.iri(IM.ALTERNATIVE_CODE)) != null) {
      codeMap.write(namespace + entity.get(TTIriRef.iri(IM.ALTERNATIVE_CODE)).asLiteral().getValue() + "\t" + entity.getIri() + "\n");
      if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED)))
        getCodeCoreMap(namespace).write(entity.get(TTIriRef.iri(IM.ALTERNATIVE_CODE)).asLiteral().getValue() + "\t" + entity.getIri() + "\n");
    } else {
      if (entity.getCode() != null) {
        codeMap.write(namespace + entity.getCode() + "\t" + entity.getIri() + "\n");
        if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED)))
          getCodeCoreMap(namespace).write(entity.getCode() + "\t" + entity.getIri() + "\n");
      }
    }
  }

  private void addCodeIdToMaps(TTEntity entity) throws IOException {
    if (entity.get(iri(IM.CODE_ID)) != null) {
      Namespace namespace = Namespace.from(entity.getScheme().getIri());
      for (TTValue codeId : entity.get(iri(IM.CODE_ID)).getElements()) {
        getCodeIds(namespace).write(codeId.asLiteral().getValue() + "\t" + entity.getIri() + "\n");
      }
    }
  }

  private void addTermCodeToMaps(TTEntity entity) throws IOException {
    Namespace namespace = Namespace.from(entity.getScheme().getIri());

    if (entity.get(iri(IM.HAS_TERM_CODE)) != null) {
      for (TTValue tc : entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
        if (tc.asNode().get(iri(IM.CODE)) != null) {
          String code = tc.asNode().get(iri(IM.CODE)).asLiteral().getValue();
          if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED)))
            getCodeCoreMap(namespace).write(code + "\t" + entity.getIri() + "\n");
        }
      }
    }
  }

  private void addMatchToToMaps(TTEntity entity) throws IOException {
    Namespace namespace = Namespace.from(entity.getScheme().getIri());

    if (entity.get(iri(IM.MATCHED_TO)) != null) {
      for (TTValue core : entity.get(iri(IM.MATCHED_TO)).getElements()) {
        if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED))) {
          legacyCore.write(entity.getIri() + "\t" + core.asIriRef().getIri() + "\n");
          if (entity.get(iri(IM.CODE_ID)) != null)
            getCodeIds(namespace).write(entity.get(iri(IM.CODE_ID)).asLiteral().getValue() + "\t" +
              core.asIriRef().getIri() + "\n");
        }
        getCodeCoreMap(namespace).write(entity.getCode() + "\t" + core.asIriRef().getIri() + "\n");
        writeTermCoreMap(namespace, entity.getName(), core.asIriRef().getIri());
        addMatchToHasTermCode(entity, core);
      }
    }
  }

  private void addMatchToHasTermCode(TTEntity entity, TTValue core) throws IOException {

    if (entity.get(iri(IM.HAS_TERM_CODE)) != null) {
      Namespace namespace = Namespace.from(entity.getScheme().getIri());

      for (TTValue tc : entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
        TTNode termCode = tc.asNode();
        if (termCode.get(iri(IM.CODE)) != null) {
          String code = termCode.get(iri(IM.CODE)).asLiteral().getValue();
          getCodeCoreMap(namespace).write(code + "\t" + core.asIriRef().getIri() + "\n");
        }
        if (termCode.get(iri(RDFS.LABEL)) != null) {
          String term = termCode.get(iri(RDFS.LABEL)).asLiteral().getValue();
          writeTermCoreMap(namespace, term, core.asIriRef().getIri() + "\n");
        }
      }
    }
  }

  private void writeTermCoreMap(Namespace namespace, String term, String core) throws IOException {
    if (term == null)
      return;

    getTermCoreMap(namespace).write(term + "\t" + core + "\n");
    byte[] arr = term.getBytes(StandardCharsets.UTF_8);
    if (arr.length != term.length()) {
      StringBuilder newTerm = new StringBuilder();
      for (byte b : arr) {
        if (b > 0)
          newTerm.append((char) b);
      }
      getTermCoreMap(namespace).write(newTerm + "\t" + core + "\n");
    }
  }

  @Override
  public void close() {
    //do nothing
  }
}
