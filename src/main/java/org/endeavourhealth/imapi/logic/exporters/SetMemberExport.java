package org.endeavourhealth.imapi.logic.exporters;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.literal;

@Slf4j
public class SetMemberExport {
  public static void execute(Path basePath) {
    SetMemberExport.execute(basePath, Collections.emptyList());
  }

  public static void execute(Path basePath, String iri) {
    execute(basePath, List.of(iri));
  }

  public static void execute(Path basePath, List<String> iris) {
    Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String date = formatter.format(new java.util.Date());
    String baseFilename = date + "_" + basePath.getFileName();
    if (basePath.getParent() != null)
      baseFilename = basePath.getParent() + File.separator + baseFilename;

    try (FileWriter fwConcept = new FileWriter(baseFilename + "_members.csv")) {
      if (iris == null || iris.isEmpty()) {
        SetMemberExport.executeConcept(fwConcept, null);
      } else {
        for (String iri : iris)
          SetMemberExport.executeConcept(fwConcept, iri);
      }
    } catch (IOException e) {
      log.error("Failed to export set members to file", e);
    }

    try (FileWriter fwConceptSet = new FileWriter(baseFilename + "_set_members.csv")) {
      if (iris == null || iris.isEmpty()) {
        SetMemberExport.executeConceptSet(fwConceptSet, null);
      } else {
        for (String iri : iris)
          SetMemberExport.executeConceptSet(fwConceptSet, iri);
      }
    } catch (IOException e) {
      log.error("Failed to export set members to file", e);
    }
  }

  private static void executeConcept(FileWriter fw, String iri) throws IOException {
    try (IMDB conn = IMDB.getConnection()) {
      SetMemberExport.runExport(fw, conn, """
        select ?set ?member ?im1Id
        where {
            ?set rdf:type im:Concept ;
                ^rdfs:subClassOf* ?member .
            ?member im:im1Id ?im1Id .
        }
        """, iri);
    }
  }

  private static void executeConceptSet(FileWriter fw, String iri) throws IOException {
    try (IMDB conn = IMDB.getConnection()) {
      SetMemberExport.runExport(fw, conn, """
        select ?set ?member ?im1Id
        where {
            ?set rdf:type im:ConceptSet ;
                im:hasMember ?member .
            ?member im:im1Id ?im1Id .
        }
        """, iri);
    }
  }

  private static void runExport(FileWriter fw, IMDB conn, String spql, String iri) throws IOException {
    TupleQuery qry = conn.prepareTupleSparql(spql);

    if (null != iri && !iri.isEmpty())
      qry.setBinding("set", Values.iri(iri));

    try (TupleQueryResult rs = qry.evaluate()) {
      if (rs.hasNext()) {
        if (iri == null || iri.isEmpty())
          log.info("Exporting all...");
        else
          log.info("Exporting [{}]", iri);

        int members = 0;

        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String set = bs.getValue("set").stringValue();
          String member = bs.getValue("member").stringValue();
          String im1Id = bs.getValue("im1Id") == null ? "" : bs.getValue("im1Id").stringValue();
          fw.write(set + "\t" + member + "\t" + im1Id + "\n");

          if (++members % 100_000 == 0)
            log.info("Exported {} members...", members);
        }

        log.info("Finished exporting {} members", members);
      }
    }
  }

  private SetMemberExport() {
    // prevent instantiation - static methods only
  }
}
