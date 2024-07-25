package org.endeavourhealth.imapi.logic.exporters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.AWSConfig;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Node;
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

  private EntityRepository2 entityRepository2 = new EntityRepository2();
  private SetRepository setRepository = new SetRepository();
  private EntityTripleRepository trplRepository = new EntityTripleRepository();

  public void publishSetToIM1(String setIri) throws JsonProcessingException, QueryException {
    StringJoiner results = generateForIm1(setIri);

    pushToS3(results);
    LOG.trace("Done");
  }

  public StringJoiner generateForIm1(String setIri) throws QueryException, JsonProcessingException {
    LOG.debug("Exporting set to IMv1");

    LOG.trace("Looking up set...");
    String name = entityRepository2.getBundle(setIri, Set.of(RDFS.LABEL)).getEntity().getName();

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
      LOG.trace("Expanding subsets for {}...", iri);
      Set<TTIriRef> subSetIris = getSubsetIrisWithNames(iri);
      LOG.trace("Found {} subsets...", subSetIris.size());
      for (TTIriRef subset : subSetIris) {
        Set<Concept> subsetMembers = getExpandedSetMembers(subset.getIri(), core, legacy, subsets, schemes);
        if (null != subsetMembers && !subsetMembers.isEmpty()) {
          subsetMembers.forEach(ss -> ss.addIsContainedIn(
            new TTEntity(subset.getIri())
              .setName(subset.getName())
          ));
          result.addAll(subsetMembers);
        }
      }
    }

    return result;
  }

  private Set<Concept> tryGetExpandedSetMembersByDefinition(String iri, boolean legacy, List<String> schemeIris) throws JsonProcessingException, QueryException {

    TTEntity entity = trplRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION, RDFS.LABEL)).getEntity();
    if (null == entity)
      return null;

    String name = entity.has(iri(RDFS.LABEL)) ? entity.getName() : "";

    Query definition = entity.has(iri(IM.DEFINITION)) ? entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class) : null;
    if (null == definition)
      return null;

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

    AmazonS3ClientBuilder s3Builder = AmazonS3ClientBuilder
      .standard()
      .withRegion(region);

    if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty())
      s3Builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));

    final AmazonS3 s3 = s3Builder.build();
    try {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
      String filename = date.format(timestamp.getTime()) + "_valueset.tsv";

      byte[] byteData = results.toString().getBytes();
      InputStream stream = new ByteArrayInputStream(byteData);

      ObjectMetadata meta = new ObjectMetadata();
      meta.setContentLength(byteData.length);

      PutObjectRequest por = new PutObjectRequest(bucket, filename, stream, meta)
        .withCannedAcl(CannedAccessControlList.BucketOwnerFullControl);

      s3.putObject(por);
    } catch (AmazonServiceException e) {
      LOG.error(e.getErrorMessage());
    }
  }
}
