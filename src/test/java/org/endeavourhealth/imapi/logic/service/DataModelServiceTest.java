package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.interfacemanager.model.DataModelProperty;
import org.endeavourhealth.imapi.model.tripletree.*;
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
      .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), new TTArray().add(new TTNode()
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INHERITED_FROM), TTIriRefExtensionsKt.iri(new TTIriRef(), ))
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PATH), TTIriRefExtensionsKt.iri(new TTIriRef(), ))
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.CLASS), TTIriRefExtensionsKt.iri(new TTIriRef(), ))
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.DATATYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), ))
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MAXCOUNT), new TTLiteral())
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.MINCOUNT), new TTLiteral())
      )));
    assertNotNull(actual);
  }

  @Test
  void getDataModelProperties_NotInheritedFrom() {
    List<DataModelProperty> actual = dataModelService.getDataModelProperties(new TTEntity()
      .setIri("http://endhealth.info/im#25451000252115")
      .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.PROPERTY), new TTArray().add(new TTNode()))
    );
    assertNotNull(actual);
  }
}