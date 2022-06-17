package org.endeavourhealth.imapi.logic.exporters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SetExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SetExporter.class);

    private EntityRepository2 entityRepository2 = new EntityRepository2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    public void publishSetToIM1(String setIri){
        StringJoiner results = generateForIm1(setIri);

        pushToS3(results);
        LOG.trace("Done");
    }

    public StringJoiner generateForIm1(String setIri) {
        LOG.debug("Exporting set to IMv1");

        LOG.trace("Looking up set...");
        String name = entityRepository2.getBundle(setIri, Set.of(RDFS.LABEL.getIri())).getEntity().getName();

        Set<String> setIris = getSets(setIri);

        Set<String> members = getMembers(setIris);

        return generateTSV(setIri, name, members);
    }

    private Set<String> getSets(String setIri) {
        LOG.trace("Getting set list...");
        Set<String> setIris = new HashSet<>();
        setIris.add(setIri);

        List<String> subsets = entityRepository2.getMemberIris(setIri);
        for(String member : subsets){
            if(entityRepository2.isSet(member)){
                setIris.add(member);
            }
        }
        return setIris;
    }

    private Set<String> getMembers(Set<String> setIris) {
        LOG.trace("Getting members for sets...");
        Set<String> members = new HashSet<>();

        for(String iri : setIris){
            TTEntity entity = entityTripleRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.getIri(), IM.HAS_MEMBER.getIri()), 0).getEntity();

            // Inject direct members into definition
            if (entity.get(IM.HAS_MEMBER) != null) {
                TTNode orNode = new TTNode();
                entity.addObject(IM.DEFINITION,orNode);
                for (TTValue value:entity.get(IM.HAS_MEMBER).getElements()){
                    orNode.addObject(SHACL.OR,value);
                }
            }

            members.addAll(entityRepository2.getSetDbids(entity.get(IM.DEFINITION)));
        }
        return members;
    }

    private StringJoiner generateTSV(String setIri, String name, Set<String> members) {
        LOG.trace("Generating output...");

        StringJoiner results = new StringJoiner(System.lineSeparator());
        results.add("vsId\tvsName\tmemberDbid");

        for(String member : members) {
                results.add(
                    new StringJoiner("\t")
                        .add(setIri)
                        .add(name)
                        .add(member)
                        .toString()
                );
        }
        return results;
    }

    private void pushToS3(StringJoiner results) {
        LOG.trace("Publishing to S3...");
        String bucket = "im-inbound-dev";
        String region = "eu-west-2";
        String accessKey = "";
        String secretKey = "";

        try {
            JsonNode config = new ConfigManager().getConfig(CONFIG.IM1_PUBLISH.getIri());
            if (config == null) {
                LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
            } else {
                bucket = config.get("bucket").asText();
                region = config.get("region").asText();
                accessKey = config.get("accessKey").asText();
                secretKey = config.get("secretKey").asText();
            }
        } catch (JsonProcessingException e) {
            LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
        }

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        final AmazonS3 s3 = AmazonS3ClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
            String filename = date.format(timestamp.getTime()) + "_valuset.tsv";
            s3.putObject(bucket, filename, results.toString());
        } catch (AmazonServiceException e) {
            LOG.error(e.getErrorMessage());
        }
    }
}
