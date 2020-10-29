package com.endavourhealth.services.concept;

import static com.endavourhealth.services.concept.ConceptConverter.toConcept;
import static com.endavourhealth.services.properties.PropertyConverter.flip;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;
import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.properties.PropertiesService;
import com.endavourhealth.services.properties.models.Property;
import com.endavourhealth.services.properties.models.PropertyNode;

@Service
public class ConceptService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConceptService.class);

	@Autowired
	ConceptRepository conceptRepository;
	
	@Autowired
	PropertiesService propertyService;
	
	@Autowired
	ConceptTctRepository conceptTctRepository;
	
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
	public Concept getConcept(String iri, Set<String> inheritanceProperties, String rootParentIri) {
		Concept concept = toConcept(conceptRepository.findByIri(iri));
		
		if(concept != null && inheritanceProperties.isEmpty() == false ) {

			// get all parents up until and including the root parent
			concept.setParents(propertyService.getInheritedProperties(concept.getDbId(), inheritanceProperties, rootParentIri));

			// get all direct properties and remove the inheritance properties thus exclusing parents
			Set<Property> allChildProperties = propertyService.getProperties(concept.getDbId());

			Set<Property> properties = allChildProperties.stream()
						 								 .filter(p -> inheritanceProperties.contains(p.getIri()) == false)
						 								 .collect(Collectors.toSet());
			
			concept.setProperties(properties);
			
			// get all properties where the concept is referenced as a parent
			// Because this is inheritance flip the owner and values around
			Set<Property> children = propertyService.getReferences(concept.getDbId(), inheritanceProperties).stream()
																											.map(p -> flip(p))
																											.collect(Collectors.toSet());
			concept.setChildren(children);				  							  

			for(Property child : concept.getChildren()) {
				setChildCount(child.getValue());
			}
		}
		
		return concept;
	}

	public List<Concept> search(String term, String rootIri) {
		return conceptRepository.search(term, rootIri).stream()
													  .map(c -> toConcept(c))
													  .collect(Collectors.toList());
	}
	
	public Set<PropertyNode> getParents(String iri, Set<String> inheritanceProperties, String rootParentIri) {
		Set<PropertyNode> parents = new HashSet<PropertyNode>();
		
		Concept concept = toConcept(conceptRepository.findByIri(iri));
		
		if(concept != null && inheritanceProperties.isEmpty() == false ) {
			parents.addAll(propertyService.getInheritedProperties(concept.getDbId(), inheritanceProperties, rootParentIri));
		}
		
		return parents;
	}
	
	public Set<Property> getChildren(String iri, Set<String> inheritanceProperties) {
		Set<Property> children = new HashSet<Property>();
		
		Concept concept = toConcept(conceptRepository.findByIri(iri));
		
		if(concept != null && inheritanceProperties.isEmpty() == false ) {
			children.addAll(propertyService.getProperties(concept.getDbId(), inheritanceProperties.toArray(new String[] {})));
		}
		
		return children;
	}
	
	public Set<Property> getProperties(String iri, Set<String> inheritanceProperties) {
		Set<Property> properties = new HashSet<Property>();
		
		Concept concept = toConcept(conceptRepository.findByIri(iri));
		
		if(concept != null && inheritanceProperties.isEmpty() == false ) {

			Set<Property> allChildProperties = propertyService.getProperties(concept.getDbId());

			properties = allChildProperties.stream()
						 				   .filter(p -> inheritanceProperties.contains(p.getIri()) == false)
						 				   .collect(Collectors.toSet());
			
		}
		
		return properties;
	}	
	
	private void setChildCount(Concept concept) {
		Long childCount = conceptTctRepository.countByTargetDbidAndLevel(concept.getDbId(), ConceptTctRepository.DIRECT_RELATION_LEVEL);
		concept.setChildCount(childCount);
	}
}
