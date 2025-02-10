package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DataModelServiceTest {
  @InjectMocks
  DataModelService dataModelService = new DataModelService();

  @Test
  void getDataModelProperties_NullEntity() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties((TTEntity) null);
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotNullEntity() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(SHACL.PROPERTY), new TTArray().add(new TTNode()
        .set(TTIriRef.iri(IM.INHERITED_FROM), new TTIriRef())
        .set(TTIriRef.iri(SHACL.PATH), new TTIriRef())
        .set(TTIriRef.iri(SHACL.CLASS), new TTIriRef())
        .set(TTIriRef.iri(SHACL.DATATYPE), new TTIriRef())
        .set(TTIriRef.iri(SHACL.MAXCOUNT), new TTLiteral())
        .set(TTIriRef.iri(SHACL.MINCOUNT), new TTLiteral())
      )));
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotInheritedFrom() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(SHACL.PROPERTY), new TTArray().add(new TTNode()))
    );
    assertNotNull(actual);
  }
}