package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GraphDtoServiceTest {

  @Mock EntityRepository entityRepository;
  @Mock DataModelRepository dataModelRepository;

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
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));
    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_RoleGroup() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_LeafNodes() {
    TTEntity entity = new TTEntity();
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));
    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }

  @Test
  void getGraphData_ParentIsList() {
    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(RDFS.SUBCLASS_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    GraphDto actual = graphDtoService.getGraphData("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }
}
