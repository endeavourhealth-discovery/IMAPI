package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.SHACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DataModelServiceTest {
  final DataModelService dataModelService = new DataModelService();

  @Test
  void getDataModelProperties_NullEntity() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties((TTEntity) null);
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotNullEntity() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(new TTIriRef(SHACL.PROPERTY), new TTArray().add(new TTNode()
        .set(new TTIriRef(IM.INHERITED_FROM), new TTIriRef())
        .set(new TTIriRef(SHACL.PATH), new TTIriRef())
        .set(new TTIriRef(SHACL.CLASS), new TTIriRef())
        .set(new TTIriRef(SHACL.DATATYPE), new TTIriRef())
        .set(new TTIriRef(SHACL.MAXCOUNT), new TTLiteral())
        .set(new TTIriRef(SHACL.MINCOUNT), new TTLiteral())
      )));
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotInheritedFrom() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(new TTIriRef(SHACL.PROPERTY), new TTArray().add(new TTNode()))
    );
    assertNotNull(actual);
  }
}