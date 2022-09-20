package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.*;
import java.util.stream.Collectors;

public class FunctionService {
	private ConceptRepository conceptRepository = new ConceptRepository();
	private EntityService entityService = new EntityService();

	public JsonNode callFunction(String iri, Map<String,Object> arguments) throws Exception {
		switch (iri){
			case  (IM.NAMESPACE+"Function_SnomedConceptGenerator") :
				return conceptRepository.createConcept(IM.NAMESPACE);
			case (IM.NAMESPACE+"Function_LocalNameRetriever") :
				return getLocalName(arguments);
			case (IM.NAMESPACE+"Function_GetAdditionalAllowableTypes") :
				return getAdditionalAllowableTypes(arguments);
			case (IM.NAMESPACE+"Function_GetLogicOptions") :
				return getLogicOptions();
			default :
				throw new IllegalArgumentException("No such function");
		}
	}

	private JsonNode getLocalName(Map<String,Object> arguments){
		if (null == arguments)
			throw new IllegalArgumentException("No arguments, send json property/value pairs in request body");
		String iri= (String) arguments.get("entityIri");
		String fieldName= (String) arguments.get("fieldName");
		if (null == arguments.get("entityIri"))
			throw new IllegalArgumentException("No entity iri property in request body");
		if (null == arguments.get("fieldName"))
			throw new IllegalArgumentException("No 'fieldName' to return the local name in. e.g. \"fieldName\" : \"code\"");
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            return om.createObjectNode().put(fieldName, iri.substring(iri.lastIndexOf("#") + 1));
        }
	}

	private JsonNode getAdditionalAllowableTypes(Map<String, Object> arguments) {
		if (null == arguments)
			throw new IllegalArgumentException("No arguments, send json property/value pairs in request body");
		if (null == arguments.get("entityIri"))
			throw new IllegalArgumentException("No entity iri property in request body");
		List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.ENTITY_TYPES.getIri(), null,1, 200, false);
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (IM.CONCEPT.getIri().equals(arguments.get("entityIri"))) {
                List<EntityReferenceNode> filteredResults = results.stream().filter(t -> Set.of(arguments.get("entityIri"), RDF.PROPERTY.getIri(), SHACL.NODESHAPE.getIri()).contains(t.getIri())).collect(Collectors.toList());
                List<TTIriRef> filteredResultsAsIri = filteredResults.stream().map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
                return om.valueToTree(filteredResultsAsIri);
            } else {
                List<EntityReferenceNode> filteredResults = results.stream().filter(t -> Set.of(arguments.get("entityIri")).contains(t.getIri())).collect(Collectors.toList());
                List<TTIriRef> filteredResultsAsIri = filteredResults.stream().map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
                return om.valueToTree(filteredResultsAsIri);
            }
        }
	}

	private JsonNode getLogicOptions() {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            Set<String> iris = new HashSet(Arrays.asList(SHACL.AND.getIri(), SHACL.OR.getIri(), SHACL.NOT.getIri()));
            Set<TTIriRef> iriRefs = entityService.getNames(iris);
            List<TTIriRef> options = new ArrayList<>(iriRefs);
            return om.valueToTree(options);
        }
	}
}
