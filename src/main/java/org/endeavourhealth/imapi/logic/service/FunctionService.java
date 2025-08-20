package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class FunctionService {
  public static final String ONE_OR_MORE_ARGUMENTS_ARE_MISSING_PARAMETER_KEY = "One or more arguments are missing parameter key";
  public static final String ENTITY_IRI = "entityIri";
  public static final String NO_ENTITY_IRI_WHERE_IN_REQUEST_BODY = "No entity iri where in request body";
  private final EntityService entityService = new EntityService();
  private final UserService userService = new UserService();

  private final RequestObjectService requestObjectService = new RequestObjectService();

  public JsonNode callFunction(HttpServletRequest request, String iri, List<Argument> arguments) throws JsonProcessingException {
    return switch (IM_FUNCTION.from(iri)) {
      case IM_FUNCTION.LOCAL_NAME_RETRIEVER -> getLocalName(arguments);
      case IM_FUNCTION.GET_ADDITIONAL_ALLOWABLE_TYPES -> getAdditionalAllowableTypes(arguments);
      case IM_FUNCTION.GET_LOGIC_OPTIONS -> getLogicOptions();
      case IM_FUNCTION.GET_SET_EDITOR_IRI_SCHEMES -> getSetEditorIriSchemes();
      case IM_FUNCTION.IM1_SCHEME_OPTIONS -> getIM1SchemeOptions();
      case IM_FUNCTION.SCHEME_FROM_IRI -> getSchemeFromIri(arguments);
      case IM_FUNCTION.GET_USER_EDITABLE_SCHEMES -> getUserEditableSchemes(request);
      default -> throw new IllegalArgumentException("No such function: " + iri);
    };
  }

  private JsonNode getLocalName(List<Argument> arguments) {
    if (null == arguments)
      throw new IllegalArgumentException("No arguments, send json where/value pairs in request body");
    String iri = null;
    String fieldName = null;
    for (Argument arg : arguments) {
      if (null == arg.getParameter())
        throw new IllegalArgumentException(ONE_OR_MORE_ARGUMENTS_ARE_MISSING_PARAMETER_KEY);
      if (ENTITY_IRI.equals(arg.getParameter())) iri = arg.getValueParameter();
      if ("fieldName".equals(arg.getParameter())) fieldName = arg.getValueData();
    }
    if (null == iri)
      throw new IllegalArgumentException(NO_ENTITY_IRI_WHERE_IN_REQUEST_BODY);
    if (null == fieldName)
      throw new IllegalArgumentException("No 'fieldName' to return the local name in. e.g. \"fieldName\" : \"code\"");
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return om.createObjectNode().put(fieldName, iri.substring(iri.lastIndexOf("#") + 1));
    }
  }

  private JsonNode getSchemeFromIri(List<Argument> arguments) {
    if (null == arguments)
      throw new IllegalArgumentException("No arguments, send json where/value pairs in request body");
    String iri = null;
    for (Argument arg : arguments) {
      if (null == arg.getParameter())
        throw new IllegalArgumentException(ONE_OR_MORE_ARGUMENTS_ARE_MISSING_PARAMETER_KEY);
      if (ENTITY_IRI.equals(arg.getParameter())) iri = arg.getValueParameter();
    }
    if (null == iri)
      throw new IllegalArgumentException(NO_ENTITY_IRI_WHERE_IN_REQUEST_BODY);
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      String schemeIri = iri.substring(0, iri.lastIndexOf("#") + 1);
      List<EntityReferenceNode> schemes = entityService.getImmediateChildren(IM.NAMESPACE.toString(), new ArrayList<>(), 1, 1000, false);
      List<EntityReferenceNode> schemesFiltered = schemes.stream().filter(s -> s.getIri().equals(schemeIri)).toList();
      List<TTIriRef> schemesFilteredIriRef = schemesFiltered.stream().map(s -> new TTIriRef().setIri(s.getIri()).setName(s.getName())).toList();
      if (schemesFiltered.isEmpty()) throw new IllegalArgumentException("Iri has invalid scheme");
      return om.valueToTree(schemesFilteredIriRef);
    }
  }

  private JsonNode getAdditionalAllowableTypes(List<Argument> arguments) {
    if (null == arguments)
      throw new IllegalArgumentException("No arguments, send array of json where/value pairs in request body");
    String entityIri = null;
    for (Argument arg : arguments) {
      if (null == arg.getParameter())
        throw new IllegalArgumentException(ONE_OR_MORE_ARGUMENTS_ARE_MISSING_PARAMETER_KEY);
      if (ENTITY_IRI.equals(arg.getParameter())) entityIri = arg.getValueIri().getIri();
    }
    if (null == entityIri)
      throw new IllegalArgumentException(NO_ENTITY_IRI_WHERE_IN_REQUEST_BODY);
    List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.ENTITY_TYPES.toString(), null, 1, 200, false);
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      if (IM.CONCEPT.toString().equals(entityIri)) {
        String finalEntityIri = entityIri;
        List<EntityReferenceNode> filteredResults = results.stream().filter(t -> Set.of(finalEntityIri, RDF.PROPERTY, SHACL.NODESHAPE).contains(t.getIri())).toList();
        List<TTIriRef> filteredResultsAsIri = filteredResults.stream().map(t -> new TTIriRef(t.getIri(), t.getName())).toList();
        return om.valueToTree(filteredResultsAsIri);
      } else {
        String finalEntityIri1 = entityIri;
        List<EntityReferenceNode> filteredResults = results.stream().filter(t -> Objects.equals(finalEntityIri1, t.getIri())).toList();
        List<TTIriRef> originalResultIri = filteredResults.stream().map(t -> new TTIriRef(t.getIri(), t.getName())).toList();
        return om.valueToTree(originalResultIri);
      }
    }
  }

  private JsonNode getLogicOptions() {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      Set<String> iris = asHashSet(SHACL.AND, SHACL.OR, SHACL.NOT);
      Set<TTIriRef> iriRefs = entityService.getNames(iris);
      List<TTIriRef> options = new ArrayList<>(iriRefs);
      return om.valueToTree(options);
    }
  }

  private JsonNode getSetEditorIriSchemes() {
    List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.NAMESPACE.toString(), null, 1, 200, false);
    List<TTIriRef> resultsAsIri = results.stream().map(r -> new TTIriRef(r.getIri(), r.getName())).toList();
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return om.valueToTree(resultsAsIri);
    }
  }

  private JsonNode getUserEditableSchemes(HttpServletRequest request) throws JsonProcessingException {
    List<EntityReferenceNode> results = entityService.getImmediateChildren(IM.NAMESPACE.toString(), null, 1, 200, false);
    String userId = requestObjectService.getRequestAgentId(request);
    List<String> organisations = userService.getUserOrganisations(userId);
    List<TTIriRef> resultsAsIri = results.stream().filter(r -> organisations.stream().anyMatch(o -> o.equals(r.getIri()))).map(r -> new TTIriRef(r.getIri(), r.getName())).toList();
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
}
