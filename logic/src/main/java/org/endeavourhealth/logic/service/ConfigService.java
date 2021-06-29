package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.repository.EntityRepository;
import org.endeavourhealth.dataaccess.repository.EntityTctRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("ConfigService")
public class ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    private final ObjectMapper om = new ObjectMapper();

    EntityRepository entityRepository = new EntityRepository();

    EntityTctRepository entityTctRepository= new EntityTctRepository();

    ConfigRepository configRepository = new ConfigRepository();

    public List<EntitySummary> getQuickAccess() throws JsonProcessingException, SQLException {
        LOG.info("getQuickAccess");

        List<EntitySummary> result = new ArrayList<>();

        String[] quickAccessIris = getConfig("quickAccessIri", String[].class);
        String[] candidates =  getConfig("quickAccessCandidatesIri", String[].class);

        if(quickAccessIris == null || candidates==null)
            return result;

        for(String iri: quickAccessIris) {
            EntitySummary src = entityRepository.getEntitySummaryByIri(iri);
            if(src!=null) {
                src.setIsDescendentOf(entityTctRepository.findByDescendant_Iri_AndAncestor_IriIn(iri, Arrays.asList(candidates))
                        .stream().sorted(Comparator.nullsLast(Comparator.comparing(TTIriRef::getName))).collect(Collectors.toList()));
                result.add(src);
            }
        }

        return result;
    }

    public <T> T getConfig(String name, Class<T> resultType) throws JsonProcessingException, SQLException {

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getConfig(), resultType);
    }

    public <T> T getConfig(String name, TypeReference<T> resultType) throws JsonProcessingException, SQLException {

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getConfig(), resultType);
    }
}
