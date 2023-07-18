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
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
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
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.zip.DataFormatException;

@Component
public class SetExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SetExporter.class);

    private EntityRepository2 entityRepository2 = new EntityRepository2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
    private SetRepository setRepository= new SetRepository();

    public void publishSetToIM1(String setIri) throws DataFormatException, JsonProcessingException, QueryException {
        StringJoiner results = generateForIm1(setIri);

        pushToS3(results);
        LOG.trace("Done");
    }

    public StringJoiner generateForIm1(String setIri) throws DataFormatException, JsonProcessingException, QueryException {
        LOG.debug("Exporting set to IMv1");

        LOG.trace("Looking up set...");
        String name = entityRepository2.getBundle(setIri, Set.of(RDFS.LABEL.getIri())).getEntity().getName();

        Set<Concept> members = getExpandedSetMembers(setIri, true, true);

        return generateTSV(setIri, name, members);
    }

    private Set<String> getSetsRecursive(String setIri) {
        LOG.trace("Getting set list...");
        Set<String> setIris = new HashSet<>();

        Set<String> subsets = setRepository.getSubsets(setIri);

        if (subsets.isEmpty())
            setIris.add(setIri);
        else {
            for (String subset :subsets) {
                setIris.addAll(getSetsRecursive(subset));
            }
        }
        return setIris;
    }

    public Set<Concept> getExpandedSetMembers(String setIri, boolean includeLegacy, boolean includeSubset) throws JsonProcessingException, QueryException {
        Set<String> setIris = new HashSet<>();
        if(includeSubset) {
            setIris.addAll(getSetsRecursive(setIri));
        } else {
            setIris.add(setIri);
        }


        LOG.trace("Expanding members for sets...");
        Set<Concept> result = new HashSet<>();

        for(String iri : setIris) {
            LOG.trace("Processing set [{}]...", iri);
            String name = entityRepository2.getBundle(iri,Set.of(RDFS.LABEL.getIri())).getEntity().getName();

            Set<Concept> members = setRepository.getSetMembers(iri, includeLegacy);

            if (members != null && !members.isEmpty()) {
                result.addAll(members);
            } else {
                TTEntity entity = entityTripleRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.getIri())).getEntity();
                if (entity.get(IM.DEFINITION)!=null)
                    result.addAll(setRepository.getSetExpansion(entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class),
                        includeLegacy,null));
                else
                  result.addAll(setRepository.getSetExpansion(new Query()
                      .match(f->f
                        .setIri(entity.getIri())
                        .setDescendantsOrSelfOf(true))
                    ,includeLegacy,null));
            }
            result.forEach(m -> m.addIsContainedIn(new TTIriRef(iri,name)));
        }

        return result;
    }

    private StringJoiner generateTSV(String setIri, String name, Set<Concept> members) {
        LOG.trace("Generating output...");

        Set<String> im1Ids = new HashSet<>();

        StringJoiner results = new StringJoiner(System.lineSeparator());
        results.add("vsId\tvsName\tmemberDbid");

        for(Concept member : members) {
            generateTSVAddResults(setIri, name, im1Ids, results, member);
            if (member.getMatchedFrom() != null){
                for (Concept legacy:member.getMatchedFrom()) {
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
            JsonNode config = new ConfigManager().getConfig(CONFIG.IM1_PUBLISH.getIri());
            if (config == null) {
                LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
            } else {
                bucket = config.get("bucket").asText();
                region = config.get("region").asText();
                if (config.has("accessKey"))
                    accessKey = config.get("accessKey").asText();
                if (config.has("secretKey"))
                    secretKey = config.get("secretKey").asText();
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
