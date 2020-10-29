package com.endavourhealth.services.properties;

import static com.endavourhealth.services.properties.PropertyConverter.toProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptPropertyDataRepository;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.dataaccess.util.ConceptTctUtil;
import com.endavourhealth.services.properties.models.Property;
import com.endavourhealth.services.properties.models.PropertyNode;

@Component
public class PropertiesService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;

	@Autowired
	ConceptPropertyDataRepository conceptPropertyDataRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;
	
	@Autowired
	ConceptTctUtil conceptTctService;
	
	public Set<PropertyNode> getInheritedProperties(int conceptDbId, Set<String> propertyIris, String rootParentIri) {
		Set<PropertyNode> parents = new HashSet<PropertyNode>();
		
		for(String propertyIri : propertyIris) {
			Integer propertyDbId = getPropertyDbId(propertyIri);
			if(propertyDbId != null) {
				// get all inherited properties
				parents.addAll(getProperties(conceptDbId, propertyDbId, rootParentIri));
			}
		}
		
		return parents;
	}
	
	public Set<Property> getProperties(int conceptDbId, String ... propertyIris) {
		Set<Property> children = new HashSet<Property>();
		
		if(propertyIris != null && propertyIris.length > 0) {
			for(String propertyIri : propertyIris) {
				Integer propertyDbId = getPropertyDbId(propertyIri);
				if(propertyDbId != null) {
					// get directly attached properties matching the given iri
					children.addAll(getProperties(conceptDbId, propertyDbId));
				}
			}
		}
		else {
			children.addAll(getProperties(conceptDbId));
		}
		
		return children;
	}

	public Set<Property> getReferences(int dbId, Set<String> propertyIris) {
		Set<Property> references = new HashSet<Property>();
		
		for(String propertyIri : propertyIris) {
			Integer propertyDbId = getPropertyDbId(propertyIri);
			if(propertyDbId != null) {
				references.addAll(getReferences(dbId, propertyDbId));
			}
		}
		
		return references;
	}
	
	private Integer getPropertyDbId(String propertyIri) {
		Integer propertyDbId = null;
		
		Concept concept = conceptRepository.findByIri(propertyIri);
		if(concept != null) {
			propertyDbId = concept.getDbid();
		}
		
		return propertyDbId;
	}
	
	private Set<PropertyNode> getProperties(int conceptDbId, int propertyDbId, String rootParentIri) {
		Set<PropertyNode> parents = new HashSet<PropertyNode>();
		
		Set<Property> directParents = getProperties(conceptDbId, propertyDbId);
		for(Property directParent : directParents) {
			if(directParent.getValue().getIri().equals(rootParentIri) == false) {
				PropertyNode parent = new PropertyNode(directParent);
				parents.add(parent);
				
				// get the parents parents and so on stopping when we get to the root parent
				getProperties(parent, propertyDbId, rootParentIri);				
			}
		}
	
		return parents;
	}
	 
	private void getProperties(PropertyNode child, int propertyDbId, String rootParentIri) {
		Set<Property> directParents = getProperties(child.getProperty().getValue().getDbId(), propertyDbId);
		
		for(Property directParent : directParents) {
			PropertyNode parent = new PropertyNode(directParent);
			child.setParent(parent);
			
			if(directParent.getValue().getIri().equals(rootParentIri) == false) {
				getProperties(parent, propertyDbId, rootParentIri);
			}
		}
	}

	private Set<Property> getProperties(int parentDbId, int propertyDbId) {
		Set<Property> children = new HashSet<Property>();
		
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByConceptDbidAndPropertyDbid(parentDbId, propertyDbId);
		
		for(ConceptPropertyObject propertyEntity : allProperties) {
			children.add(toProperty(propertyEntity));
		}
		
		return children;
	}
	
	private Set<Property> getProperties(int parentDbId) {
		Set<Property> children = new HashSet<Property>();
		
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByConceptDbid(parentDbId);
		
		for(ConceptPropertyObject propertyEntity : allProperties) {
			children.add(toProperty(propertyEntity));
		}
		
		return children;
	}

	private Set<Property> getReferences(int parentDbId, int propertyDbId) {
		Set<Property> references = new HashSet<Property>();
		
		List<ConceptPropertyObject> allProperties = conceptPropertyObjectRepository.findByObjectDbidAndPropertyDbid(parentDbId, propertyDbId);
		
		for(ConceptPropertyObject propertyEntity : allProperties) {
			references.add(toProperty(propertyEntity));
		}
		
		return references;
	}
	
	
	
	/// Dan 

//	public Properties getProperties(String iri) {
//		Concept concept = conceptRepository.findByIri(iri);
//		Properties properties = new Properties();
//		properties.setCoreProperties(getCoreProperties(concept.getDbid()));
//		properties.setInheritedProperties(getInheritedProperties(concept.getDbid()));
//		return properties;
//	}
//
//	public List<Property> getCoreProperties(Integer Dbid) {
//		List<Property> properties = new ArrayList<Property>();
//		// find concept property objects
//		List<ConceptPropertyObject> conceptPropertyObjects = conceptPropertyObjectRepository.findByConceptDbid(Dbid);
//		// get concepts for each of those objects
//		conceptPropertyObjects.forEach(conceptPropertyObject -> {
//			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
//			List<String> types = Arrays.asList("owl:topObjectProperty", "owl:topDataProperty");
//			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject, types)) {
//				Value value = new Value(conceptPropertyObject.getObject(), conceptPropertyObject.getConcept().getIri());
//				properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
//			}
//		});
//		return properties;
//	}
//	
//	public List<Property> getInheritedProperties(Integer Dbid) {
//		List<Property> properties = new ArrayList<Property>();
//		// find concept property objects
//		
//		List<ConceptPropertyObject> conceptPropertyObjects = new ArrayList<ConceptPropertyObject>();
//		List<ConceptTct> conceptTcts = conceptTctRepository.findBySourceDbid(Dbid);
//		conceptTcts.forEach(conceptTct -> {
//			conceptPropertyObjects.addAll(conceptPropertyObjectRepository.findByConceptDbid(conceptTct.getTarget().getDbid()));
//		});
//		
//		// get concepts for each of those objects
//		conceptPropertyObjects.forEach(conceptPropertyObject -> {
//			// lookup conceptPropertyObjects in the Transitive Closure table to determine if they are valid
//			List<String> types = Arrays.asList("owl:topObjectProperty", "owl:topDataProperty");
//			if (conceptTctService.checkIfPropertyIsValidType(conceptPropertyObject, types) && conceptPropertyObject.getConcept().getDbid() != Dbid) {
//				Value value = new Value(conceptPropertyObject.getObject(), conceptPropertyObject.getConcept().getIri());
//				properties.add(new Property(conceptPropertyObject, conceptPropertyObject.getProperty(), value));
//			}
//		});
//		return properties;
//	}
	
	

}
