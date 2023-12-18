package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.vocabulary.im.FUNCTION;

import java.util.*;
import java.util.stream.Collectors;

public class FunctionService {
	private ConceptRepository conceptRepository = new ConceptRepository();
	private EntityService entityService = new EntityService();
	private UserService userService = new UserService();

	private EntityRepository entityRepository = new EntityRepository();

	private final RequestObjectService requestObjectService = new RequestObjectService();

	public JsonNode callFunction(HttpServletRequest request, String iri, List<Argument> arguments) throws Exception {
		FUNCTION functionIri = FUNCTION.valueOf(iri);
        return switch (functionIri) {
            case SNOMED_CONCEPT_GENERATOR -> conceptRepository.createConcept(IM.NAMESPACE.iri);
            case LOCAL_NAME_RETRIEVER -> getLocalName(arguments);
            case GET_ADDITIONAL_ALLOWABLE_TYPES -> getAdditionalAllowableTypes(arguments);
            case GET_LOGIC_OPTIONS -> getLogicOptions();
            case GET_SET_EDITOR_IRI_SCHEMES -> getSetEditorIriSchemes();
            case IM1_SCHEME_OPTIONS -> getIM1SchemeOptions();
            case SCHEME_FROM_IRI -> getSchemeFromIri(arguments);
            case GET_USER_EDITABLE_SCHEMES -> getUserEditableSchemes(request);
            case GENERATE_IRI_CODE -> generateIriCode(arguments);
            default -> throw new IllegalArgumentException("No such function: " + iri);
        };
	}

	public Boolean callAskFunction(HttpServletRequest request, String iri, List<Argument> arguments) {
		return switch(FUNCTION.valueOf(iri)) {
			case IS_TYPE -> isType(arguments);
			default -> throw new IllegalArgumentException("No such ask function: " + iri);
		};
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

	private JsonNode getSchemeFromIri(List<Argument> arguments) {
		if (null == arguments)
			throw new IllegalArgumentException("No arguments, send json property/value pairs in request body");
		String iri = null;
		for (Argument arg : arguments) {
			if (null == arg.getParameter()) throw new IllegalArgumentException("One or more arguments are missing parameter key");
			if ("entityIri".equals(arg.getParameter())) iri = arg.getValueVariable();
		}
		if (null == iri)
			throw new IllegalArgumentException("No entity iri property in request body");
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			String schemeIri = iri.substring(0,iri.lastIndexOf("#")+1);
			List<EntityReferenceNode> schemes = entityService.getImmediateChildren(IM.GRAPH.iri,new ArrayList<>(),1,1000,false);
			List<EntityReferenceNode> schemesFiltered = schemes.stream().filter( s -> s.getIri().equals(schemeIri)).toList();
			List<TTIriRef> schemesFilteredIriRef = schemesFiltered.stream().map(s -> new TTIriRef().setIri(s.getIri()).setName(s.getName())).collect(Collectors.toList());
			if (schemesFiltered.isEmpty()) throw new IllegalArgumentException("Iri has invalid scheme");
			return om.valueToTree(schemesFilteredIriRef);
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
		List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.ENTITY_TYPES.iri, null,1, 200, false);
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (IM.CONCEPT.iri.equals(entityIri)) {
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
		List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.GRAPH.iri,null,1,200,false);
		List<TTIriRef> resultsAsIri = results.stream().map(r -> new TTIriRef(r.getIri(),r.getName())).collect(Collectors.toList());
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			return om.valueToTree(resultsAsIri);
		}
	}

	private JsonNode getUserEditableSchemes(HttpServletRequest request) throws JsonProcessingException {
		List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.GRAPH.iri,null,1,200,false);
		String userId = requestObjectService.getRequestAgentId(request);
		List<String> organisations = userService.getUserOrganisations(userId);
		List<TTIriRef> resultsAsIri = results.stream().filter(r -> organisations.stream().anyMatch(o -> o.equals(r.getIri()))).map(r -> new TTIriRef(r.getIri(),r.getName())).collect(Collectors.toList());
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			return om.valueToTree(resultsAsIri);
		}
	}

	private JsonNode getIM1SchemeOptions() {
		List<String> results = entityService.getIM1SchemeOptions();
		try (CachedObjectMapper om = new CachedObjectMapper()) {
			return om.stringArrayToTree(results);
		}
	}

	private JsonNode generateIriCode(List<Argument> arguments) throws Exception {
		if (null == arguments)
			throw new IllegalArgumentException("No arguments, send array of json property/value pairs in request body");
		String entityIri = null;
		for (Argument arg : arguments) {
			if (null == arg.getParameter()) throw new IllegalArgumentException("One or more arguments are missing parameter key");
			if ("scheme".equals(arg.getParameter())) entityIri = arg.getValueIri().getIri();
		}
		if (null == entityIri)
			throw new IllegalArgumentException("No scheme parameter in request body");
		List<EntityReferenceNode> schemes = entityService.getImmediateChildren(IM.GRAPH.iri, null,1, 200, false);
		String finalEntityIri2 = entityIri;
		if (schemes.stream().noneMatch(s -> s.getIri().equals(finalEntityIri2))) throw new IllegalArgumentException("Iri is not a valid scheme");
		CachedObjectMapper om = new CachedObjectMapper();
		JsonNode generated;
        if (entityIri.equals(IM.NAMESPACE.iri) || entityIri.equals(SNOMED.NAMESPACE.iri)) {
			return om.createObjectNode().put("code", conceptRepository.createConcept(IM.NAMESPACE.iri).get("iri").get("@id").asText().split("#")[1]);
		} else return om.createObjectNode().put("iri", "");
	}

	private Boolean isType(List<Argument> arguments) {
		if (null == arguments) throw new IllegalArgumentException("Missing arguments");
		Argument type = arguments.stream().filter(arg -> arg.getParameter().equals("type")).findFirst().orElse(null);
		Argument searchIri = arguments.stream().filter(arg -> arg.getParameter().equals("searchIri")).findFirst().orElse(null);
		if (null == type) throw new IllegalArgumentException("Missing argument with parameter 'type'");
		if (null == searchIri) throw new IllegalArgumentException("Missing argument with parameter 'searchIri'");
		if (null == type.getValueIri()) throw new IllegalArgumentException("Missing 'type' valueIri");
		if (null == searchIri.getValueData()) throw new IllegalArgumentException("Missing 'searchIri' valueData");
		List<EntityReferenceNode> validTypes = entityService.getImmediateChildren(IM.ENTITY_TYPES.iri,null,null,null,false);
		if (null != validTypes.stream().filter(vt -> vt.getIri().equals(type.getValueIri().getIri())).findFirst().orElse(null)) return entityRepository.isType(type.getValueIri().getIri(),searchIri.getValueData());
		else throw new IllegalArgumentException("Type: '" + type.getValueIri().getIri() + "' is not a valid entity type");
	}
}
