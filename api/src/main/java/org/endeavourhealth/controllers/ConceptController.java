package org.endeavourhealth.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.endeavourhealth.converters.ConceptToImLang;
import org.endeavourhealth.dataaccess.ConceptServiceV3;
import org.endeavourhealth.dto.ConceptDto;
import org.endeavourhealth.dto.GraphDto;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/concept")
@CrossOrigin(origins = "*")
public class ConceptController {

	@Autowired
    ConceptServiceV3 conceptService;

	@Autowired
    ConceptToImLang conceptToImLang;

	@PostMapping(value = "/search")
	public SearchResponse advancedSearch(@RequestBody SearchRequest request) {
		return new SearchResponse().setConcepts(conceptService.advancedSearch(request));
	}

	@GetMapping(value = "", produces = "application/json")
	public TTConcept getConcept(@RequestParam(name = "iri") String iri) {
		return conceptService.getConcept(iri);
	}

	@GetMapping(value = "", produces = "application/imlang")
	public String getConceptImLang(@RequestParam(name = "iri") String iri) {
		return conceptToImLang.translateConceptToImLang(conceptService.getConcept(iri));
	}

	@GetMapping(value = "/children")
	public List<ConceptReferenceNode> getConceptChildren(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) {
		return conceptService.getImmediateChildren(iri, page, size, includeLegacy);
	}

	@GetMapping(value = "/parentHierarchy")
	public List<ConceptReferenceNode> getConceptParentHierarchy(@RequestParam(name = "iri") String iri) {
		List<ConceptReferenceNode> parents = conceptService.getParentHierarchy(iri);

		return parents;
	}

	@GetMapping(value = "/parents")
	public List<ConceptReferenceNode> getConceptParents(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) {
		return conceptService.getImmediateParents(iri, page, size, includeLegacy);
	}

	@GetMapping(value = "/parents/definitions")
	public List<TTConcept> getConceptAncestorDefinitions(@RequestParam(name = "iri") String iri) {
		return conceptService.getAncestorDefinitions(iri);
	}

	@GetMapping(value = "/usages")
	public List<ConceptSummary> conceptUsages(@RequestParam(name = "iri") String iri) {
		return conceptService.usages(iri);
	}

	@GetMapping(value = "/mappedFrom")
	public List<TTIriRef> getCoreMappedFromLegacy(@RequestParam(name = "iri") String legacyIri) {
		return conceptService.getCoreMappedFromLegacy(legacyIri);
	}

	@GetMapping(value = "/mappedTo")
	public List<TTIriRef> getLegacyMappedToCore(@RequestParam(name = "iri") String coreIri) {
		return conceptService.getLegacyMappedToCore(coreIri);
	}

	@PostMapping(value = "/isWhichType")
	public List<TTIriRef> conceptIsWhichType(@RequestParam(name = "iri") String iri,
			@RequestBody List<String> candidates) {
		return conceptService.isWhichType(iri, candidates);
	}

	@GetMapping(value = "/members")
	public ExportValueSet valueSetMembersJson(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) {
		return conceptService.getValueSetMembers(iri, expanded);
	}

	@GetMapping(value = "/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@RequestParam(name = "iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) {
		ExportValueSet exportValueSet = conceptService.getValueSetMembers(iri, expanded);

		StringBuilder sb = new StringBuilder();

		sb.append(
				"Inc\\Exc\tValueSetIri\tValueSetName\tMemberIri\tMemberTerm\tMemberCode\tMemberSchemeIri\tMemberSchemeName\n");

		for (ValueSetMember c : exportValueSet.getIncluded()) {
			sb.append("Inc\t").append(exportValueSet.getValueSet().getIri()).append("\t")
					.append(exportValueSet.getValueSet().getName()).append("\t").append(c.getConcept().getIri())
					.append("\t").append(c.getConcept().getName()).append("\t").append(c.getCode()).append("\t");
			if (c.getScheme() != null)
				sb.append(c.getScheme().getIri()).append("\t").append(c.getScheme().getName());

			sb.append("\n");
		}

		if (exportValueSet.getExcluded() != null) {
			for (ValueSetMember c : exportValueSet.getExcluded()) {
				sb.append("Exc\t").append(exportValueSet.getValueSet().getIri()).append("\t")
						.append(exportValueSet.getValueSet().getName()).append("\t").append(c.getConcept().getIri())
						.append("\t").append(c.getConcept().getName()).append("\t").append(c.getCode()).append("\t");
				if (c.getScheme() != null)
					sb.append(c.getScheme().getIri()).append("\t").append(c.getScheme().getName());

				sb.append("\n");
			}
		}

		return sb.toString();
	}

	@GetMapping(value = "/isMemberOf")
	public ValueSetMembership isMemberOfValueSet(@RequestParam(name = "iri") String conceptIri,
			@RequestParam("valueSetIri") String valueSetIri) {
		return conceptService.isValuesetMember(valueSetIri, conceptIri);
	}

