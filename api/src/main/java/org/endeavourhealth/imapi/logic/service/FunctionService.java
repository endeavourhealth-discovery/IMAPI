package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.iml.Argument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class FunctionService {
	private ConceptRepository conceptRepository = new ConceptRepository();
	private EntityService entityService = new EntityService();

	public JsonNode callFunction(String iri, List<Argument> arguments) throws Exception {
		switch (iri){
			case  (IM.NAMESPACE+"Function_SnomedConceptGenerator") :
				return conceptRepository.createConcept(IM.NAMESPACE);
			case (IM.NAMESPACE+"Function_LocalNameRetriever") :
				return getLocalName(arguments);
			case (IM.NAMESPACE+"Function_GetAdditionalAllowableTypes") :
				return getAdditionalAllowableTypes(arguments);
			case (IM.NAMESPACE+"Function_GetLogicOptions") :
				return getLogicOptions();
			case (IM.NAMESPACE + "Function_GetSetEditorIriSchemes"):
				return getSetEditorIriSchemes();
			default :
				throw new IllegalArgumentException("No such function");
		}
	}

	private JsonNode getLocalName(List<Argument> arguments){
		if (null == arguments)
			throw new IllegalArgumentException("No arguments, send json property/value pairs in request body");
		String iri = null;
		String fieldName = null;
		for (Argument arg : arguments) {
			if (null == arg.getParameter()) throw new IllegalArgumentException("One or more arguments are missing parameter key");
			if ("entityIri".equals(arg.getParameter())) iri = arg.getValueVariable();
			if ("fieldName".equals(arg.getParameter())) fieldName = arg.getValueData();
		}
		if (null == iri)
			throw new IllegalArgumentException("No entity iri property in request body");
		if (null == fieldName)
			throw new IllegalArgumentException("No 'fieldName' to return the local name in. e.g. \"fieldName\" : \"code\"");
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            return om.createObjectNode().put(fieldName, iri.substring(iri.lastIndexOf("#") + 1));
        }
	}

	private JsonNode getAdditionalAllowableTypes(List<Argument> arguments) {
		if (null == arguments)
			throw new IllegalArgumentException("No arguments, send array of json property/value pairs in request body");
		String entityIri = null;
		for (Argument arg : arguments) {
			if (null == arg.getParameter()) throw new IllegalArgumentException("One or more arguments are missing parameter key");
			if ("entityIri".equals(arg.getParameter())) entityIri = arg.getValueIri().getIri();
		}
		if (null == entityIri)
			throw new IllegalArgumentException("No entity iri property in request body");
		List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.ENTITY_TYPES.getIri(), null,1, 200, false);
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (IM.CONCEPT.getIri().equals(entityIri)) {
				String finalEntityIri = entityIri;
				List<EntityReferenceNode> filteredResults = results.stream().filter(t -> Set.of(finalEntityIri, RDF.PROPERTY.getIri(), SHACL.NODESHAPE.getIri()).contains(t.getIri())).collect(Collectors.toList());
                List<TTIriRef> filteredResultsAsIri = filteredResults.stream().map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
                return om.valueToTree(filteredResultsAsIri);
            } else {
				String finalEntityIri1 = entityIri;
				List<EntityReferenceNode> filteredResults = results.stream().filter(t -> Set.of(finalEntityIri1).contains(t.getIri())).collect(Collectors.toList());
				List<TTIriRef> originalResultIri = filteredResults.stream().map(t -> new TTIriRef(t.getIri(), t.getName())).collect(Collectors.toList());
                return om.valueToTree(originalResultIri);
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

	private JsonNode getSetEditorIriSchemes() {
		List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.GRAPH.getIri(),null,1,200,false);
		List<TTIriRef> resultsAsIri = results.stream().map(r -> new TTIriRef(r.getIri(),r.getName())).collect(Collectors.toList());
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			return om.valueToTree(resultsAsIri);
		}
	}
}
