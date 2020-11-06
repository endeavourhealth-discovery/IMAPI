package com.endavourhealth.dataaccess;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.models.objectModel.Concept;
import com.endavourhealth.models.objectModel.ConceptReference;
import com.endavourhealth.models.objectModel.ConceptReferenceNode;
import com.endavourhealth.models.objectModel.ConceptStatus;

@Component
public class ConceptService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConceptService.class);
	
	@Autowired
	ConceptRepository conceptRepository;
	
	public Set<ConceptReference> findByNameLike(String term, String rootIri) {
		Set<ConceptReference> result = null;
		
		if(term != null && isValid(rootIri)) {
			result = new HashSet<>();
			for(com.endavourhealth.dataaccess.entity.Concept entity : conceptRepository.search(term, rootIri)) {
				result.add(toConceptReferenceModel(entity));
			}
		}
		else {
			LOG.warn("Unable to complete search - either term is empty or iri is invalid");
		}
		
		return result;
	}

	public Concept getConcept(String iri) {
		Concept concept = null;
		
		if(isValid(iri)) {
			concept = toConceptModel(conceptRepository.findByIri(iri));
		}
		else {
			LOG.warn("Unable to retreive Concept - iri is invalid");
		}
		
		return  concept;		
	}
	
	public ConceptReference getConceptReference(String iri) {
		ConceptReference conceptReference = null;
		
		if(isValid(iri)) {
			return toConceptReferenceModel(conceptRepository.findByIri(iri));
		}
		else {
			LOG.warn("Unable to retreive ConceptReference - iri is invalid");
		}
		
		return  conceptReference;	
	}
	
	public Set<ConceptReference> getImmediateChildren(String iri) {
		Set<ConceptReference> immediateChildren = null;
		
		if(isValid(iri)) {
			// Will Weatherill 6 Nov - a hack until things are plumbed together with the database
			immediateChildren = new HashSet<>();
		}
		else {
			LOG.warn("Unable to retreive immediate child Concepts - iri is invalid");
		}
		
		return immediateChildren;
	}
	
	public Set<ConceptReferenceNode> getParentHierarchy(String iri) {
		Set<ConceptReferenceNode> parentHierarchy = null;
		
		if(isValid(iri)) {
			// Will Weatherill 6 Nov - a hack until things are plumbed together with the database
			parentHierarchy = new HashSet<>();
		}
		else {
			LOG.warn("Unable to retreive parent hierarchy for Concept - iri is invalid");
		}
		
		return parentHierarchy;
	}

	// return a ConceptReference to the newly created Concept where
	// ConceptReference.id is populated with the dbid of the stored 
	// Concept
	public ConceptReference create(Concept newConcept) {
		ConceptReference conceptReference = null;
		
		if(newConcept != null) {
			// Will Weatherill 6 Nov - a hack until things are plumbed together with the database
			conceptReference = new ConceptReference(new Random().nextLong());
			conceptReference.setIri(newConcept.getIri());
			conceptReference.setName(newConcept.getName());
		}
		else {
			LOG.warn("Unable to save Concept - Concept was null");			
		}
		
		return conceptReference;
	}
	
	private boolean isValid(String iri) {
		boolean isValid = false;
		
		isValid =  (iri != null && iri.isEmpty() == false);
		if(isValid == false) {
			LOG.warn(String.format("invalid IRI (iri:%s) - iri is either null or empty", iri));
		}
		
		return isValid;
	} 
	
	//-------------------------------------------------------------------------------------- 
	//--- Will Weatherill 6 Nov - temp methods to get the dataaccess layer working until --- 
	//--- the database is connected                                                      ---
	//--------------------------------------------------------------------------------------
	private Concept toConceptModel(com.endavourhealth.dataaccess.entity.Concept entity) {
		Concept model = null;
		
		if(entity != null) {
			model = new Concept(entity.getIri(), entity.getName());
			model.setCode(entity.getCode());
			model.setDescription(entity.getDescription());
			model.setScheme(entity.getScheme().toString());
			model.setStatus(ConceptStatus.byName(entity.getStatus().getName()));
		}
		
		return model;
	}
	
	private ConceptReference toConceptReferenceModel(com.endavourhealth.dataaccess.entity.Concept entity) {
		ConceptReference model = null;
		
		if(entity != null) {
			model = new ConceptReference(entity.getDbid());
			model.setIri(entity.getIri());
			model.setName(entity.getName());
		}
		
		return model;
	}
}
