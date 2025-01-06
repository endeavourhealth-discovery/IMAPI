package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.model.DownloadEntityOptions;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class DownloadService {
  private static final Logger LOG = LoggerFactory.getLogger(DownloadService.class);

  private EntityService entityService = new EntityService();
  private DataModelService dataModelService = new DataModelService();
  private ConceptService conceptService = new ConceptService();

  public DownloadDto getJsonDownload(String iri, List<String> configs, DownloadEntityOptions params) {
    LOG.info("getJsonDownload {}", iri);
    if (iri == null || iri.isEmpty()) return null;

    DownloadDto downloadDto = new DownloadDto();

    downloadDto.setSummary(entityService.getSummaryFromConfig(iri, configs));

    if (params.includeHasSubtypes()) {
      downloadDto.setHasSubTypes(entityService.getImmediateChildren(iri, null, null, null, params.includeInactive()));
    }
    if (params.includeInferred()) {
      downloadDto.setInferred(entityService.getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP))).getEntity());
    }
    if (params.includeProperties()) {
      downloadDto.setDataModelProperties(dataModelService.getDataModelProperties(iri));
    }
    if (params.includeTerms()) {
      downloadDto.setTerms(conceptService.getEntityTermCodes(iri, false));
    }
    if (params.includeIsChildOf()) {
      downloadDto.setIsChildOf(entityService.getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF))).getEntity().get(iri(IM.IS_CHILD_OF)));
    }
    if (params.includeHasChildren()) {
      downloadDto.setHasChildren(entityService.getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN))).getEntity().get(iri(IM.HAS_CHILDREN)));
    }

    return downloadDto;
  }

  public XlsHelper getExcelDownload(String iri, List<String> configs, DownloadEntityOptions params) {
    LOG.info("getExcelDownload {}", iri);
    if (iri == null || iri.isEmpty()) return null;

    XlsHelper xls = new XlsHelper();

    xls.addSummary(entityService.getSummaryFromConfig(iri, configs));

    if (params.includeHasSubtypes()) {
      xls.addHasSubTypes(entityService.getImmediateChildren(iri, null, null, null, params.includeInactive()));
    }
    if (params.includeInferred()) {
      xls.addInferred(entityService.getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP))));
    }
    if (params.includeProperties()) {
      xls.addDataModelProperties(dataModelService.getDataModelProperties(iri));
    }
    if (params.includeTerms()) {
      xls.addTerms(conceptService.getEntityTermCodes(iri, false));
    }
    TTEntity isChildOfEntity = entityService.getBundle(iri, new HashSet<>(List.of(IM.IS_CHILD_OF))).getEntity();
    TTArray isChildOfData = isChildOfEntity.get(iri(IM.IS_CHILD_OF));
    if (params.includeIsChildOf() && isChildOfData != null) {
      xls.addIsChildOf(isChildOfData.getElements());
    }
    TTEntity hasChildrenEntity = entityService.getBundle(iri, new HashSet<>(List.of(IM.HAS_CHILDREN))).getEntity();
    TTArray hasChildrenData = hasChildrenEntity.get(iri(IM.HAS_CHILDREN));
    if (params.includeHasChildren() && hasChildrenData != null) {
      xls.addHasChildren(hasChildrenData.getElements());
    }

    return xls;
  }
}
