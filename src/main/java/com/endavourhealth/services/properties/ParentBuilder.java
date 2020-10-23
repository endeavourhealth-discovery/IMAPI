package com.endavourhealth.services.properties;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.concept.models.Parent;
import com.endavourhealth.services.concept.models.Relationship;
import com.endavourhealth.services.properties.models.InheritanceProperty;	

@Service
class ParentBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(ParentBuilder.class);

	Set<Parent> getParents(Concept child, InheritanceProperty inheritanceProperty, String rootParentIri) {
		Set<Parent> parents = new HashSet<Parent>();
		
		Set<Relationship> parentRelationships = inheritanceProperty.getParents(child);
		
		// get the direct parents (and also their ancestors)
		for(Relationship parentRelationship : parentRelationships) {
			Parent parent = new Parent(parentRelationship);	
			
			// keep unwinding 'til the root is hit
			if(parentRelationship.getRealtion().getIri().equals(rootParentIri) == false) {
				addParents(parent, inheritanceProperty, rootParentIri);		
			}
			
			parents.add(parent);
		}	
		
		return parents;
	}
	
	private void addParents(Parent child, InheritanceProperty inheritanceProperty, String rootIri) {
		Set<Relationship> parentRelationships = inheritanceProperty.getParents(child.getRelationship().getRealtion());
		
		// get the parent's parent and so on
		for(Relationship parentRelationship : parentRelationships) {
			Parent parent = new Parent(parentRelationship);
			child.setParent(parent);
			
			// keep unwinding 'til the root is hit
			if(parentRelationship.getRealtion().getIri().equals(rootIri) == false) {	
				addParents(parent, inheritanceProperty, rootIri);			
			}
		}	
	}
}
