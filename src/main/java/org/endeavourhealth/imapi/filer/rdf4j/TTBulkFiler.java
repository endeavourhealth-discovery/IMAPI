package org.endeavourhealth.imapi.filer.rdf4j;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTToNQuad;
import org.endeavourhealth.imapi.vocabulary.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  private FileWriter termCoreMap;
  private FileWriter coreTerms;
  private FileWriter quads;
  private FileWriter subtypes;
  private FileWriter codeCoreMap;
  private FileWriter descendants;
  private FileWriter legacyCore;
  private FileWriter allEntities;
  private FileWriter codeIds;
  private FileWriter coreIris;

  private static void setStatusAndScheme(TTEntity entity) {
    if (entity.get(iri(RDFS.LABEL)) != null) {
      if (entity.get(iri(IM.HAS_STATUS)) == null)
        entity.set(iri(IM.HAS_STATUS), iri(IM.ACTIVE));
      if (entity.get(iri(IM.HAS_SCHEME)) == null)
        entity.set(iri(IM.HAS_SCHEME), TTIriRef.iri(getScheme(entity.getIri())));
    }
  }

  private static String getScheme(String iri) {
    if (iri.contains("#"))
      return iri.substring(0, iri.lastIndexOf("#") + 1);
    else
      return iri.substring(0, iri.lastIndexOf("/") + 1);
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
    writeGraph(document);

  }

  @Override
  public void writeLog(TTDocument document) {
    throw new UnsupportedOperationException("TTBulkFiler does not support writeLog");
  }

  @Override
  public void fileDeltas(String dataPath) {
    throw new UnsupportedOperationException("Deltas cannot be filed by a bulk filer. Set Filer Factory bulk to false");
  }

  private void writeGraph(TTDocument document) throws TTFilerException {

    String namespaceIri = document.getNamespace().getIri();
    if (namespaceIri == null)
      throw new TTFilerException("Document has no namespace set");

    Namespace namespace = Namespace.from(namespaceIri);
    if (namespace == null)
      throw new TTFilerException("Unrecognized namespace [" +  namespaceIri + "]");

    Graph graph = Graph.from(namespace.toString());
    if (graph == null)
      throw new TTFilerException("Unrecognized graph [" +  namespaceIri + "]");

    SCHEME scheme = SCHEME.from(namespace.toString());
    if (scheme == null)
      throw new TTFilerException("Unrecognized scheme [" +  namespaceIri + "]");

    String path = dataPath;

    try {
      createFileWriters(scheme, path);

      int counter = 0;
      TTToNQuad converter = new TTToNQuad();
      log.info("Writing out graph data for {}", graph);
      for (TTEntity entity : document.getEntities()) {
        counter++;
        Namespace entityNS = entity.getGraph() == null ? namespace : Namespace.from(entity.getGraph().toString());

        if (entity.get(IM.PRIVACY_LEVEL.asIri()) != null && (entity.get(IM.PRIVACY_LEVEL.asIri()).asLiteral().intValue() > getPrivacyLevel()))
          continue;

        if (entityNS == null)
          throw new TTFilerException("Unrecognized namespace on entity [" +  entity.getGraph() + "]");


        allEntities.write(entity.getIri() + "\n");
        if (graph.equals(Graph.IM))
          coreIris.write(entity.getIri() + "\t" + entity.getName() + "\n");
        addToMaps(entity, entityNS);
        addSubtypes(entity);
        addTerms(entity, entityNS);

        setStatusAndScheme(entity);

        transformAndWriteQuads(converter, entity, graph);
        if (counter % 100000 == 0) {
          log.info("{} entities from {} written", counter, document.getNamespace().getIri());
        }

      }
      log.debug("{} entities written to file for {}", counter, document.getNamespace().getIri());
      log.info("Finished - total of {} statements,  {}", statementCount, new Date());
    } catch (Exception e) {
      throw new TTFilerException(e.getMessage());
    } finally {
      closeFileWriters();
    }
  }

  private void transformAndWriteQuads(TTToNQuad converter, TTEntity entity, Graph entityGraph) throws IOException {
    List<String> quadList = converter.transformEntity(entity, entityGraph);
    for (String quad : quadList) {
      quads.write(quad + "\n");
      incrementStatementCount();
    }
  }

  private void createFileWriters(SCHEME scheme, String path) throws IOException {
    quads = new FileWriter(path + "/BulkImport" + ".nq", true);
    codeMap = new FileWriter(path + "/CodeMap.txt", true);
    termCoreMap = new FileWriter(path + "/TermCoreMap-" + scheme + ".txt", true);
    subtypes = new FileWriter(path + "/SubTypes" + ".txt", true);
    allEntities = new FileWriter(path + "/Entities" + ".txt", true);
    codeCoreMap = new FileWriter(path + "/CodeCoreMap-" + scheme + ".txt", true);
    codeIds = new FileWriter(path + "/CodeIds-" + scheme + ".txt", true);
    descendants = new FileWriter(path + "/Descendants" + ".txt", true);
    coreTerms = new FileWriter(path + "/CoreTerms" + ".txt", true);
    legacyCore = new FileWriter(path + "/LegacyCore" + ".txt", true);
    coreIris = new FileWriter(path + "/coreIris.txt", true);
  }

  private void closeFileWriters() {
    try {
      quads.close();
      codeMap.close();
      termCoreMap.close();
      subtypes.close();
      codeCoreMap.close();
      descendants.close();
      coreTerms.close();
      legacyCore.close();
      allEntities.close();
      codeIds.close();
      coreIris.close();
    } catch (IOException e) {
      log.warn("Failed to close one or more file writers");
    }
  }

  private void addTerms(TTEntity entity, Namespace namespace) throws IOException {
    if (namespace.equals(Namespace.IM) && entity.getName() != null)
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

  private void addToMaps(TTEntity entity, Namespace namespace) throws IOException {
    addCodeToMaps(entity, namespace);
    addCodeIdToMaps(entity);
    addTermCodeToMaps(entity, namespace);
    addMatchToToMaps(entity, namespace);
  }

  private void addCodeToMaps(TTEntity entity, Namespace namespace) throws IOException {
    if (entity.get(TTIriRef.iri(IM.ALTERNATIVE_CODE)) != null) {
      codeMap.write(namespace + entity.get(TTIriRef.iri(IM.ALTERNATIVE_CODE)).asLiteral().getValue() + "\t" + entity.getIri() + "\n");
      if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED)))
        codeCoreMap.write(entity.get(TTIriRef.iri(IM.ALTERNATIVE_CODE)).asLiteral().getValue() + "\t" + entity.getIri() + "\n");
    } else {
      if (entity.getCode() != null) {
        codeMap.write(namespace + entity.getCode() + "\t" + entity.getIri() + "\n");
        if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED)))
          codeCoreMap.write(entity.getCode() + "\t" + entity.getIri() + "\n");
      }
    }
  }

  private void addCodeIdToMaps(TTEntity entity) throws IOException {
    if (entity.get(iri(IM.CODE_ID)) != null) {
      for (TTValue codeId : entity.get(iri(IM.CODE_ID)).getElements()) {
        codeIds.write(codeId.asLiteral().getValue() + "\t" + entity.getIri() + "\n");
      }
    }
  }

  private void addTermCodeToMaps(TTEntity entity, Namespace namespace) throws IOException {
    if (entity.get(iri(IM.HAS_TERM_CODE)) != null) {
      for (TTValue tc : entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
        if (tc.asNode().get(iri(IM.CODE)) != null) {
          String code = tc.asNode().get(iri(IM.CODE)).asLiteral().getValue();
          if (namespace.equals(Namespace.IM) || (namespace.equals(Namespace.SNOMED)))
            codeCoreMap.write(code + "\t" + entity.getIri() + "\n");
        }
      }
    }
  }

  private void addMatchToToMaps(TTEntity entity, Namespace namespace) throws IOException {
    boolean isCoreGraph = namespace.equals(Namespace.IM) || namespace.equals(Namespace.SNOMED);

    if (entity.get(iri(IM.MATCHED_TO)) != null) {
      for (TTValue core : entity.get(iri(IM.MATCHED_TO)).getElements()) {
        if (!isCoreGraph) {
          legacyCore.write(entity.getIri() + "\t" + core.asIriRef().getIri() + "\n");
          if (entity.get(iri(IM.CODE_ID)) != null)
            codeIds.write(entity.get(iri(IM.CODE_ID)).asLiteral().getValue() + "\t" +
              core.asIriRef().getIri() + "\n");
        }
        codeCoreMap.write(entity.getCode() + "\t" + core.asIriRef().getIri() + "\n");
        writeTermCoreMap(entity.getName(), core.asIriRef().getIri());
        addMatchToHasTermCode(entity, core);
      }
    }
  }

  private void addMatchToHasTermCode(TTEntity entity, TTValue core) throws IOException {

    if (entity.get(iri(IM.HAS_TERM_CODE)) != null) {
      for (TTValue tc : entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
        TTNode termCode = tc.asNode();
        if (termCode.get(iri(IM.CODE)) != null) {
          String code = termCode.get(iri(IM.CODE)).asLiteral().getValue();
          codeCoreMap.write(code + "\t" + core.asIriRef().getIri() + "\n");
        }
        if (termCode.get(iri(RDFS.LABEL)) != null) {
          String term = termCode.get(iri(RDFS.LABEL)).asLiteral().getValue();
          writeTermCoreMap(term, core.asIriRef().getIri() + "\n");
        }
      }
    }
  }

  private void writeTermCoreMap(String term, String core) throws IOException {
    if (term == null)
      return;

    termCoreMap.write(term + "\t" + core + "\n");
    byte[] arr = term.getBytes(StandardCharsets.UTF_8);
    if (arr.length != term.length()) {
      StringBuilder newTerm = new StringBuilder();
      for (byte b : arr) {
        if (b > 0)
          newTerm.append((char) b);
      }
      termCoreMap.write(newTerm + "\t" + core + "\n");
    }
  }

  @Override
  public void close() {
    //do nothing
  }
}