	@GetMapping(value = "/referenceSuggestions")
	public List<TTIriRef> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
//    	TODO generate and return suggestions
		return new ArrayList<TTIriRef>(
				Arrays.asList(new TTIriRef(":961000252104", "method (attribute)"),
						new TTIriRef(":1271000252102", "Hospital inpatient admission"),
						new TTIriRef(":1911000252103", "Transfer event")));
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public TTConcept createConcept(@RequestBody ConceptDto conceptDto) {
//    	TODO convert conceptDto to concept
//    	TODO save concept
		return new TTConcept().setCode(conceptDto.getCode());
	}

	@GetMapping(value = "/properties")
	public List<PropertyValue> getAllProperties(@RequestParam(name = "iri") String iri) {
		TTConcept concept = conceptService.getConcept(iri);
		List<PropertyValue> properties = new ArrayList<PropertyValue>();

        if (concept.has(IM.PROPERTY_GROUP)) {
            for (TTValue propertyGroup : concept.getAsArray(IM.PROPERTY_GROUP).getElements()) {
                if (propertyGroup.isNode()) {
                    TTIriRef inheritedFrom = propertyGroup.asNode().has(IM.INHERITED_FROM)
                        ? propertyGroup.asNode().get(IM.INHERITED_FROM).asIriRef()
                        : null;

                    if (propertyGroup.asNode().has(SHACL.PROPERTY)) {
                        for (TTValue property : propertyGroup.asNode().get(SHACL.PROPERTY).asArray().getElements()) {
                            TTIriRef propertyPath = property.asNode().get(SHACL.PATH).asIriRef();
                            if (properties.stream().noneMatch(o -> o.getProperty().getIri().equals(propertyPath.getIri()))) {
                            	if(inheritedFrom != null) inheritedFrom.setName(getConcept(inheritedFrom.getIri()).getName());
                            	PropertyValue pv = new PropertyValue()
                                    .setInheritedFrom(inheritedFrom)
                                    .setProperty(propertyPath);
                                
                                propertyPath.setName(getConcept(propertyPath.getIri()).getName());
                                if (property.asNode().has(SHACL.CLASS)) pv.setValueType(property.asNode().get(SHACL.CLASS).asIriRef());
                                if (property.asNode().has(SHACL.DATATYPE)) pv.setValueType(property.asNode().get(SHACL.DATATYPE).asIriRef());
                                TTConcept valueType = getConcept(pv.getValueType().getIri());
                                if(valueType != null) pv.getValueType().setName(valueType.getName());
                                properties.add(pv);
                            }
                        }
                    }
                }
            }
        }
		return properties;
	}

	@GetMapping(value = "/graph")
	public GraphDto getGraphData(@RequestParam(name = "iri") String iri) {
		TTConcept concept = conceptService.getConcept(iri);
		
		GraphDto graphData = new GraphDto().setIri(concept.getIri()).setName(concept.getName());

		GraphDto graphParents = new GraphDto().setName("Parents");
//		GraphDto graphChildren = new GraphDto().setName("Children");
		GraphDto graphProps = new GraphDto().setName("Properties");
		
		List<TTValue> parents = concept.getAsArray(IM.IS_A).getElements();
//		List<ConceptReferenceNode> children = conceptService.getImmediateChildren(iri, null, null, false);
		List<PropertyValue> properties = getAllProperties(iri);

		if(parents !=null) {
			parents.forEach(parent -> {
				String parentIri = parent.asIriRef().getIri();
				String parentName = getConcept(parentIri).getName();
				GraphDto graphParent = new GraphDto().setIri(parentIri).setName(parentName)
						.setPropertyType("is a");
				graphParents.getChildren().add(graphParent);
			});
		}
		
//		children.forEach(child -> {
//			GraphDto graphChild = new GraphDto().setIri(child.getIri()).setName(child.getName());
//			graphChildren.getChildren().add(graphChild);
//		});
		properties.forEach(prop -> {
			GraphDto graphProp = new GraphDto().setIri(prop.getProperty().getIri())
					.setName(prop.getProperty().getName())
					.setInheritedFromName(prop.getInheritedFrom() != null ? prop.getInheritedFrom().getName() : "")
					.setInheritedFromIri(prop.getInheritedFrom() != null ? prop.getInheritedFrom().getIri() : "")
					.setPropertyType(prop.getProperty().getName()).setValueTypeIri(prop.getValueType().getIri())
					.setValueTypeName(prop.getValueType().getName());
			graphProps.getChildren().add(graphProp);
		});

		graphData.getChildren().add(graphParents);
//		graphData.getChildren().add(graphChildren);
		graphData.getChildren().add(graphProps);

		return graphData;
	}

	public List<String> getFlatParentHierarchy(String iri, List<String> flatParentIris) {
		List<ConceptReferenceNode> parents = conceptService.getParentHierarchy(iri);

		if (parents == null) {
			return flatParentIris;
		}

		for (ConceptReferenceNode parent : parents) {
			flatParentIris.add(parent.getIri());
			getFlatParentHierarchy(parent.getIri(), flatParentIris);
		}

		return flatParentIris;
	}
}
