package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


@Component
public class SetService {
  private static final Logger LOG = LoggerFactory.getLogger(SetService.class);

  private final SetTextFileExporter setTextFileExporter = new SetTextFileExporter();

  public String getTSVSetExport(SetExporterOptions options) throws QueryException, JsonProcessingException {
    return setTextFileExporter.getSetFile(options, "\t");

  }

  public String getCSVSetExport(SetExporterOptions options) throws QueryException, JsonProcessingException {
    return setTextFileExporter.getSetFile(options, ",");

  }

  public SetContent getSetContent(SetOptions options) throws QueryException, JsonProcessingException {
    SetContent result = new SetContent();

    LOG.trace("Fetching metadata for {}...", options.getSetIri());
    TTEntity entity = new EntityTripleRepository().getEntityPredicates(options.getSetIri(), Set.of(RDFS.LABEL, RDFS.COMMENT, IM.HAS_STATUS, IM.VERSION, IM.DEFINITION)).getEntity();

    if (null != entity) {
      result.setName(entity.getName())
        .setDescription(entity.getDescription())
        .setVersion(entity.getVersion());

      if (null != entity.getStatus())
        result.setStatus(entity.getStatus().getName());

      if (options.includeDefinition() && null != entity.get(iri(IM.DEFINITION))) {
        Query qryDef = entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
        result.setSetDefinition(new IMQToECL().getECLFromQuery(qryDef, true));
      }
    }

    SetExporter setExporter = new SetExporter();

    if (options.includeSubsets()) {
      LOG.trace("Fetching subsets...");
      result.setSubsets(setExporter.getSubsetIrisWithNames(options.getSetIri()).stream().map(TTIriRef::getIri).collect(Collectors.toSet()));
    }

    if (options.includeCore() || options.includeLegacy() || options.includeSubsets()) {
      LOG.trace("Expanding...");
      result.setConcepts(setExporter
        .getExpandedSetMembers(options.getSetIri(), options.includeCore(), options.includeLegacy(), options.includeSubsets(), options.getSchemes())
      );
    }

    return result;
  }
}
