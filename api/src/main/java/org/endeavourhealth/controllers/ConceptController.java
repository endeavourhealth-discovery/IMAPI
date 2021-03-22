package org.endeavourhealth.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.endeavourhealth.dto.ConceptDto;
import org.endeavourhealth.dto.GraphDto;
import org.endeavourhealth.converters.ImLangConverter;
import org.endeavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@Qualifier("ConceptServiceV3")
	IConceptService conceptService;

	@Autowired
	ImLangConverter imLangConverter;

	@PostMapping(value = "/search")
	public SearchResponse advancedSearch(@RequestBody SearchRequest request) {
		return new SearchResponse().setConcepts(conceptService.advancedSearch(request));
	}

	@GetMapping(value = "/{iri}", produces = "application/json")
	public Concept getConcept(@PathVariable("iri") String iri) {
		return conceptService.getConcept(iri);
	}

	@GetMapping(value = "/{iri}", produces = "application/imlang")
	public String getConceptImLang(@PathVariable("iri") String iri) {
		return imLangConverter.convertToImLang(conceptService.getConcept(iri));
	}

	@GetMapping(value = "/{iri}/children")
	public List<ConceptReferenceNode> getConceptChildren(@PathVariable("iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) {
		return conceptService.getImmediateChildren(iri, page, size, includeLegacy);
	}

	@GetMapping(value = "/{iri}/parentHierarchy")
	public List<ConceptReferenceNode> getConceptParentHierarchy(@PathVariable("iri") String iri) {
		List<ConceptReferenceNode> parents = conceptService.getParentHierarchy(iri);

		return parents;
	}

	@GetMapping(value = "/{iri}/parents")
	public List<ConceptReferenceNode> getConceptParents(@PathVariable("iri") String iri,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "includeLegacy", required = false) boolean includeLegacy) {
		return conceptService.getImmediateParents(iri, page, size, includeLegacy);
	}

	@GetMapping(value = "/{iri}/parents/definitions")
	public List<Concept> getConceptAncestorDefinitions(@PathVariable("iri") String iri) {
		return conceptService.getAncestorDefinitions(iri);
	}

	@GetMapping(value = "/{iri}/usages")
	public List<ConceptSummary> conceptUsages(@PathVariable("iri") String iri) {
		return conceptService.usages(iri);
	}

	@GetMapping(value = "/{iri}/mappedFrom")
	public List<ConceptReference> getCoreMappedFromLegacy(@PathVariable("iri") String legacyIri) {
		return conceptService.getCoreMappedFromLegacy(legacyIri);
	}

	@GetMapping(value = "/{iri}/mappedTo")
	public List<ConceptReference> getLegacyMappedToCore(@PathVariable("iri") String coreIri) {
		return conceptService.getLegacyMappedToCore(coreIri);
	}

	@PostMapping(value = "/{iri}/isWhichType")
	public List<ConceptReference> conceptIsWhichType(@PathVariable("iri") String iri,
			@RequestBody List<String> candidates) {
		return conceptService.isWhichType(iri, candidates);
	}

	@GetMapping(value = "/{iri}/members")
	public ExportValueSet valueSetMembersJson(@PathVariable("iri") String iri,
			@RequestParam(name = "expanded", required = false) boolean expanded) {
		return conceptService.getValueSetMembers(iri, expanded);
	}

	@GetMapping(value = "/{iri}/members", produces = { "text/csv" })
	public String valueSetMembersCSV(@PathVariable("iri") String iri,
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

	@GetMapping(value = "/{iri}/isMemberOf/{valueSetIri}")
	public ValueSetMembership isMemberOfValueSet(@PathVariable("iri") String conceptIri,
			@PathVariable("valueSetIri") String valueSetIri) {
		return conceptService.isValuesetMember(valueSetIri, conceptIri);
	}

	@GetMapping(value = "/referenceSuggestions")
	public List<ConceptReference> getSuggestions(@RequestParam String keyword, @RequestParam String word) {
//    	TODO generate and return suggestions
		return new ArrayList<ConceptReference>(
				Arrays.asList(new ConceptReference(":961000252104", "method (attribute)"),
						new ConceptReference(":1271000252102", "Hospital inpatient admission"),
						new ConceptReference(":1911000252103", "Transfer event")));
	}

	@PostMapping
	public Concept createConcept(@RequestBody ConceptDto conceptDto) {
//    	TODO convert conceptDto to concept
//    	TODO save concept
		return new Concept().setCode(conceptDto.getCode());
	}

	@GetMapping(value = "/{iri}/properties")
	public List<PropertyValue> getAllProperties(@PathVariable("iri") String iri) {
		Concept concept = conceptService.getConcept(iri);
		List<PropertyValue> properties = new ArrayList<PropertyValue>();
		if (concept.getProperty() != null) {
			properties.addAll(concept.getProperty());
		}
		
		List<String> flatParentIris = getParentProperties(iri, new ArrayList<String>());

		if (flatParentIris.size() != 0) {
			flatParentIris.forEach(parentIri -> {
				Concept parentConcept = conceptService.getConcept(parentIri);
				ConceptReference parentReference = new ConceptReference(parentConcept.getIri(),
						parentConcept.getName());
				if (parentConcept.getProperty() != null) {
					parentConcept.getProperty().forEach(property -> {
						property.setInheritedFrom(parentReference);
						properties.add(property);
					});
				}
			});
		}
		return properties;
	}

	@GetMapping(value = "/{iri}/graph")
	public GraphDto getGraphData(@PathVariable("iri") String iri) {
		Concept concept = conceptService.getConcept(iri);
		GraphDto graphData = new GraphDto().setIri(concept.getIri()).setName(concept.getName());

		GraphDto graphParents = new GraphDto().setName("Parents");
		GraphDto graphChildren = new GraphDto().setName("Children");
		GraphDto graphProps = new GraphDto().setName("Properties");

		List<ConceptReferenceNode> parents = conceptService.getImmediateParents(iri, null, null, false);
		List<ConceptReferenceNode> children = conceptService.getImmediateChildren(iri, null, null, false);
		List<PropertyValue> properties = getAllProperties(iri);

		parents.forEach(parent -> {
			GraphDto graphParent = new GraphDto().setIri(parent.getIri()).setName(parent.getName())
					.setPropertyType("is a");
			graphParents.getChildren().add(graphParent);
		});
		children.forEach(child -> {
			GraphDto graphChild = new GraphDto().setIri(child.getIri()).setName(child.getName());
			graphChildren.getChildren().add(graphChild);
		});
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
		graphData.getChildren().add(graphChildren);
		graphData.getChildren().add(graphProps);

		return graphData;
	}
	
	public List<String> getParentProperties(String iri, List<String> flatParentIris) {
		List<ConceptReferenceNode> parents = conceptService.getParentHierarchy(iri);
		if(parents != null) {
			for(ConceptReferenceNode parent: parents) {
				flatParentIris.add(parent.getIri());
	            getParentProperties(parent.getIri(), flatParentIris);
	        }
		}
		return flatParentIris;
	}
}
