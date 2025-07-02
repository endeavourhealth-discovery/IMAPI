package org.endeavourhealth.imapi.filer.rdf4j;

import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.FileRepository;
import org.endeavourhealth.imapi.filer.TCGenerator;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Slf4j
public class ClosureGeneratorBulk implements TCGenerator {
  private static final String[] topConcepts = {"http://snomed.info/sct#138875005", IM.CONCEPT.toString(), "http://snomed.info/sct#370115009"};
  private final Set<String> blockingIris = new HashSet<>();
  private Map<String, Map<String, Set<String>>> relationshipMap;
  private HashMap<String, Set<String>> closureMap;
  private int counter;

  @Override
  public void generateClosure(String outpath, boolean secure) throws IOException {
    getTctBlockers();
    FileRepository repo = new FileRepository(outpath);

    relationshipMap = HashMap.newHashMap(1_000_000);
    log.info("Getting all subtypes....");
    repo.fetchRelationships(relationshipMap, blockingIris);


    try (FileWriter isas = new FileWriter(outpath + "/BulkImport.nq", true)) {
      buildClosure();
      writeClosureData(isas);

    }
  }


  private void getTctBlockers() {
    log.debug("Getting top level stoppers");
    blockingIris.addAll(Arrays.asList(topConcepts));
    //For now, only exclude top snomed and im concept
  }


  private void buildClosure() {
    closureMap = HashMap.newHashMap(10_000_000);
    log.debug("Generating closure map for subclasses");
    int c = 0;
    counter = 0;
    for (Map.Entry<String, Set<String>> row : relationshipMap.get(RDFS.SUBCLASS_OF.toString()).entrySet()) {
      c++;
      String child = row.getKey();
      if (closureMap.get(child) == null) {
        if (c % 100000 == 0)
          log.debug("Processed {} entities", c);
        generateClosure(child);
      }
    }
    log.debug("Closure built with {} triples with {} keys", counter, closureMap.size());
  }


  private Set<String> generateClosure(String child) {
    Set<String> closures = closureMap.computeIfAbsent(child, k -> new HashSet<>());
    String relationship = RDFS.SUBCLASS_OF.toString();

    // Add self
    closures.add(child);
    counter++;

    Set<String> parents = relationshipMap.get(relationship).get(child);
    if (parents != null) {
      for (String parent : parents) {
        // Check do we have its closure?
        Set<String> parentIsAs = closureMap.get(parent);
        if (parentIsAs == null) {
          parentIsAs = generateClosure(parent);
        }
        // Add parents closure to this closure
        for (String parentIsA : parentIsAs) {
          if (!closures.contains(parentIsA)) {
            closures.add(parentIsA);
            counter++;
          }
        }
      }
    }
    return closures;
  }

  private void writeClosureData(FileWriter fw) throws IOException {
    counter = 0;
    int max = 0;
    log.info("Writing closure data");
    for (Map.Entry<String, Set<String>> entry : closureMap.entrySet()) {
      int isas = entry.getValue().size();
      if (isas > max) {
        log.info("{} has {} isas", entry.getKey(), isas);
        max = isas;
      }
      for (String closure : entry.getValue()) {
        counter++;
        TTBulkFiler.setStatementCount(TTBulkFiler.getStatementCount() + 1);
        if (counter % 1_000_000 == 0)
          log.info("Written {} isas ", counter);
        fw.write("<" + entry.getKey() + "> <" + IM.IS_A + "> <" + closure + "> <" + IM.NAMESPACE + ">.\n");
      }
    }
    fw.close();
    log.debug("{} Closure triples written", counter);
    log.debug("statement count {}", TTBulkFiler.getStatementCount());
  }

}
