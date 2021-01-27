package org.endeavourhealth.dataaccess;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("ConfigService")
public class ConfigService implements IConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    ConceptRepository conceptRepository;

    @Override
    public List<ConceptReference> getQuickAccess() {
        LOG.info("getQuickAccess");

        String[] quickAccessIris = new String[] {":SemanticConcept", ":DiscoveryCommonDataModel", ":HealthRecord", ":VSET_DataModel", ":VSET_QueryValueSets"};

        return Arrays.stream(quickAccessIris)
            .map(iri -> {
                Concept c = conceptRepository.findByIri(iri);
                return new ConceptReference(c.getIri(), c.getName());
            })
            .collect(Collectors.toList());
    }
}
