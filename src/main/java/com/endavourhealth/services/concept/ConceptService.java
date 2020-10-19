package com.endavourhealth.services.concept;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.services.concept.models.Concept;

@Service
public class ConceptService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConceptService.class);
	
	@Autowired
	ConceptConverter conceptConverter;
	
	@Autowired
	IdentifierService identifierService;
	
	@Autowired 
	ParentService parentService;
	
	@Autowired
	ChildService childService;
	
	@Autowired
	ConceptRepository conceptRepository;
	
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
	public Concept getConcept(String iri) {
		return identifierService.getConcept(iri);
	}

	/**
	 * Add the the full parent inheritance hierarchy to the given concept. 
	 * <br>
	 * Note: it is assumed that the concept exists within the database. If not then this method will not add any parents. To the
	 * caller it will appear as though the concept does not have any parents. This may not in fact be the case if the concept cannot
	 * be resolved. It is the caller's responsibility to ensure that the concept exists within the database
	 * 
	 * @param concept - the concept to add parents to (must not be null)
	 */
	public void addParents(Concept concept) {
		Integer conceptDbId = identifierService.getDbId(concept);
		
		if(conceptDbId != null) {
			parentService.addParents(concept, conceptDbId);
		}
		else {
			LOG.debug(String.format("No entity could be found the corresponds to the concept: %s", concept));
		}
	}

	/**
	 * Add the the direct children to the given concept ie those that inherit directly from the concept. 
	 * <br>
	 * Note: it is assumed that the concept exists within the database. If not then this method will not add any children. To the
	 * caller it will appear as though the concept does not have any children. This may not in fact be the case if the concept cannot
	 * be resolved. It is the caller's responsibility to ensure that the concept exists within the database
	 * 
	 * @param concept - the concept to add children to (must not be null)
	 */
	public void addChildren(Concept concept) {
		Integer conceptDbId = identifierService.getDbId(concept);
		
		if(conceptDbId != null) {
			childService.addDirectChildren(concept, conceptDbId);
		}
		else {
			LOG.debug(String.format("No entity could be found the corresponds to the concept: %s", concept));
		}
	}

	public List<Concept> search(String term) {
		return identifierService.search(term, ":DiscoveryCommonDataModel");
	}


}
