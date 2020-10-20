package com.endavourhealth.concept;

import com.endavourhealth.dataaccess.entity.Concept;

public interface FromConceptConverter<D> {
	
	public D convert(Concept concept);
}
