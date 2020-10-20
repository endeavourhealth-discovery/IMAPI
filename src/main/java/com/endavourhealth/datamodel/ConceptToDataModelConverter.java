package com.endavourhealth.datamodel;

import org.springframework.stereotype.Service;

import com.endavourhealth.concept.FromConceptConverter;
import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.datamodel.models.DataModelDetail;

@Service
class ConceptToDataModelConverter implements FromConceptConverter<DataModelDetail> {

	@Override
	public DataModelDetail convert(Concept concept) {
		DataModelDetail dataModel = new DataModelDetail();
		dataModel.setIri(concept.getIri());
		dataModel.setName(concept.getName());
		dataModel.setDescription(concept.getDescription());
		
		return dataModel;
	}

}
