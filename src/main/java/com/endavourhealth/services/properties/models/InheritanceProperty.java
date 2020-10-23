package com.endavourhealth.services.properties.models;

import java.util.Set;

import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.concept.models.Relationship;

public interface InheritanceProperty {

	public Set<Relationship> getParents(Concept concept);
	
	public Set<Relationship> getChildren(Concept concept);
	
	public String getInheritancePropertyIri();

}
