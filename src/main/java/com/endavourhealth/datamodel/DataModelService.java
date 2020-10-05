package com.endavourhealth.datamodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.repository.ConceptPropertyDataRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.datamodel.models.DataModel;

@Component
public class DataModelService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptPropertyDataRepository conceptPropertyDataRepository;

	public DataModel getDataModel(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		return generateDataModel(concept);
	}

	private DataModel generateDataModel(Concept concept) {
		DataModel dataModel = new DataModel();
		dataModel.setName(concept.getName());
		dataModel.setIri(concept.getIri());
		dataModel.setDescription(concept.getDescription());
		
		return dataModel;
	}

}
