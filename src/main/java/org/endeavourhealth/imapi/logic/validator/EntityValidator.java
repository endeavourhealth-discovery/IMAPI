package org.endeavourhealth.imapi.logic.validator;

import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.requests.EntityValidationRequest;
import org.endeavourhealth.imapi.model.responses.EntityValidationResponse;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityValidator {
  EntityService entityService;

  public EntityValidationResponse validate(EntityValidationRequest request, EntityService entityService, List<Graph> graphs) throws ValidationException {
    this.entityService = entityService;
    return switch (VALIDATION.from(request.getValidationIri())) {
      case VALIDATION.HAS_PARENT -> hasValidParents(request.getEntity());
      case VALIDATION.IS_DEFINITION -> isValidDefinition(request.getEntity());
      case VALIDATION.IS_IRI -> isValidIri(request.getEntity());
      case VALIDATION.IS_TERMCODE -> isValidTermCodes(request.getEntity());
      case VALIDATION.IS_PROPERTY -> isValidProperties(request.getEntity());
      case VALIDATION.IS_SCHEME -> isValidScheme(request.getEntity(), graphs);
      case VALIDATION.IS_STATUS -> isValidStatus(request.getEntity(), graphs);
      case VALIDATION.IS_ROLE_GROUP -> isValidRoleGroups(request.getEntity());
      default -> throw new ValidationException("Invalid validation IRI: " + request.getValidationIri());
    };
  }

  private EntityValidationResponse hasValidParents(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false);
    response.setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
    if (hasParameterAndAllAreTTIriRefs(entity, RDFS.SUBCLASS_OF.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, IM.IS_CONTAINED_IN.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, IM.IS_SUBSET_OF.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, RDFS.SUB_PROPERTY_OF.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, IM.IS_CHILD_OF.toString())) isValid(response);
    return response;
  }

  private boolean isValidTTIriRef(TTValue value) {
    return value.isIriRef() && !value.asIriRef().getIri().isEmpty() && !value.asIriRef().getName().isEmpty();
  }

  private void isValid(EntityValidationResponse response) {
    response.setValid(true);
    response.setMessage(null);
  }

  private boolean hasParameterAndAllAreTTIriRefs(TTEntity entity, String parameter) {
    return entity.has(iri(parameter)) && !entity.get(iri(parameter)).isEmpty() && entity.get(iri(parameter)).getElements().stream().allMatch(this::isValidTTIriRef);
  }

  private EntityValidationResponse isValidDefinition(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false);
    response.setMessage("Entity definition is invalid");
    if (entity.has(IM.DEFINITION) || entity.has(IM.IS_SUBSET_OF) || entity.has(IM.HAS_SUBSET)) {
      if (entity.has(IM.DEFINITION)) {
        try {
          Query query = entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
          if (query.isInvalid()) {
            response.setMessage("Query definition has unknown concepts or is invalid. Check using editor.");
            response.setValid(false);
            return response;
          }
        } catch (Exception e) {
          response.setMessage(e.getMessage());
          response.setValid(false);
          return response;
        }
      }
      isValid(response);
    }
    return response;
  }


  private EntityValidationResponse isValidIri(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false);
    response.setMessage("Entity IRI is invalid");
    if (entity.getIri() == null || entity.getIri().isEmpty()) {
      response.setMessage("Entity is missing iri");
      return response;
    } else if (!entity.getIri().contains("#")) {
      response.setMessage("Entity IRI must contain #");
      return response;
    }
    String[] splits = entity.getIri().split("#");
    if (splits.length > 2)
      response.setMessage("Entity IRI contains invalid character # within identifier");
    else if (!splits[0].matches("^http://[a-zA-Z]+\\.[a-zA-Z]+/[a-zA-Z]+$"))
      response.setMessage("Iri URL is invalid");
    else if (splits.length < 2) response.setMessage("Iri must contain a code");
    else if (!encodeUrlJS(splits[1]).equals(splits[1])) {
      String encodedCode = encodeUrlJS(splits[1]);
      boolean hasInvalidCharacter = Pattern.compile("%[0-9a-zA-Z]{2}").matcher(encodedCode).find();
      if (hasInvalidCharacter) response.setMessage("Iri code contains invalid characters");
    } else if (splits[1].equals("CSET_")) response.setMessage("Iri code missing after prefix: " + splits[1]);
    else {
      response.setValid(true);
      response.setMessage(null);
    }
    return response;
  }

  private String encodeUrlJS(String url) {
    return URLEncoder
      .encode(url, StandardCharsets.UTF_8)
      .replace("+", "%20")
      .replace("~", "%7E")
      .replace("'", "%27")
      .replace("(", "%28")
      .replace(")", "%29")
      .replace("!", "%21");
  }

  private EntityValidationResponse isValidTermCodes(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false);
    response.setMessage("1 or more term codes are invalid");
    if (entity.has(iri(IM.HAS_TERM_CODE))) {
      if (entity.get(iri(IM.HAS_TERM_CODE)).getElements().stream().allMatch(this::isValidTermCode)) {
        response.setValid(true);
        response.setMessage(null);
      }
    } else {
      response.setValid(true);
      response.setMessage(null);
    }
    return response;
  }

  private boolean isValidTermCode(TTValue value) {

    return value.asNode().has(iri(IM.CODE)) &&
      value.asNode().has(iri(IM.HAS_STATUS)) &&
      value.asNode().has(iri(RDFS.LABEL)) &&
      !value.asNode().get(iri(IM.CODE)).get(0).asLiteral().getValue().isEmpty() &&
      value.asNode().get(iri(IM.HAS_STATUS)).get(0).asIriRef() != null &&
      !value.asNode().get(iri(RDFS.LABEL)).get(0).asLiteral().getValue().isEmpty() && value.asNode().get(iri(IM.HAS_STATUS)).size() == 1 && value.asNode().get(iri(IM.HAS_STATUS)).get(0).asIriRef() != null;
  }

  private EntityValidationResponse isValidProperties(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(true).setMessage(null);
    TTArray properties = entity.get(iri(SHACL.PROPERTY));
    if (properties == null || properties.isEmpty()) {
      response.setValid(false);
      response.setMessage("Data models must have at least 1 property");
    } else {
      for (TTValue property : properties.getElements()) {
        if (!isValidIriOrIriList(property.asNode().get(iri(SHACL.PATH)), 1, 1)) response.setValid(false);
        if (
          !isValidIriOrIriList(property.asNode().get(iri(SHACL.NODE)), 1, 1) &&
            !isValidIriOrIriList(property.asNode().get(iri(SHACL.DATATYPE)), 1, 1) &&
            !isValidIriOrIriList(property.asNode().get(iri(SHACL.CLASS)), 1, 1)
        ) response.setValid(false);
      }
      if (!response.isValid()) response.setMessage("One or more invalid properties");
    }
    return response;
  }

  private boolean isValidIriOrIriList(TTArray list, int minLength, int maxLength) {
    if (null == list) return minLength == 0;
    if (list.size() < minLength || list.size() > maxLength) return false;
    return list.getElements().stream().allMatch(item -> {
      if (item.isIriRef()) return !item.asIriRef().getIri().isEmpty();
      else if (item.isNode() && null != item.asNode().getIri()) return !item.asNode().getIri().isEmpty();
      else return false;
    });
  }

  private EntityValidationResponse isValidScheme(TTEntity entity, List<Graph> graphs) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false).setMessage("Scheme is invalid");
    List<TTIriRef> schemes = entityService.getChildren(IM.NAMESPACE.toString(), null, null, null, false, graphs);
    if (entity.has(iri(IM.HAS_SCHEME)) && !entity.get(iri(IM.HAS_SCHEME)).isEmpty() && entity.get(iri(IM.HAS_SCHEME)).get(0).isIriRef()) {
      if (schemes.stream().anyMatch(s -> s.getIri().equals(entity.get(iri(IM.HAS_SCHEME)).get(0).asIriRef().getIri()))) {
        response.setValid(true);
        response.setMessage(null);
      }
    }
    return response;
  }

  private EntityValidationResponse isValidStatus(TTEntity entity, List<Graph> graphs) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false).setMessage("Status is invalid");
    List<TTIriRef> schemes = entityService.getChildren(IM.STATUS.toString(), null, null, null, false, graphs);
    if (entity.has(iri(IM.HAS_STATUS)) && !entity.get(iri(IM.HAS_STATUS)).isEmpty() && entity.get(iri(IM.HAS_STATUS)).get(0).isIriRef()) {
      if (schemes.stream().anyMatch(s -> s.getIri().equals(entity.get(iri(IM.HAS_STATUS)).get(0).asIriRef().getIri()))) {
        response.setValid(true);
        response.setMessage(null);
      }
    }
    return response;
  }

  private EntityValidationResponse isValidRoleGroups(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(true).setMessage(null);
    if (!entity.has(iri(IM.ROLE_GROUP))) return response;
    for (TTValue group : entity.get(iri(IM.ROLE_GROUP)).getElements()) {
      if (group.asNode().has(iri(IM.GROUP_NUMBER))) {
        if (group.asNode().getPredicateMap().size() <= 1) {
          response.setValid(false);
          response.setMessage("1 or more role groups are invalid");
        } else {
          for (Map.Entry<TTIriRef, TTArray> groupData : group.asNode().getPredicateMap().entrySet()) {
            String key = groupData.getKey().getIri();
            TTArray value = groupData.getValue();
            if (!key.equals(IM.GROUP_NUMBER.toString())) {
              if (key.isEmpty() || value.isEmpty() || value.get(0).asIriRef().getIri().isEmpty() || value.get(0).asIriRef().getName().isEmpty()) {
                response.setValid(false);
                response.setMessage("1 or more role groups are invalid");
              }
            }
          }
        }
      }
    }
    return response;
  }
}
