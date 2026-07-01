package org.endeavourhealth.imapi.logic.validator;

import jakarta.xml.bind.ValidationException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.interfacemanager.model.EntityValidationRequest;
import org.endeavourhealth.interfacemanager.model.EntityValidationResponse;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityValidatorTest {
  final EntityValidator entityValidator = new EntityValidator();
  final EntityService entityService = new EntityService();

  @Nested
  class hasValidParents {
    @Test
    void isValidIriAndData() throws ValidationException {
      TTEntityJava actual = new TTEntityJava();
      actual.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CONTAINED_IN), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.QueryVocab.toString(), "Query"));
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void isInvalidIriAndData() throws ValidationException {
      TTEntityJava actual = new TTEntityJava();
      actual.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "foo"), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.QueryVocab.toString(), "Query"));
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void isValidIriAndInvalidData() throws ValidationException {
      TTEntityJava actual = new TTEntityJava();
      actual.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CONTAINED_IN), "foo");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void isInvalidIriAndInvalidData() throws ValidationException {
      TTEntityJava actual = new TTEntityJava();
      actual.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.IM + "foo"), "bar");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.HAS_PARENT).setEntity(actual);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity is missing a parent. Add a parent to 'Subset of', 'Subclass of' or 'Contained in'.");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }
  }

  @Nested
  class isValidIri {
    @Test
    void passesWithCorrectIri() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealth.info/im#903031000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsWithSpaces() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealth.info/im#90303 1000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri code contains invalid characters");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsWithMultipleSpecialCharacters() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealth.info/im#90303 10+00$25&21/04");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri code contains invalid characters");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsWithHashInIdentifier() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealth.info/im#9030310002521#04");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Entity IRI contains invalid character # within identifier");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfUrlMissingHash() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealth.info/im903031000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri URL is invalid");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfUrlIsWrongFormat() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealthinfo/im#903031000252104");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri URL is invalid");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfIriMissingCode() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.setIri("http://endhealthinfo/im#");
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_IRI).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Iri URL is invalid");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }
  }

  @Nested
  class isValidIriOrIriList {
    @Test
    void failsIfNoProperties() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), new TTArrayJava());
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("Data models must have at least 1 property");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithoutPath() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      ttArrayJava.add(new TTNodeJava());
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithPathWithoutRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTNodeJava().setIri("Some iri"));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithArrayPathWithoutRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithArrayMultiPathWithNodeRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")).add(new TTNodeJava().setIri("Some other iri")));
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void failsIfPropertyWithPathWithArrayMultiNodeRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTNodeJava().setIri("Some iri"));
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")).add(new TTNodeJava().setIri("Some other iri")));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(false).setMessage("One or more invalid properties");
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void passesIfPropertyWithPathWithNodeRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.NODE), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void passesIfPropertyWithPathWithClassRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    void passesIfPropertyWithPathWithDatatypeRange() throws ValidationException {
      TTEntityJava entity = new TTEntityJava();
      TTArrayJava ttArrayJava = new TTArrayJava();
      TTNodeJava ttNode = new TTNodeJava();
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttNode.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE), new TTArrayJava().add(new TTNodeJava().setIri("Some iri")));
      ttArrayJava.add(ttNode);
      entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), ttArrayJava);
      EntityValidationRequest request = new EntityValidationRequest().setValidationIri(ValidationVocab.IS_PROPERTY).setEntity(entity);
      EntityValidationResponse response = new EntityValidationResponse().setValid(true).setMessage(null);
      assertThat(entityValidator.validate(request, entityService)).usingRecursiveComparison().isEqualTo(response);
    }
  }
}

