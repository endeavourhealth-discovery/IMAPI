package com.endavourhealth.services.properties.models;

import static com.endavourhealth.services.concept.ConceptConverter.toConcept;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.concept.models.Relationship;
import com.endavourhealth.services.properties.InheritancePropertyService;

@Service
public class IsAInhertianceProperty implements InheritanceProperty {
	
	@Autowired
	public ConceptPropertyObjectRepository conceptPropertyObjectRepository;	
	
	@Value("${concept.isa.iri:sn:116680003}")
	String isAConceptIri;
	
	private Integer isAConceptDbId;

	
	@Autowired
	InheritancePropertyService inheritancePropertyService;
//	
//	@Autowired
//	Map<String, InheritanceProperty> inheritanceProperties;
	
	@PostConstruct
	public void init() {
		isAConceptDbId = inheritancePropertyService.getPropertyDbId(isAConceptIri);
		if(isAConceptDbId != null) {
			inheritancePropertyService.register(this);
		}
	}

	@Override
	public Set<Relationship> getParents(Concept child) {
		Set<Relationship> relationships = new HashSet<Relationship>();
		
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByConceptDbidAndPropertyDbid(child.getDbId(), isAConceptDbId);
		
		for(ConceptPropertyObject property : allProperties) {
			Relationship relationship = new Relationship(toConcept(property.getObject()), toConcept(property.getProperty()));
			relationships.add(relationship);
		}
		
		return relationships;
	}

	@Override
	public Set<Relationship> getChildren(Concept parent) {
		Set<Relationship> relationships = new HashSet<Relationship>();
		
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByObjectDbidAndPropertyDbid(parent.getDbId(), isAConceptDbId);
		
		for(ConceptPropertyObject property : allProperties) {
			Relationship relationship = new Relationship(toConcept(property.getConcept()), toConcept(property.getProperty()));
			relationships.add(relationship);
		}
		
		return relationships;
	}

	@Override
	public String getInheritancePropertyIri() {
		return isAConceptIri;
	}
	
	
}
