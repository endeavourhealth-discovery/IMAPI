package com.endavourhealth.datamodel;

import java.util.ArrayList;
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
import com.endavourhealth.datamodel.models.DataModel;
import com.endavourhealth.datamodel.models.Property;
import com.endavourhealth.datamodel.models.Value;

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
		return new DataModel(concept, properties);
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
			List<ConceptTct> conceptTcts = conceptTctRepository.findBySource(conceptPropertyObject.getProperty());
			conceptTcts.forEach(conceptTct -> {
				Concept target = conceptRepository.findByDbid(conceptTct.getTarget());
				if (target.getIri().equalsIgnoreCase("owl:topObjectProperty")
						|| target.getIri().equalsIgnoreCase("owl:topDataProperty")) {
					properties.add(generateProperty(conceptPropertyObject));
				}
			});
		});
		return properties;
	}

	private Property generateProperty(ConceptPropertyObject conceptPropertyObject) {
		Concept propertyConcept = conceptRepository.findByDbid(conceptPropertyObject.getProperty());
		Concept propertyObject = conceptRepository.findByDbid(conceptPropertyObject.getObject());
		Value value = new Value(propertyObject);
		Property property = new Property(conceptPropertyObject, propertyConcept, value);
		return property;
	}

}
