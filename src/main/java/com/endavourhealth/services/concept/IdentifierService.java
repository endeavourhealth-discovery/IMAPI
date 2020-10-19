package com.endavourhealth.services.concept;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.services.concept.models.Concept;

@Service
public class IdentifierService {
	
	private static final Logger LOG = LoggerFactory.getLogger(IdentifierService.class);
	
	@Autowired
	ConceptRepository conceptRepository;
	
	@Autowired
	ConceptConverter conceptConverter;
	
	/**
	 * Gets the database identifier for the concept entity that matches the given concept
	 * 
	 * @param concept - the query (should not be null) should have a valid IRI
	 * @return Integer - the database identifier of the corresponding concept entity or null if not such entity exists
	 */
	Integer getDbId(Concept concept) {
		return getDbId(concept.getIri());
	}
	
	/**
	 * Gets the database identifier for the concept entity that matches the given concept IRI
	 * 
	 * @param concept - the query (should not be null) 
	 * @return Integer - the database identifier of the corresponding concept entity or null if not such entity exists
	 */
	Integer getDbId(String iri) {
		Integer dbId = null;
		
		com.endavourhealth.dataaccess.entity.Concept entity = conceptRepository.findByIri(iri);
		if(entity != null) {
			dbId = entity.getDbid();
		}
		else {
			LOG.debug(String.format("No concept entity could be found the corresponds to the concept IRI: %s", iri));
		}
		
		return dbId;
	}	
	
	/**
	 * Gets the concept model that matches the given concept database identifier
	 * 
	 * @param dbId - the query (should not be null) should correspond to a concept entity
	 * @return Concept - the concept model corresponding to the concept database identifier or null if no entity with the given database identifier exists
	 */
	Concept getConcept(Integer dbId) {		
		Concept model = null;
		
		com.endavourhealth.dataaccess.entity.Concept entity = conceptRepository.findByDbid(dbId);
		if(entity != null) {
			model = conceptConverter.convert(entity);				
		}
		else {
			LOG.debug(String.format("No concept entity could be found with the db identifier: %d", dbId));
		}
		
		return model;
	}
	
	/**
	 * Retrieve the {@link Concept} with the given IRI. 
	 * <br>
	 * Note 1: The parent and children collections within the returned {@link Concept} will not be initialised. See the various add* methods on {@link ConceptService}
	 * <br>
	 * Note 2: that if no {@link Concept} can be found with the given IRI then this method will return <b>null</b>
	 * 
	 * @param iri - the iri associated with the required concept
	 * @return Concept - the concept model represented by the given iri. Note that this method will return null if the iri cannot be resolved
	 * @see ConceptService#addChildren(Concept)
	 * @see ConceptService#addParents(Concept)
	 */
	Concept getConcept(String iri) {
		Concept model = null;
		
		com.endavourhealth.dataaccess.entity.Concept entity = conceptRepository.findByIri(iri);
		if(entity != null) {
			model = conceptConverter.convert(entity);				
		}
		else {
			LOG.debug(String.format("No concept entity could be found with the IRI identifier: %s", iri));
		}
		
		return model;
	}

	public List<Concept> search(String term, String root) {
		
		List<Concept> concepts = new ArrayList<Concept>();
		List<com.endavourhealth.dataaccess.entity.Concept> entities = conceptRepository.search(term, root);

		entities.forEach(entity -> {
			concepts.add(conceptConverter.convert(entity));
		});

		return concepts;

	}

}
