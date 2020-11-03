package com.endavourhealth.services.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.DataAccessService;
import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.services.properties.models.Properties;
import com.endavourhealth.services.properties.models.Property;
import com.endavourhealth.services.properties.models.Value;

@Component
public class PropertiesService {

	@Autowired
	DataAccessService dataAccessService;

	public Properties getProperties(String iri) {
		Concept concept = dataAccessService.findByIri(iri);
		Properties properties = new Properties();
		properties.setCoreProperties(getCoreProperties(concept.getDbid()));
		properties.setInheritedProperties(getInheritedProperties(concept.getDbid()));
		return properties;
	}

	public List<Property> getCoreProperties(Integer Dbid) {
		List<Property> properties = new ArrayList<Property>();
		// find concept property objects
		List<ConceptPropertyObject> conceptPropertyObjects = dataAccessService.getCoreProperties(Dbid, null);
		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			Value value = new Value(conceptPropertyObject.getObject(), conceptPropertyObject.getConcept().getIri());
			properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
		});
		return properties;
	}
	
	public List<Property> getInheritedProperties(Integer Dbid) {
		List<Property> properties = new ArrayList<Property>();
		// find concept property objects
		
		List<ConceptPropertyObject> conceptPropertyObjects = dataAccessService.getInheritedProperties(Dbid, null);
		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
				Value value = new Value(conceptPropertyObject.getObject(), conceptPropertyObject.getConcept().getIri());
				properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
		});
		return properties;
	}

	public List<ConceptPropertyObject> getConceptPropertyObjects(String iri) {
		Concept concept = dataAccessService.findByIri(iri);
		List<ConceptPropertyObject> conceptPropertyObjects = dataAccessService.getCoreProperties(concept.getDbid(), null);
		return conceptPropertyObjects;
	}

}
