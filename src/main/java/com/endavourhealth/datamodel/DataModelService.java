package com.endavourhealth.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptPropertyDataRepository;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.datamodel.models.DataModel;
import com.endavourhealth.datamodel.models.Property;

@Component
public class DataModelService {

	@Autowired
	ConceptRepository conceptRepository;
	
	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;

	@Autowired
	ConceptPropertyDataRepository conceptPropertyDataRepository;
	
	@Autowired 
	ConceptTctRepository conceptTctRepository;
	
	//@Autowired
	//ConceptBuilder conceptService;

	public DataModel getDataModel(String iri) {
		com.endavourhealth.concept.models.Concept dmConcept = null; //lookup from config
		
		//com.endavourhealth.concept.models.Concept c = conceptService.getConceptOfType(iri, dmConcept);
		
		// can ask if concept is a type of datamodel and if not error
		
		// will translate according to valueset or datamodel or prmative into types
		
		// can also ask for properties by value type to say get me all datamodel properties
		
		
		Concept concept = conceptRepository.findByIri(iri);
		
		
		List<Property> properties = new ArrayList<Property>();
		
		// find concept properties
		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConcept(concept.getDbid());
		// get concepts for those properties
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			Property property = new Property();
			Concept propertyConcept = conceptRepository.findByDbid(conceptPropertyObject.getProperty());
			property.setIri(propertyConcept.getIri());
			property.setName(propertyConcept.getName());
			property.setDescription(propertyConcept.getDescription());
			property.setMinCardinality(conceptPropertyObject.getMinCardinality());
			property.setMaxCardinality(conceptPropertyObject.getMaxCardinality());
			properties.add(property);
		});
		
		return generateDataModel(concept, properties);
	}

	private DataModel generateDataModel(Concept concept, List<Property> properties) {
		DataModel dataModel = new DataModel();
		dataModel.setName(concept.getName());
		dataModel.setIri(concept.getIri());
		dataModel.setDescription(concept.getDescription());
		
		properties.forEach(property -> {
			dataModel.addProperty(property);
		});
		
		return dataModel;
	}

}
