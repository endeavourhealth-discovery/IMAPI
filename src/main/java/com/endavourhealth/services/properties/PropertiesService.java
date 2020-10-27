package com.endavourhealth.services.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptPropertyDataRepository;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.dataaccess.util.ConceptTctUtil;
import com.endavourhealth.services.properties.models.Properties;
import com.endavourhealth.services.properties.models.Property;
import com.endavourhealth.services.properties.models.Value;

@Component
public class PropertiesService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;

	@Autowired
	ConceptPropertyDataRepository conceptPropertyDataRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;
	
	@Autowired
	ConceptTctUtil conceptTctService;

	public Properties getProperties(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		Properties properties = new Properties();
		properties.setCoreProperties(getCoreProperties(concept.getDbid()));
		properties.setInheritedProperties(getInheritedProperties(concept.getDbid()));
		return properties;
	}

	public List<Property> getCoreProperties(Integer Dbid) {
		List<Property> properties = new ArrayList<Property>();
		// find concept property objects
		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConceptDbid(Dbid);
		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
			List<String> types = Arrays.asList("owl:topObjectProperty", "owl:topDataProperty");
			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject, types)) {
				Value value = new Value(conceptPropertyObject.getObject(), conceptPropertyObject.getConcept().getIri());
				properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
			}
		});
		return properties;
	}
	
	public List<Property> getInheritedProperties(Integer Dbid) {
		List<Property> properties = new ArrayList<Property>();
		// find concept property objects
		
		List<ConceptPropertyObject> conceptPropertyObjects = new ArrayList<ConceptPropertyObject>();
		List<ConceptTct> conceptTcts = conceptTctRepository.findBySourceDbid(Dbid);
		conceptTcts.forEach(conceptTct -> {
			conceptPropertyObjects.addAll(conceptPropertyObjectRepository.findByConceptDbid(conceptTct.getTarget().getDbid()));
		});
		
		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
			List<String> types = Arrays.asList("owl:topObjectProperty", "owl:topDataProperty");
			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject, types) && conceptPropertyObject.getConcept().getDbid() != Dbid) {
				Value value = new Value(conceptPropertyObject.getObject(), conceptPropertyObject.getConcept().getIri());
				properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
			}
		});
		return properties;
	}

	public List<ConceptPropertyObject> getConceptPropertyObjects(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		
		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConceptDbid(concept.getDbid());
		
		return conceptPropertyObjects;
	}

}
