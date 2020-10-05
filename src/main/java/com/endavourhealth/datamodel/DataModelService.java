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

	public DataModel getDataModel(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		List<Property> properties = getProperties(concept.getDbid());
		return generateDataModel(concept, properties);
	}

	public List<Property> getDataModelProperties(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		return getProperties(concept.getDbid());
	}

	public List<Property> getProperties(Integer Dbid) {
		List<Property> properties = new ArrayList<Property>();

		// find concept property objects
		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConcept(Dbid);
		// get concepts for each of those objects
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
		return properties;
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
