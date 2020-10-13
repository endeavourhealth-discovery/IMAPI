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
import com.endavourhealth.dataaccess.service.ConceptPropertyObjectService;
import com.endavourhealth.dataaccess.service.ConceptTctService;
import com.endavourhealth.datamodel.models.DataModel;
import com.endavourhealth.datamodel.models.DataModelDetail;
import com.endavourhealth.datamodel.models.Properties;
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

	@Autowired
	ConceptPropertyObjectService conceptPropertyObjectService;

	@Autowired
	ConceptTctService conceptTctService;

	public List<DataModel> search(String term) {
		List<DataModel> datamodels = new ArrayList<DataModel>();
		List<Concept> concepts = conceptRepository.search(term, ":DiscoveryCommonDataModel");

		concepts.forEach(concept -> {
			datamodels.add(new DataModel(concept.getIri(), concept.getName(), concept.getDescription()));
		});

		return datamodels;

	}

	public DataModelDetail getDataModel(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		
		Properties properties = new Properties();
		properties.setCoreProperties(getCoreProperties(concept.getDbid()));
		
		return new DataModelDetail(concept, properties);
	}

	public Properties getDataModelProperties(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		Properties properties = new Properties();
		properties.setCoreProperties(getCoreProperties(concept.getDbid()));
		return properties;
	}

	public List<Property> getCoreProperties(Integer Dbid) {
		List<Property> properties = new ArrayList<Property>();
		// find concept property objects
		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConceptDbid(Dbid);
		// get concepts for each of those objects
		conceptPropertyObjects.forEach(conceptPropertyObject -> {
			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject)) {
				Value value = new Value(conceptPropertyObject.getObject());
				properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
			}
		});
		return properties;
	}

}
