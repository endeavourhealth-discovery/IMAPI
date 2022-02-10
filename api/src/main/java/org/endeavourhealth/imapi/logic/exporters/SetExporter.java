package org.endeavourhealth.imapi.logic.exporters;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Component
public class SetExporter {
    private static final Logger LOG = LoggerFactory.getLogger(SetExporter.class);

    private EntityRepository2 entityRepository2 = new EntityRepository2();
    private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();

    public void publishSetToIM1(String setIri){
        String name = entityRepository2.getBundle(setIri, Set.of(RDFS.LABEL.getIri())).getEntity().getName();

        Set<String> setIris = getSets(setIri);

        Set<CoreLegacyCode> members = getMembers(setIris);

        StringJoiner results = generateTSV(setIri, name, members);

        pushToS3(results);
    }

    private void pushToS3(StringJoiner results) {
        String bucket = "imv1sender";
        String region = "eu-west-2";

        try {
            JsonNode config = new ConfigManager().getConfig(CONFIG.IM1_PUBLISH.getIri());
            if (config == null) {
                LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
            } else {
                bucket = config.get("bucket").asText();
                region = config.get("region").asText();
            }
        } catch (JsonProcessingException e) {
            LOG.debug("No IM1_PUBLISH config found, reverting to defaults");
        }

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        try {
            s3.putObject(bucket, "valueset.tsv", results.toString());
        } catch (AmazonServiceException e) {
            LOG.error(e.getErrorMessage());
        }
    }

    private StringJoiner generateTSV(String setIri, String name, Set<CoreLegacyCode> members) {
        StringJoiner results = new StringJoiner(System.lineSeparator());
        for(CoreLegacyCode member : members) {
            results.add(
                new StringJoiner("\t")
                    .add(setIri)
                    .add(name)
                    .add(member.getCode())
                    .add(member.getScheme().getIri())
                    .toString()
            );

            if (member.getLegacyCode() != null && !member.getLegacyCode().isEmpty()) {
                results.add(
                    new StringJoiner("\t")
                        .add(setIri)
                        .add(name)
                        .add(member.getLegacyCode())
                        .add(member.getLegacyScheme().getIri())
                        .toString()
                );
            }
        }
        return results;
    }

    private Set<CoreLegacyCode> getMembers(Set<String> setIris) {
        Set<CoreLegacyCode> members = new HashSet<>();

        for(String iri : setIris){
            TTArray definition = entityTripleRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.getIri()), 0).getEntity().get(IM.DEFINITION);
            members.addAll(entityRepository2.getSetExpansion(definition, false));
            members.addAll(entityRepository2.getSetExpansion(definition, true));
        }
        return members;
    }

    private Set<String> getSets(String setIri) {
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
}
