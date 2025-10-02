package org.endeavourhealth.imapi.logic.exporters;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.nio.file.Path;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class SetMemberExport {

  private static final String MODE = System.getenv("HOSTING_MODE");
  private static final String ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");
  private static final String SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
  private static final String BUCKET_REGION = System.getenv("TCT_BUCKET_REGION");
  private static final String BUCKET_NAME = System.getenv("TCT_BUCKET_NAME");

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

    if (iris == null || iris.isEmpty()) {
      SetMemberExport.executeConcept(baseFilename + "_tct_members.csv", null);
    } else {
      for (String iri : iris)
        SetMemberExport.executeConcept(baseFilename + "_tct_members.csv", iri);
    }

    if (iris == null || iris.isEmpty()) {
      SetMemberExport.executeConceptSet(baseFilename + "_set_members.csv", null);
    } else {
      for (String iri : iris)
        SetMemberExport.executeConceptSet(baseFilename + "_set_members.csv", iri);
    }

  }

  private static void executeConcept(String fileName, String iri) {
    try (IMDB conn = IMDB.getConnection()) {
      SetMemberExport.runExport(fileName, conn, """
        select ?set ?member ?im1Id
        where {
            ?set rdf:type im:Concept ;
                ^rdfs:subClassOf* ?member .
            ?member im:im1Id ?im1Id .
        }
        """, iri);
    }
  }

  private static void executeConceptSet(String fileName, String iri) {
    try (IMDB conn = IMDB.getConnection()) {
      SetMemberExport.runExport(fileName, conn, """
        select ?set ?member ?im1Id
        where {
            ?set rdf:type im:ConceptSet ;
                im:hasMember ?member .
            ?member im:im1Id ?im1Id .
        }
        """, iri);
    }
  }

  private static void runExport(String fileName, IMDB conn, String spql, String iri) {
    TupleQuery qry = conn.prepareTupleSparql(spql);

    if (null != iri && !iri.isEmpty())
      qry.setBinding("set", Values.iri(iri));

    try (TupleQueryResult rs = qry.evaluate()) {
      if (rs.hasNext()) {
        if (iri == null || iri.isEmpty())
          log.info("Exporting all...");
        else
          log.info("Exporting [{}]", iri);

        export(fileName, rs);
      }
    }
  }

  private static void export(String fileName, TupleQueryResult rs) {
    StringBuilder builder = new StringBuilder();
    int members = 0;

    while (rs.hasNext()) {
      BindingSet bs = rs.next();
      String set = bs.getValue("set").stringValue();
      String member = bs.getValue("member").stringValue();
      String im1Id = bs.getValue("im1Id") == null ? "" : bs.getValue("im1Id").stringValue();

      if (!MODE.equals("production")) {
        try (FileWriter fw = new FileWriter(fileName)) {
          fw.write(set + "\t" + member + "\t" + im1Id + "\n");
        } catch (IOException e) {
          log.error("Failed to export file: {}", fileName, e);
        }
      } else {
        builder.append(set).append("\t").append(member).append("\t").append(im1Id).append("\n");
      }
      if (++members % 100_000 == 0)
        log.info("Exported {} members...", members);
    }

    if (MODE.equals("production"))
      exportToBucket(builder.toString(), fileName);

    log.info("Finished exporting {} members", members);
  }

  private static void exportToBucket(String data, String fileName) {
    StaticCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY_ID, SECRET_ACCESS_KEY));

    try (S3Client client = S3Client.builder()
      .region(Region.of(BUCKET_REGION))
      .credentialsProvider(provider)
      .build()) {

      byte[] byteData = data.getBytes();
      InputStream stream = new ByteArrayInputStream(byteData);

      PutObjectRequest objRequest = PutObjectRequest.builder()
        .bucket(BUCKET_NAME)
        .key(fileName)
        .contentLength((long) byteData.length)
        .contentType("text/plain")
        .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
        .build();

      client.putObject(objRequest, RequestBody.fromInputStream(stream, byteData.length));
    }
  }

  private SetMemberExport() {
    // prevent instantiation - static methods only
  }
}
