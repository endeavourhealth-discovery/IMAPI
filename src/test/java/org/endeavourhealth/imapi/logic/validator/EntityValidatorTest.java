package org.endeavourhealth.imapi.logic.validator;

import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.requests.EntityValidationRequest;
import org.endeavourhealth.imapi.model.responses.EntityValidationResponse;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityValidatorTest {
  EntityValidator entityValidator = new EntityValidator();
  EntityService entityService = new EntityService();

  @Nested
  class hasValidParents {
    @Test
    void isValidIriAndData() throws ValidationException {
      TTEntity actual = new TTEntity();
      actual.addObject(iri(IM.IS_CONTAINED_IN), iri(IM.QUERY.toString(), "Query"));
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void isInvalidIriAndData() throws ValidationException {
      TTEntity actual = new TTEntity();
      actual.addObject(iri(Namespace.IM + "foo"), iri(IM.QUERY.toString(), "Query"));
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void isValidIriAndInvalidData() throws ValidationException {
      TTEntity actual = new TTEntity();
      actual.addObject(iri(IM.IS_CONTAINED_IN), "foo");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void isInvalidIriAndInvalidData() throws ValidationException {
      TTEntity actual = new TTEntity();
      actual.addObject(iri(Namespace.IM + "foo"), "bar");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }
  }

  @Nested
  class isValidIri {
    @Test
    void passesWithCorrectIri() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealth.info/im#903031000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsWithSpaces() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealth.info/im#90303 1000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri code contains invalid characters");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsWithMultipleSpecialCharacters() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealth.info/im#90303 10+00$25&21/04");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri code contains invalid characters");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsWithHashInIdentifier() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealth.info/im#9030310002521#04");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity IRI contains invalid character # within identifier");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfUrlMissingHash() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealth.info/im903031000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity IRI must contain #");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfUrlIsWrongFormat() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealthinfo/im#903031000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri URL is invalid");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfIriMissingCode() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.setIri("http://endhealthinfo/im#");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri URL is invalid");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }
  }

  @Nested
  class isValidIriOrIriList {
    @Test
    void failsIfNoProperties() throws ValidationException {
      TTEntity entity = new TTEntity();
      entity.set(iri(SHACL.PROPERTY), new TTArray());
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Data models must have at least 1 property");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithoutPath() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      ttArray.add(new TTNode());
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithPathWithoutRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTNode().setIri("Some iri"));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithArrayPathWithoutRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTArray().add(new TTNode().setIri("Some iri")));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithArrayMultiPathWithNodeRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTArray().add(new TTNode().setIri("Some iri")).add(new TTNode().setIri("Some other iri")));
      ttNode.set(iri(SHACL.NODE), new TTArray().add(new TTNode().setIri("Some iri")));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithPathWithArrayMultiNodeRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTNode().setIri("Some iri"));
      ttNode.set(iri(SHACL.NODE), new TTArray().add(new TTNode().setIri("Some iri")).add(new TTNode().setIri("Some other iri")));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void passesIfPropertyWithPathWithNodeRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTArray().add(new TTNode().setIri("Some iri")));
      ttNode.set(iri(SHACL.NODE), new TTArray().add(new TTNode().setIri("Some iri")));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void passesIfPropertyWithPathWithClassRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTArray().add(new TTNode().setIri("Some iri")));
      ttNode.set(iri(SHACL.CLASS), new TTArray().add(new TTNode().setIri("Some iri")));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void passesIfPropertyWithPathWithDatatypeRange() throws ValidationException {
      TTEntity entity = new TTEntity();
      TTArray ttArray = new TTArray();
      TTNode ttNode = new TTNode();
      ttNode.set(iri(SHACL.PATH), new TTArray().add(new TTNode().setIri("Some iri")));
      ttNode.set(iri(SHACL.DATATYPE), new TTArray().add(new TTNode().setIri("Some iri")));
      ttArray.add(ttNode);
      entity.set(iri(SHACL.PROPERTY), ttArray);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(VALIDATION.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService, List.of(Graph.IM))).usingRecursiveComparison().isEqualTo(response);
    }
  }
}

