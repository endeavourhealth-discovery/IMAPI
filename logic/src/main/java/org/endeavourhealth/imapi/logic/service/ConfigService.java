package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.entity.Config;
import org.endeavourhealth.imapi.dataaccess.repository.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.repository.EntityTctRepository;
import org.endeavourhealth.imapi.dataaccess.repository.ConfigRepository;
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

    public <T> T getConfig(String name, Class<T> resultType) throws JsonProcessingException, SQLException {

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getData(), resultType);
    }

    public <T> T getConfig(String name, TypeReference<T> resultType) throws JsonProcessingException, SQLException {

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getData(), resultType);
    }
}
