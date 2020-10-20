package com.endavourhealth.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.TreeNode;
import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;

@Service
class TreeService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TreeService.class);
	
	@Autowired
	ConceptTctRepository conceptTctRepository;
	
	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;
	
	@Autowired
	ConceptRepository conceptRepository;
	
	@Value("${concept.isa.iri:sn:116680003}")
	String isAConceptIri;

	<D> Set<TreeNode<D>> getParents(Concept concept, FromConceptConverter<D> fromConceptConverter) {	
		TreeNodeBuilder<D> treeNodeBuilder = new TreeNodeBuilder<D>(concept, fromConceptConverter);
		addParents(concept, treeNodeBuilder);

		return treeNodeBuilder.getSeedNode().getParents();
	}
		
	<D> Set<TreeNode<D>> getChildren(Concept concept, FromConceptConverter<D> fromConceptConverter) {	
		TreeNodeBuilder<D> treeNodeBuilder = new TreeNodeBuilder<D>(concept, fromConceptConverter);
		addChildren(concept, treeNodeBuilder);

		return treeNodeBuilder.getSeedNode().getChildren();
	}	
	
	private void addChildren(Concept parentConcept, TreeNodeBuilder<?> treeNodeBuilder) {

		List<ConceptTct> childTcts = conceptTctRepository.findByTargetDbidAndLevel(parentConcept.getDbid(), ConceptTct.DIRECT_RELATION_LEVEL);
				
		for(ConceptTct childTct : childTcts) {
			Concept childConcept = childTct.getSource();
			
			treeNodeBuilder.addChildNode(childConcept, parentConcept);		
		}

	}
	
	private <D> void addParents(Concept childConcept, TreeNodeBuilder<D> treeNodeBuilder) {
		List<ConceptPropertyObject> parents = getParentConceptPropertyObjects(childConcept.getDbid());
		for(ConceptPropertyObject parent : parents) {
			Concept parentConcept = parent.getObject();
			
			treeNodeBuilder.addParentNode(childConcept, parentConcept);
			
			// walk up the hierarchy
			addParents(parentConcept, treeNodeBuilder);
		}		
	}	
	
	// filter the CPO entities only retaining those whose property is an instance of the "Is A" concept
	private List<ConceptPropertyObject> getParentConceptPropertyObjects(Integer conceptDbId) {
		List<ConceptPropertyObject> parentProperties = new ArrayList<ConceptPropertyObject>();
			
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByConceptDbid(conceptDbId);
		if(allProperties != null && allProperties.isEmpty() == false) {
			parentProperties = allProperties.stream()
											.filter(cpo -> cpo.getProperty().getIri().equals(isAConceptIri))
											.collect(Collectors.toList());
		}
		else {
			LOG.debug(String.format("No ConceptPropertyObject instances could be found with the concept field value: %d", conceptDbId));
		}
		
		return parentProperties;
	}		
}
