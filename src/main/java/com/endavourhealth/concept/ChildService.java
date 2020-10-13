package com.endavourhealth.concept;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.Concept;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;

@Service
class ChildService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChildService.class);
	
	@Autowired
	ConceptTctRepository conceptTctRepository;
	
	@Autowired
	ConceptConverter conceptConverter;
	
	/**
	 * Add the the direct children to the given concept ie those that inherit directly from the concept. 
	 * <br>
	 * Note 1: it is assumed that the conceptDbId resolves to an actual concept entity. If not then this method will not add any children. To the
	 * caller it will appear as though the concept does not have any children. This may not in fact be the case if the coneptDbId cannot
	 * be resolved. It is the caller's responsibility to ensure that the conceptDbId exists and corresponds to the given concept
	 * <br>
	 * Note 2: it is assumed that the conceptDbId resolves to the given concept param. This method makes not attempt to check this relationship.
	 * It is the caller's responsibility to ensure that the conceptDbId exists and corresponds to the given concept
	 * 
	 * @param concept - the concept to add children to (must not be null)
	 * @param conceptDbId - the database identifier of the concept (see note above)
	 */
	void addDirectChildren(Concept concept, Integer conceptDbId) {
		
		if(conceptDbId != null) {
			
			if(concept != null) {
				if(concept.hasChildren()) {
					LOG.info(String.format("The concept: %s already has children. Add children may overwrite exisiting children", concept));
				}
				
				addDirectChildren(concept, getDirectChildren(conceptDbId));
			}
			else {
				LOG.debug("Unable to add children to concept as the given concept was null");
			}
		}
		else {
			LOG.debug(String.format("Unable to add children to concept: %s as the given conceptDbId was null", concept));
		}
	}
	
	private void addDirectChildren(Concept parentConcept, List<ConceptTct> childTcts) {
		for(ConceptTct childTct : childTcts) {
			Concept childConcept = conceptConverter.convert(childTct.getSource());
			
			if(childConcept != null) {
				parentConcept.addChild(childConcept);
			}
			else {
				// TODO - exception. There is something wrong with the DB as a CPO object points to a non-existant concept
			}			
		}
	}
	
	private List<ConceptTct> getDirectChildren(Integer conceptDbId) {		
		List<ConceptTct> directChildren = conceptTctRepository.findByTargetDbidAndLevel(conceptDbId, ConceptTct.DIRECT_RELATION_LEVEL);
		
		return directChildren;
	}
}
