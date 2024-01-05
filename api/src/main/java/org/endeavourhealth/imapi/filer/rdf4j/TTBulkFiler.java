package org.endeavourhealth.imapi.filer.rdf4j;

import org.apache.commons.lang3.SystemUtils;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTToNQuad;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTBulkFiler  implements TTDocumentFiler {
    private static final Logger LOG = LoggerFactory.getLogger(TTBulkFiler.class);
    private static String dataPath;
    private static String configTTl;
    private static String preload;
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
    private static int privacyLevel=0;
    private static int statementCount;
    private static final Set<String> specialChildren= new HashSet<>(List.of(SNOMED.NAMESPACE + "92381000000106"));

    public void fileDocument(TTDocument document) throws TTFilerException {
        if (document.getEntities() == null)
            return;
        if (document.getEntities().isEmpty())
            return;
        writeGraph(document);

    }

    @Override
    public void writeLog(TTDocument document) throws Exception {

    }

    @Override
    public void fileDeltas(String dataPath) throws Exception {
        throw new Exception("Deltas cannot be filed by a bulk filer. Set Filer Factory bulk to false");
    }


    private void writeGraph(TTDocument document) throws TTFilerException {

        String graph = null;
        if (document.getGraph() != null) {
            graph = document.getGraph().getIri();
        }
        String scheme = graph.substring(graph.lastIndexOf("/") + 1);
        String path = dataPath;

        try {
            createFileWriters(scheme, path);

            int counter = 0;
            TTToNQuad converter = new TTToNQuad();
            LOG.info("Writing out graph data for " + graph);
            for (TTEntity entity : document.getEntities()) {
                String entityGraph = entity.getGraph() != null ? entity.getGraph().getIri() : graph;
                if (entity.get(iri(IM.PRIVACY_LEVEL)) != null && (entity.get(iri(IM.PRIVACY_LEVEL)).asLiteral().intValue() > getPrivacyLevel()))
                    continue;

                allEntities.write(entity.getIri() + "\n");
                if (graph.equals(IM.NAMESPACE))
                    coreIris.write(entity.getIri() + "\t" + entity.getName() + "\n");
                addToMaps(entity, entityGraph);
                addSubtypes(entity);
                addTerms(entity, entityGraph);

                setStatusAndScheme(graph, entity);

                transformAndWriteQuads(converter, entity, entityGraph);

                if (counter++ % 100000 == 0)
                    LOG.info("Written {} entities for {}", counter, document.getGraph().getIri());
            }
            LOG.debug("{} entities written to file", counter);
            LOG.info("Finished - total of {} statements,  {}", statementCount, new Date());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new TTFilerException(e.getMessage());
        } finally {
            closeFileWriters();
        }
    }

    private static void setStatusAndScheme(String graph, TTEntity entity) {
        if (entity.get(iri(RDFS.LABEL)) != null) {
            if (entity.get(iri(IM.HAS_STATUS)) == null)
                entity.set(iri(IM.HAS_STATUS), iri(IM.ACTIVE));
            if (entity.get(iri(IM.HAS_SCHEME)) == null)
                entity.set(iri(IM.HAS_SCHEME), TTIriRef.iri(graph));
        }
    }

    private void transformAndWriteQuads(TTToNQuad converter, TTEntity entity, String entityGraph) throws IOException {
        List<String> quadList = converter.transformEntity(entity, entityGraph);
        for (String quad : quadList) {
            quads.write(quad + "\n");
            statementCount++;
        }
    }

    private void createFileWriters(String scheme, String path) throws IOException {
        quads = new FileWriter(path + "/BulkImport" + ".nq", true);
        //quads = new FileWriter(path + "/BulkImport-" + fileNumber + ".nq");
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
            LOG.warn("Failed to close one or more file writers");
        }
    }

    private void addTerms(TTEntity entity, String graph) throws IOException {
        boolean isCoreGraph= graph.equals(IM.NAMESPACE)||graph.equals(SNOMED.NAMESPACE);
        if (isCoreGraph && entity.getName()!=null)
            coreTerms.write(entity.getName()+"\t"+ entity.getIri()+"\n");
    }

    private void addSubtypes(TTEntity entity) throws IOException {
        for (TTIriRef relationship:List.of(iri(RDFS.SUBCLASS_OF),iri(RDFS.SUB_PROPERTY_OF),iri(IM.SUBSUMED_BY),iri(IM.USUALLY_SUBSUMED_BY),iri(IM.APPROXIMATE_SUBSUMED_BY),
          iri(IM.LOCAL_SUBCLASS_OF),iri(IM.MULTIPLE_SUBSUMED_BY))) {
            if (entity.get(relationship) != null)
                for (TTValue parent : entity.get(relationship).getElements()) {
                    subtypes.write(entity.getIri() + "\t" + relationship.getIri() + "\t" + parent.asIriRef().getIri() + "\n");
                    if (specialChildren.contains(parent.asIriRef().getIri()))
                        descendants.write(parent.asIriRef().getIri() + "\t" + entity.getIri() + "\t" + entity.getName() + "\n");
                }
        }
    }

    private void addToMaps(TTEntity entity,String graph) throws IOException {
        addCodeToMaps(entity, graph);
        addCodeIdToMaps(entity);
        addTermCodeToMaps(entity, graph);
        addMatchToToMaps(entity, graph);
    }

    private void addCodeToMaps(TTEntity entity, String graph) throws IOException {
        if (entity.getCode()!=null){
            codeMap.write(graph+entity.getCode()+"\t"+ entity.getIri()+"\n");
            if (graph.equals(IM.NAMESPACE)|| (graph.equals(SNOMED.NAMESPACE)))
                codeCoreMap.write(entity.getCode()+"\t"+ entity.getIri()+"\n");
        }
    }

    private void addCodeIdToMaps(TTEntity entity) throws IOException {
        if (entity.get(iri(IM.CODE_ID))!=null) {
            for (TTValue codeId : entity.get(iri(IM.CODE_ID)).getElements()) {
                codeIds.write(codeId.asLiteral().getValue() + "\t" + entity.getIri() + "\n");
            }
        }
    }

    private void addTermCodeToMaps(TTEntity entity, String graph) throws IOException {
        if (entity.get(iri(IM.HAS_TERM_CODE))!=null){
                for (TTValue tc: entity.get(iri(IM.HAS_TERM_CODE)).getElements()) {
                    if (tc.asNode().get(iri(IM.CODE)) != null) {
                        String code = tc.asNode().get(iri(IM.CODE)).asLiteral().getValue();
                        if (graph.equals(IM.NAMESPACE)|| (graph.equals(SNOMED.NAMESPACE)))
                            codeCoreMap.write(code + "\t" + entity.getIri() + "\n");
                    }
                }
        }
    }

    private void addMatchToToMaps(TTEntity entity, String graph) throws IOException {
        boolean isCoreGraph= graph.equals(IM.NAMESPACE)||graph.equals(SNOMED.NAMESPACE);

        if (entity.get(iri(IM.MATCHED_TO))!=null) {
            for (TTValue core : entity.get(iri(IM.MATCHED_TO)).getElements()) {
                if (!isCoreGraph) {
                    legacyCore.write(entity.getIri() + "\t" + core.asIriRef().getIri() + "\n");
                    if (entity.get(iri(IM.CODE_ID))!=null)
                        codeIds.write(entity.get(iri(IM.CODE_ID)).asLiteral().getValue()+"\t"+
                                                       core.asIriRef().getIri()+"\n");
                }
                codeCoreMap.write(entity.getCode()+"\t"+ core.asIriRef().getIri()+"\n");
                writeTermCoreMap(entity.getName(),core.asIriRef().getIri());
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
                    codeCoreMap.write(code+"\t"+core.asIriRef().getIri()+"\n");
                }
                if (termCode.get(iri(RDFS.LABEL)) != null) {
                    String term = termCode.get(iri(RDFS.LABEL)).asLiteral().getValue();
                    writeTermCoreMap(term, core.asIriRef().getIri()+"\n");
                }
            }
        }
    }

    private void writeTermCoreMap(String term,String core) throws IOException {
        if (term== null)
            return;

        termCoreMap.write(term+"\t"+ core+"\n");
        byte[] arr=term.getBytes(StandardCharsets.UTF_8);
        if (arr.length != term.length()){
            StringBuilder newTerm= new StringBuilder();
            for (byte b : arr) {
                if (b > 0)
                    newTerm.append((char) b);
            }
            termCoreMap.write(newTerm+"\t"+ core+"\n");
        }
    }

 public static void createRepository() throws TTFilerException {
     LOG.info("Fast import of {} quad data", new Date());
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

         LOG.info("Executing command [{}]", startCommand + command);

         Process process = Runtime.getRuntime()
                 .exec(startCommand + command,
                         null, new File(preloadPath));
         BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
         BufferedReader e = new BufferedReader(new InputStreamReader(process.getErrorStream()));

         String line = r.readLine();
         while (line != null) {
             System.out.println(line);
             line = r.readLine();
         }

         boolean error = false;
         line = e.readLine();
         while (line != null) {
             error = true;
             System.err.println(line);
             line = e.readLine();
         }

         process.waitFor();

         if (error || process.exitValue() != 0) {
             System.err.println("Bulk import failed");
             throw new TTFilerException("Bulk import failed");
         }

         File directory = new File(data + pathDelimiter);
         for (File file : Objects.requireNonNull(directory.listFiles())) {
             if (!file.isDirectory() && !file.delete())
                 LOG.error("File delete failed");
         }
     } catch (IOException | InterruptedException e) {
         LOG.error(e.getMessage());
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

    @Override
    public void close() throws Exception {
        //do nothing
    }
}
