package com.endavourhealth.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.Concept;
import com.endavourhealth.concept.models.ConceptTreeNode;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;

@Service
class ParentService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ParentService.class);

	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;
	
	@Autowired
	IdentifierService identifierService;
	
	@Value("${concept.isa.iri:sn:116680003}")
	String isAConceptIri;
	
	Integer isAConceptDbId;
	
	@PostConstruct
	void init() {
		isAConceptDbId = identifierService.getDbId(isAConceptIri);
	}

	/**
	 * Add the the full parent inheritance hierarchy to the given concept. 
	 * <br>
	 * Note: it is assumed that the conceptDbId resolves to an actual concept entity. If not this method will not add any parents. To the
	 * caller it will appear as though the concept does not have any parents. This may not in fact be the case if the coneptDbId cannot
	 * be resolved. It is the caller's responsibility to ensure that the conceptDbId exists and corresponds to the given concept
	 * 
	 * @param concept - the concept to add parents to (must not be null)
	 * @param conceptDbId - the database identifier of the concept (see note above)
	 */
	void addParents(Concept concept, Integer conceptDbId) {
		
		if(conceptDbId != null) {
			
			if(concept != null) {
				ConceptTreeNode conceptTreeNode = new ConceptTreeNode(concept);
				addParents(conceptTreeNode, conceptDbId);
				concept.setTree(conceptTreeNode);
			}
			else {
				LOG.debug("Unable to addParents to concept as the given concept was null");
			}
		}
		else {
			LOG.debug(String.format("Unable to addParents to concept: %s as the given conceptDbId was null", concept));
		}
	}
	
	private ConceptTreeNode addParents(ConceptTreeNode conceptTreeNode, Integer conceptDbId) {
		List<ConceptPropertyObject> parents = getParents(conceptDbId);
		for(ConceptPropertyObject parent : parents) {
			Integer parentDbId = parent.getObject();
			Concept parentConcept = identifierService.getConcept(parentDbId);
			
			if(parentConcept != null) {
				ConceptTreeNode parentTreeNode = new ConceptTreeNode(parentConcept, conceptTreeNode);
				
				addParents(parentTreeNode, parentDbId);
			}
			else {
				// TODO - exception. There is something wrong with the DB as a CPO object points to a non-existant concept
			}
		}
		
		return conceptTreeNode;
	}

	// filter the CPO entities only retaining those whose property is an instance of the "Is A" concept
	private List<ConceptPropertyObject> getParents(Integer conceptDbId) {
		List<ConceptPropertyObject> parentProperties = new ArrayList<ConceptPropertyObject>();
			
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByConcept(conceptDbId);
		if(allProperties != null && allProperties.isEmpty() == false) {
			parentProperties = allProperties.stream()
											.filter(cpo -> cpo.getProperty().equals(isAConceptDbId))
											.collect(Collectors.toList());
		}
		else {
			LOG.debug(String.format("No ConceptPropertyObject instances could be found with the concept field value: %d", conceptDbId));
		}
		
		return parentProperties;
	}
	
}
