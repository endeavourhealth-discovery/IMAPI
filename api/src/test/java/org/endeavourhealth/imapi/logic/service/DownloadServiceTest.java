package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.model.DownloadEntityOptions;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class DownloadServiceTest {
  @InjectMocks
  DownloadService downloadService = new DownloadService();

  @InjectMocks
  EntityService entityService = spy(EntityService.class);

  @InjectMocks
  ConceptService conceptService = spy(ConceptService.class);

  @InjectMocks
  DataModelService dataModelService = spy(DataModelService.class);

  @Mock
  EntityRepository entityRepository;

  @Mock
  ConceptRepository conceptRepository;

  @Mock
  DataModelRepository dataModelRepository;

  @Mock
  ConfigManager configManager = new ConfigManager();

  @Test
  void getJsonDownload_NullIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadEntityOptions params = new DownloadEntityOptions();
    DownloadDto actual = downloadService.getJsonDownload(null, configs, params);
    assertNull(actual);
  }

  @Test
  void getJsonDownload_EmptyIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadEntityOptions params = new DownloadEntityOptions();
    DownloadDto actual = downloadService.getJsonDownload("", configs, params);
    assertNull(actual);
  }

  @Test
  void getJsonDownload_OnlySummary() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    TTEntity entity = new TTEntity("http://endhealth.info/im#25451000252115")
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    DownloadEntityOptions params = new DownloadEntityOptions();

    DownloadDto actual = downloadService.getJsonDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);
  }

  @Test
  void getJsonDownload_All() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    TTEntity entity = new TTEntity()
      .setIri("http://endhealth.info/im#myConcept")
      .setName("My concept")
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    DownloadEntityOptions params = new DownloadEntityOptions();
    params
      .setIncludeInactive(true)
      .setIncludeTerms(true)
      .setIncludeIsChildOf(true)
      .setIncludeHasChildren(true)
      .setIncludeInferred(true)
      .setIncludeMembers(true)
      .setExpandMembers(true)
      .setExpandSubsets(true)
      .setIncludeHasSubtypes(true)
      .setIncludeProperties(true);

    DownloadDto actual = downloadService.getJsonDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);
  }

  @Test
  void getExcelDownload_NullIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadEntityOptions params = new DownloadEntityOptions();
    XlsHelper actual = downloadService.getExcelDownload(null, configs, params);
    assertNull(actual);

  }

  @Test
  void getExcelDownload_EmptyIri() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    DownloadEntityOptions params = new DownloadEntityOptions();
    XlsHelper actual = downloadService.getExcelDownload("", configs, params);
    assertNull(actual);

  }

  @Test
  void getExcelDownload_SummaryOnly() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    DownloadEntityOptions params = new DownloadEntityOptions();

    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );

    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    XlsHelper actual = downloadService.getExcelDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);

  }

  @Test
  void getExcelDownload_All() {
    List<ComponentLayoutItem> configs = new ArrayList<>();
    configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF));

    DownloadEntityOptions params = new DownloadEntityOptions();
    params
      .setIncludeInactive(true)
      .setIncludeTerms(true)
      .setIncludeIsChildOf(true)
      .setIncludeHasChildren(true)
      .setIncludeInferred(true)
      .setIncludeMembers(true)
      .setExpandMembers(true)
      .setExpandSubsets(true)
      .setIncludeHasSubtypes(true)
      .setIncludeProperties(true);

    TTEntity entity = new TTEntity()
      .set(TTIriRef.iri(IM.IS_CHILD_OF), new TTArray()
        .add(iri("http://endhealth.info/im#parent1", "Parent 1"))
        .add(iri("http://endhealth.info/im#parent2", "Parent 2"))
      );
    when(entityRepository.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(entity));

    XlsHelper actual = downloadService.getExcelDownload("http://endhealth.info/im#25451000252115", configs, params);
    assertNotNull(actual);
  }
}
