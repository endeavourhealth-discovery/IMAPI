package org.endeavourhealth.imapi.logic.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.StringJoiner;

@Slf4j
@Component
public class SetExporter {

  public void publishSetToIM1(String setIri, String name, Set<Concept> members) throws JsonProcessingException {
    StringJoiner results = generateForIm1(setIri, name, members);
    pushToS3(results);
    log.trace("Done");
  }

  public StringJoiner generateForIm1(String setIri, String name, Set<Concept> members) throws JsonProcessingException {
    log.debug("Exporting set to IMv1");
    return generateIMV1TSV(setIri, name, members);
  }

  private StringJoiner generateIMV1TSV(String setIri, String name, Set<Concept> members) {
    log.trace("Generating output...");
    StringJoiner results = new StringJoiner(System.lineSeparator());
    results.add("vsId\tvsName\tmemberDbid");

    for (Concept member : members) {
      generateTSVAddResults(setIri, name, member.getIm1Id(), results, member);
      if (member.getMatchedFrom() != null) {
        for (Concept legacy : member.getMatchedFrom()) {
          generateTSVAddResults(setIri, name, member.getIm1Id(), results, legacy);
        }
      }
    }
    return results;
  }

  private void generateTSVAddResults(String setIri, String name, String im1Id, StringJoiner results, Concept member) {
    if (member.getIm1Id() != null) {
      results.add(
        new StringJoiner("\t")
          .add(setIri)
          .add(name)
          .add(im1Id)
          .toString());
    }
  }

  private void pushToS3(StringJoiner results) {
    log.trace("Publishing to S3...");
    String bucket = "im-inbound-dev";
    String region = "eu-west-2";
    String accessKey = null;
    String secretKey = null;

    String bucketEnv = System.getenv("IM1_PUBLISH_BUCKET");
    String regionEnv = System.getenv("IM1_PUBLISH_REGION");
    String accessKeyEnv = System.getenv("IM1_PUBLISH_ACCESS_KEY");
    String secretKeyEnv = System.getenv("IM1_PUBLISH_SECRET_KEY");
    if (bucketEnv != null) {
      bucket = bucketEnv;
    }
    if (regionEnv != null) {
      region = regionEnv;
    }
    if (accessKeyEnv != null) {
      accessKey = accessKeyEnv;
    }
    if (secretKeyEnv != null) {
      secretKey = secretKeyEnv;
    }

    if (accessKey == null || accessKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {
      throw new IllegalArgumentException("AccessKey or SecretKey cannot be empty");
    }

    try (S3Client s3 = S3Client.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))).build()) {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
      String filename = date.format(timestamp.getTime()) + "_valueset.tsv";

      byte[] byteData = results.toString().getBytes();
      InputStream stream = new ByteArrayInputStream(byteData);

      PutObjectRequest por = PutObjectRequest.builder()
        .bucket(bucket)
        .key(filename)
        .contentLength((long) byteData.length)
        .contentType("text/plain")
        .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
        .build();

      s3.putObject(por, RequestBody.fromInputStream(stream, byteData.length));
    }
  }
}
