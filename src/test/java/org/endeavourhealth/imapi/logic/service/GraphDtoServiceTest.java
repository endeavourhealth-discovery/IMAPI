package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GraphDtoServiceTest {

  @Mock
  EntityRepository entityRepository;
  @Mock
  DataModelRepository dataModelRepository;

  GraphDtoService graphDtoService;

  @BeforeEach
  void initMocks() {
    EntityService entityService = new EntityService(entityRepository);
    DataModelService dataModelService = new DataModelService(dataModelRepository, entityRepository);
    graphDtoService = new GraphDtoService(entityService, dataModelService);
  }

  @Test
  void getGraphData_NullIri() {
    GraphDto actual = graphDtoService.getGraphData(null);
    assertNotNull(actual);
  }

  @Test
  void getGraphData_NotNullEntity() {
    TTEntityJava entity = new TTEntityJava();
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));
    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_RoleGroup() {
    TTEntityJava entity = new TTEntityJava();
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_LeafNodes() {
    TTEntityJava entity = new TTEntityJava();
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));
    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_ParentIsList() {
    TTEntityJava entity = new TTEntityJava()
      .set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), new TTArrayJava()
        .add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://endhealth.info/im#parent1", "Parent 1"))
        .add(TTIriRefExtensionsKt.iri(new TTIriRef(), "http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }
}
