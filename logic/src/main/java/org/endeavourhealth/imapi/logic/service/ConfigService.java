package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.ConfigRepositoryImpl;
import org.endeavourhealth.imapi.model.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("ConfigService")
public class ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    private final ObjectMapper om = new ObjectMapper();
    private final ConfigRepositoryImpl configRepository = new ConfigRepositoryImpl();

    public <T> T getConfig(String name, Class<T> resultType) throws JsonProcessingException {
        LOG.debug("getConfig<Class>");

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getData(), resultType);
    }

    public <T> T getConfig(String name, TypeReference<T> resultType) throws JsonProcessingException {
        LOG.debug("getConfig<TypeReference>");

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getData(), resultType);
    }
}
