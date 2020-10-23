package com.endavourhealth.services.properties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.concept.models.Parent;
import com.endavourhealth.services.concept.models.Relationship;
import com.endavourhealth.services.properties.models.InheritanceProperty;

@Service
public class InheritancePropertyService {

	@Autowired
	private ConceptRepository conceptRepository;
	
	@Autowired
	ParentBuilder parentBuilder;
	
	private Map<String, InheritanceProperty> inheritanceProperties;
	
	@PostConstruct
	public void init() {
		inheritanceProperties = new HashMap<String, InheritanceProperty>();
	}

	public Set<Parent> getParents(Concept concept, Set<String> inheritiancePropertyIris, String rootParentIri) {
		Set<Parent> parents = new HashSet<Parent>();
		
		for(String inheritiancePropertyIri : inheritiancePropertyIris) {
			InheritanceProperty inheritanceProperty = inheritanceProperties.get(inheritiancePropertyIri);
			
			if(inheritanceProperty != null) {
				parents.addAll(parentBuilder.getParents(concept, inheritanceProperty, rootParentIri));
			}			
		}
		
		return parents;
	}

	public Set<Relationship> getChildren(Concept concept, Set<String> inheritiancePropertyIris) {
		Set<Relationship> children = new HashSet<Relationship>();
		
		for(String inheritiancePropertyIri : inheritiancePropertyIris) {
			InheritanceProperty inheritanceProperty = inheritanceProperties.get(inheritiancePropertyIri);
			
			if(inheritanceProperty != null) {
				children.addAll(inheritanceProperty.getChildren(concept));
			}			
		}
		
		return children;
	}

	public Integer getPropertyDbId(String propertyIri) {
		Integer propertyDbId = null;
		
		com.endavourhealth.dataaccess.entity.Concept property = conceptRepository.findByIri(propertyIri);

		if (property != null) {
			propertyDbId = property.getDbid();
		}
		
		return propertyDbId;
	}

	// return true if the inheritance property was not previously registered
	public boolean register(InheritanceProperty inhertianceProperty) {
		return inheritanceProperties.put(inhertianceProperty.getInheritancePropertyIri(), inhertianceProperty) == null;
	}
}
