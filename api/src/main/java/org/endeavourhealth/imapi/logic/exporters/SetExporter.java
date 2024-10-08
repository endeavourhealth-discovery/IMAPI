package org.endeavourhealth.imapi.logic.exporters;

import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.AWSConfig;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class SetExporter {
  private static final Logger LOG = LoggerFactory.getLogger(SetExporter.class);

  private EntityRepository entityRepository = new EntityRepository();
  private SetRepository setRepository = new SetRepository();

  public void publishSetToIM1(String setIri) throws JsonProcessingException, QueryException {
    StringJoiner results = generateForIm1(setIri);

    pushToS3(results);
    LOG.trace("Done");
  }

  public StringJoiner generateForIm1(String setIri) throws QueryException, JsonProcessingException {
    LOG.debug("Exporting set to IMv1");

    LOG.trace("Looking up set...");
    String name = entityRepository.getBundle(setIri, Set.of(RDFS.LABEL)).getEntity().getName();

    Set<Concept> members = getExpandedSetMembers(setIri, true, true, true, List.of());

    return generateIMV1TSV(setIri, name, members);
  }

  public Set<TTIriRef> getSubsetIrisWithNames(String iri) {
    Set<TTIriRef> subsets = setRepository.getSubsetIrisWithNames(iri);
    return new HashSet<>(subsets);
  }

  public Set<Concept> getExpandedSetMembers(String iri, boolean core, boolean legacy, boolean subsets, List<String> schemes) throws QueryException, JsonProcessingException {
    if (!(core || legacy || subsets))
      return new HashSet<>();

    Set<Concept> result = null;

    if (core || legacy) {
      result = tryGetExpandedSetMembersByDefinition(iri, legacy, schemes);

      // If nothing found from definition, try to get direct members
      if (null == result || result.isEmpty())
        result = setRepository.getSetMembers(iri, legacy, schemes);

      // If nothing found from members, try to get descendants
      if (null == result || result.isEmpty()) {
        Query descendantsOf = new Query()
          .match(f -> f
            .instanceOf(n -> n.setIri(iri)
              .setDescendantsOrSelfOf(true)));
        result = setRepository.getSetExpansion(descendantsOf, legacy, null, schemes);
      }
    }

    if (null == result)
      result = new HashSet<>();

    if (subsets) {
      expandSubsets(iri, core, legacy, schemes, result);
    }

    return result;
  }

  private void expandSubsets(String iri, boolean core, boolean legacy, List<String> schemes, Set<Concept> result) throws QueryException, JsonProcessingException {
    LOG.trace("Expanding subsets for {}...", iri);
    Set<TTIriRef> subSetIris = getSubsetIrisWithNames(iri);
    LOG.trace("Found {} subsets...", subSetIris.size());
    for (TTIriRef subset : subSetIris) {
      Set<Concept> subsetMembers = getExpandedSetMembers(subset.getIri(), core, legacy, true, schemes);
      if (null != subsetMembers && !subsetMembers.isEmpty()) {
        subsetMembers.forEach(ss -> ss.addIsContainedIn(
          new TTEntity(subset.getIri())
            .setName(subset.getName())
        ));
        result.addAll(subsetMembers);
      }
    }
  }

  private Set<Concept> tryGetExpandedSetMembersByDefinition(String iri, boolean legacy, List<String> schemeIris) throws JsonProcessingException, QueryException {

    TTEntity entity = entityRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION, RDFS.LABEL)).getEntity();
    if (null == entity)
      return Collections.emptySet();

    String name = entity.has(iri(RDFS.LABEL)) ? entity.getName() : "";

    Query definition = entity.has(iri(IM.DEFINITION)) ? entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class) : null;
    if (null == definition)
      return Collections.emptySet();

    Set<Concept> result = setRepository.getSetExpansion(definition, legacy, null, schemeIris);

    if (null != result && !result.isEmpty()) {
      LOG.trace("Found {} results", result.size());
      result.forEach(se -> se.addIsContainedIn(new TTEntity(iri).setName(name)));
    }


    return result;
  }

  private StringJoiner generateIMV1TSV(String setIri, String name, Set<Concept> members) {
    LOG.trace("Generating output...");

    Set<String> im1Ids = new HashSet<>();

    StringJoiner results = new StringJoiner(System.lineSeparator());
    results.add("vsId\tvsName\tmemberDbid");

    for (Concept member : members) {
      generateTSVAddResults(setIri, name, im1Ids, results, member);
      if (member.getMatchedFrom() != null) {
        for (Concept legacy : member.getMatchedFrom()) {
          generateTSVAddResults(setIri, name, im1Ids, results, legacy);
        }
      }
    }
    return results;
  }

  private void generateTSVAddResults(String setIri, String name, Set<String> im1Ids, StringJoiner results, Concept member) {
    if (member.getIm1Id() != null) {
      for (String im1Id : member.getIm1Id()) {
        if (!im1Ids.contains(im1Id)) {
          results.add(
            new StringJoiner("\t")
              .add(setIri)
              .add(name)
              .add(im1Id)
              .toString());
          im1Ids.add(im1Id);
        }
      }
    }
  }

  private void pushToS3(StringJoiner results) {
    LOG.trace("Publishing to S3...");
    String bucket = "im-inbound-dev";
    String region = "eu-west-2";
    String accessKey = null;
    String secretKey = null;

    try {
      AWSConfig config = new ConfigManager().getConfig(CONFIG.IM1_PUBLISH, new TypeReference<AWSConfig>() {
      });
      if (config == null) {
        LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
      } else {
        bucket = config.getBucket();
        region = config.getRegion();
        if (null != config.getAccessKey())
          accessKey = config.getAccessKey();
        if (null != config.getSecretKey())
          secretKey = config.getSecretKey();
      }
    } catch (JsonProcessingException e) {
      LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
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
