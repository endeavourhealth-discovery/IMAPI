package org.endeavourhealth.imapi.logic.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

  public String getFHIRSetExport(SetExporterOptions options) throws QueryException, JsonProcessingException {
    LOG.debug("Exporting set to FHIR ValueSet");
    SetContent result = getSetContent(options);

    ValueSet valueSet = new ValueSet();
    ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
    ValueSet.ConceptSetFilterComponent filter = new ValueSet.ConceptSetFilterComponent();
    ValueSet.ValueSetComposeComponent compose = new ValueSet.ValueSetComposeComponent();
    ValueSet.ConceptSetComponent includeConcept = new ValueSet.ConceptSetComponent();
    List<ValueSet.ValueSetExpansionContainsComponent> contains = new ArrayList<>();
    List<ValueSet.ConceptSetFilterComponent> filters = new ArrayList<>();
    List<ValueSet.ConceptSetComponent> includes = new ArrayList<>();

    valueSet.setLanguage("en");
    valueSet.setUrl(options.getSetIri());
    if (null != result) {
      valueSet.setVersion(String.valueOf(result.getVersion()));
      valueSet.setName(result.getName());
      valueSet.setTitle(result.getName());
      valueSet.setDescription(result.getDescription());
      if (null != result.getStatus()) {
        valueSet.setStatus(Enumerations.PublicationStatus.valueOf(result.getStatus().toUpperCase()));
      }
      valueSet.setVersion(String.valueOf(result.getVersion()));

      if (options.includeDefinition()) {
        TTEntity entityDefinition = new EntityTripleRepository().getEntityPredicates(options.getSetIri(), Set.of(IM.DEFINITION)).getEntity();
        filter.setValue(entityDefinition.get(iri(IM.DEFINITION)).asLiteral().getValue());
        filters.add(filter);
        includeConcept.setFilter(filters);
        includes.add(includeConcept);
      }

      if (!result.getSubsets().isEmpty()) {
        List<CanonicalType> subsetList = new ArrayList<>();
        ValueSet.ConceptSetComponent subsetConcept = new ValueSet.ConceptSetComponent();
        for (String s : result.getSubsets()) {
          CanonicalType convertedSubset = new CanonicalType(s);
          subsetList.add(convertedSubset);
        }
        subsetConcept.setValueSet(subsetList);
        includes.add(subsetConcept);
      }

      if (!result.getConcepts().isEmpty()) {
        for (Concept c : result.getConcepts()) {
          ValueSet.ValueSetExpansionContainsComponent subContains = new ValueSet.ValueSetExpansionContainsComponent();
          subContains.setId(c.getCodeId());
          subContains.setDisplay(c.getName());
          subContains.setCode(c.getCode());
          subContains.setSystem(c.getScheme().getIri());
          contains.add(subContains);
        }
      }
    }

    compose.setInclude(includes);
    valueSet.setCompose(compose);
    expansion.setContains(contains);
    valueSet.setExpansion(expansion);

    FhirContext ctx = FhirContext.forR4();
    IParser parser = ctx.newJsonParser();

    return parser.encodeResourceToString(valueSet);
  }
}

