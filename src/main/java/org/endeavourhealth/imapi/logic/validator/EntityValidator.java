package org.endeavourhealth.imapi.logic.validator;

import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.interfacemanager.model.*;

import java.lang.Exception;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EntityValidator {
  EntityService entityService;

  public EntityValidationResponse validate(EntityValidationRequest request, EntityService entityService) throws ValidationException {
    this.entityService = entityService;
    EntityValidationResponse response =
      switch (ValidationVocab.fromValue(request.getValidationIri())) {
        case ValidationVocab.HAS_PARENT -> hasValidParents(request.getEntity());
        case ValidationVocab.IS_DEFINITION -> isValidDefinition(request.getEntity());
        case ValidationVocab.IS_IRI -> isValidIri(request.getEntity());
        case ValidationVocab.IS_TERMCODE -> isValidTermCodes(request.getEntity());
        case ValidationVocab.IS_PROPERTY -> isValidProperties(request.getEntity());
        case ValidationVocab.IS_SCHEME -> isValidScheme(request.getEntity());
        case ValidationVocab.IS_STATUS -> isValidStatus(request.getEntity());
        case ValidationVocab.IS_ROLE_GROUP -> isValidRoleGroups(request.getEntity());
        case null ->
          throw new IllegalStateException("Failed to decode into VALIDATION enum: " + request.getValidationIri());
        default -> throw new ValidationException("Invalid validation IRI: " + request.getValidationIri());
      };
    System.out.println(response);
    return response;
  }

  private EntityValidationResponse hasValidParents(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false);
    response.setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
    if (hasParameterAndAllAreTTIriRefs(entity, RdfsVocab.SUBCLASS_OF.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, ImVocab.IS_CONTAINED_IN.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, ImVocab.IS_SUBSET_OF.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, RdfsVocab.SUB_PROPERTY_OF.toString())) isValid(response);
    if (hasParameterAndAllAreTTIriRefs(entity, ImVocab.IS_CHILD_OF.toString())) isValid(response);
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
    return entity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), parameter)) && !entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), parameter)).isEmpty() && entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), parameter)).getElements().stream().allMatch(this::isValidTTIriRef);
  }

  private EntityValidationResponse isValidDefinition(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false);
    response.setMessage("Entity definition is invalid");
    if (entity.has(ImVocab.DEFINITION) || entity.has(ImVocab.IS_SUBSET_OF) || entity.has(ImVocab.HAS_SUBSET)) {
      if (entity.has(ImVocab.DEFINITION)) {
        try {
          Query query = entity.get(ImVocab.DEFINITION).asLiteral().objectValue(Query.class);
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
    } else if (!entity.getIri().contains("#") && !entity.getIri().contains(":")) {
      response.setMessage("Entity IRI must contain : or #");
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
    if (entity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE))) {
      if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)).getElements().stream().allMatch(this::isValidTermCode)) {
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

    return value.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE)) &&
      value.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)) &&
      value.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)) &&
      !value.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE)).get(0).asLiteral().getValue().isEmpty() &&
      value.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)).get(0).asIriRef() != null &&
      !value.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL)).get(0).asLiteral().getValue().isEmpty() && value.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.
      HAS_STATUS)).size() == 1 && value.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)).get(0).asIriRef() != null;
  }

  private EntityValidationResponse isValidProperties(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(true).setMessage(null);
    TTArray properties = entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY));
    if (properties == null || properties.isEmpty()) {
      response.setValid(false);
      response.setMessage("Data models must have at least 1 property");
    } else {
      for (TTValue property : properties.getElements()) {
        if (!isValidIriOrIriList(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH)), 1, 1))
          response.setValid(false);
        if (
          !isValidIriOrIriList(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE)), 1, 1) &&
            !isValidIriOrIriList(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE)), 1, 1) &&
            !isValidIriOrIriList(property.asNode().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS)), 1, 1)
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

  private EntityValidationResponse isValidScheme(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false).setMessage("Scheme is invalid");
    List<TTIriRef> schemes = entityService.getChildren(ImVocab.ROOT_NAMESPACE.toString(), null, null, null, false);
    if (entity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME)) && !entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME)).
      isEmpty() && entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME)).get(0).isIriRef()) {
      if (schemes.stream().anyMatch(s -> s.getIri().equals(entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME)).
        get(0).asIriRef().getIri()))) {
        response.setValid(true);
        response.setMessage(null);
      }
    }
    return response;
  }

  private EntityValidationResponse isValidStatus(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(false).setMessage("Status is invalid");
    List<TTIriRef> schemes = entityService.getChildren(ImVocab.STATUS.toString(), null, null, null, false);
    if (entity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)) && !entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)).
      isEmpty() && entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)).get(0).isIriRef()) {
      if (schemes.stream().anyMatch(s -> s.getIri().equals(entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS)).
        get(0).asIriRef().getIri()))) {
        response.setValid(true);
        response.setMessage(null);
      }
    }
    return response;
  }

  private EntityValidationResponse isValidRoleGroups(TTEntity entity) {
    EntityValidationResponse response = new EntityValidationResponse();
    response.setValid(true).setMessage(null);
    if (!entity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP))) return response;
    for (TTValue group : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)).getElements()) {
      if (group.asNode().has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.GROUP_NUMBER))) {
        if (group.asNode().getPredicateMap().size() <= 1) {
          response.setValid(false);
          response.setMessage("1 or more role groups are invalid");
        } else {
          for (Map.Entry<TTIriRef, TTArray> groupData : group.asNode().getPredicateMap().entrySet()) {
            String key = groupData.getKey().getIri();
            TTArray value = groupData.getValue();
            if (!key.equals(ImVocab.GROUP_NUMBER.toString())) {
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
