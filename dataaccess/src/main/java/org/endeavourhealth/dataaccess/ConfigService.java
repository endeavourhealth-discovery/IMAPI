package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTctRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    ConceptRepository conceptRepository;

    ConceptTctRepository conceptTctRepository= new ConceptTctRepository();

    @Autowired
    ConfigRepository configRepository;

    public List<ConceptSummary> getQuickAccess() throws JsonProcessingException, SQLException {
        LOG.info("getQuickAccess");

        List<ConceptSummary> result = new ArrayList<>();

        String[] quickAccessIris = getConfig("quickAccessIri", String[].class);
        String[] candidates =  getConfig("quickAccessCandidatesIri", String[].class);

        if(quickAccessIris == null || candidates==null)
            return result;

        for(String iri: quickAccessIris) {
            Concept c = conceptRepository.findByIri(iri);

            if(c != null) {
	            ConceptSummary src = new ConceptSummary()
	                .setIri(c.getIri())
	                .setName(c.getName())
	                .setCode(c.getCode());
	
	            if (c.getScheme() != null)
	                src.setScheme(new TTIriRef(c.getScheme().getIri(), c.getScheme().getName()));
	
	            src.setIsDescendentOf(conceptTctRepository.findByDescendant_Iri_AndAncestor_IriIn(iri, Arrays.asList(candidates))
                        .stream().sorted(Comparator.comparing(TTIriRef::getName)).collect(Collectors.toList()));
	            result.add(src);
            }
            else {
            	LOG.debug(String.format("Warning - unable to find concept with the IRI %s", iri));
            }
        }

        return result;
    }

    public <T> T getConfig(String name, Class<T> resultType) throws JsonProcessingException {

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getConfig(), resultType);
    }

    public <T> T getConfig(String name, TypeReference<T> resultType) throws JsonProcessingException {

        Config config = configRepository.findByName(name);
        if(config==null)
            return null;
        return om.readValue(config.getConfig(), resultType);
    }
}
