package org.endeavourhealth.dataaccess;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTctRepository;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("ConfigService")
public class ConfigService implements IConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ConceptTctRepository conceptTctRepository;

    @Override
    public List<ConceptSummary> getQuickAccess() {
        LOG.info("getQuickAccess");

        String[] quickAccessIris = new String[] {":SemanticConcept", ":HealthRecord", ":VSET_DataModel", ":VSET_QueryValueSets"};
        String[] candidates = new String[] {":DiscoveryCommonDataModel", ":VSET_ValueSet", ":SemanticConcept"};

        List<ConceptSummary> result = new ArrayList<>();

        for(String iri: quickAccessIris) {
            Concept c = conceptRepository.findByIri(iri);

            if(c != null) {
	            ConceptSummary src = new ConceptSummary()
	                .setIri(c.getIri())
	                .setName(c.getName())
	                .setCode(c.getCode());
	
	            if (c.getScheme() != null)
	                src.setScheme(new ConceptReference(
	                    c.getScheme().getIri(),
	                    c.getScheme().getName())
	                );
	
	            src.setIsDescendentOf(conceptTctRepository.findBySource_Iri_AndTarget_IriIn(iri, Arrays.asList(candidates))
	                .stream().map(tct -> new ConceptReference(tct.getTarget().getIri(), tct.getTarget().getName()))
	                .sorted(Comparator.comparing(ConceptReference::getName))
	                .collect(Collectors.toList()));
	
	            result.add(src);
            }
            else {
            	LOG.debug(String.format("Warning - unable to find concept with the IRI %s", iri));
            }
        }

        return result;
    }
}
