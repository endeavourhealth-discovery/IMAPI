package org.endeavourhealth.imapi.logic.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.exporters.ExcelSetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.model.SetDiffObject;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Entity;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


@Component
public class SetService {
  private static final Logger LOG = LoggerFactory.getLogger(SetService.class);

  private final SetTextFileExporter setTextFileExporter = new SetTextFileExporter();
  private SetRepository setRepository = new SetRepository();
  private EntityRepository entityRepository = new EntityRepository();
  private FilerService filerService = new FilerService();

  public String getTSVSetExport(SetExporterOptions options) throws QueryException, JsonProcessingException {
    return setTextFileExporter.getSetFile(options, "\t");

  }

  public String getCSVSetExport(SetExporterOptions options) throws QueryException, JsonProcessingException {
    return setTextFileExporter.getSetFile(options, ",");

  }

  public SetContent getSetContent(SetOptions options) throws QueryException, JsonProcessingException {
    SetContent result = new SetContent();

    LOG.trace("Fetching metadata for {}...", options.getSetIri());
    TTEntity entity = new EntityRepository().getEntityPredicates(options.getSetIri(), Set.of(RDFS.LABEL, RDFS.COMMENT, IM.HAS_STATUS, IM.VERSION, IM.DEFINITION)).getEntity();

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
      if (null != result.getStatus())
        valueSet.setStatus(Enumerations.PublicationStatus.valueOf(result.getStatus().toUpperCase()));
      valueSet.setVersion(String.valueOf(result.getVersion()));

      TTEntity entityDefinition = new EntityRepository().getEntityPredicates(options.getSetIri(), Set.of(IM.DEFINITION)).getEntity();
      if (null != entityDefinition.get(iri(IM.DEFINITION))) {
        filter.setValue(entityDefinition.get(iri(IM.DEFINITION)).asLiteral().getValue());
        filters.add(filter);
        includeConcept.setFilter(filters);
        includes.add(includeConcept);
      }

      if (null != result.getSubsets() && !result.getSubsets().isEmpty()) {
        List<CanonicalType> subsetList = new ArrayList<>();
        ValueSet.ConceptSetComponent subsetConcept = new ValueSet.ConceptSetComponent();
        for (String s : result.getSubsets()) {
          CanonicalType convertedSubset = new CanonicalType(s);
          subsetList.add(convertedSubset);
        }
        subsetConcept.setValueSet(subsetList);
        includes.add(subsetConcept);
      }

      if (null != result.getConcepts() && !result.getConcepts().isEmpty()) {
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

  public Set<Concept> getFullyExpandedMembers(String iri, boolean includeLegacy, boolean includeSubset, List<String> schemes) throws QueryException, JsonProcessingException {
    SetExporter setExporter = new SetExporter();
    return setExporter.getExpandedSetMembers(iri, true, includeLegacy, includeSubset, schemes);
  }

  public Set<TTIriRef> getSubsets(String iri) {
    SetExporter setExporter = new SetExporter();
    return setExporter.getSubsetIrisWithNames(iri);
  }

  public List<TTIriRef> getDistillation(List<TTIriRef> conceptList) {
    List<String> iriList = conceptList.stream().map(c -> "<" + c.getIri() + ">").toList();
    String iris = String.join(" ", iriList);
    Set<String> isas = setRepository.getDistillation(iris);
    conceptList.removeIf(c -> isas.contains(c.getIri()));
    return conceptList;
  }


  public XSSFWorkbook getSetExport(SetExporterOptions options) throws JsonProcessingException, QueryException {
    if (options.getSetIri() == null || options.getSetIri().isEmpty()) {
      throw new IllegalArgumentException("SetIri is required");
    }
    return new ExcelSetExporter().getSetAsExcel(options);
  }

  public void updateSubsetsFromSuper(String agentName, TTEntity entity) throws TTFilerException, JsonProcessingException {
    TTArray subsets = entity.get(iri(IM.HAS_SUBSET));
    String entityIri = entity.getIri();
    Set<TTIriRef> subsetsOriginal = getSubsets(entityIri);
    List<TTIriRef> subsetsArray = subsets.stream().map(TTValue::asIriRef).toList();
    for (TTIriRef subset : subsetsArray) {
      TTEntity subsetEntity = entityRepository.getBundle(subset.getIri()).getEntity();
      if (null != subsetEntity) {
        if (!(subsetEntity.isType(iri(IM.VALUESET)) || subsetEntity.isType(iri(IM.CONCEPT_SET))))
          throw new TTFilerException("Subsets must be of type valueSet or conceptSet. Type: " + subsetEntity.getType());
        TTArray isSubsetOf = subsetEntity.get(iri(IM.IS_SUBSET_OF));
        if (null == isSubsetOf) {
          subsetEntity.set(iri(IM.IS_SUBSET_OF), new TTArray().add(iri(entityIri)));
          filerService.updateEntity(subsetEntity, agentName);
        } else if (isSubsetOf.getElements().stream().noneMatch(i -> Objects.equals(i.asIriRef().getIri(), entityIri))) {
          isSubsetOf.add(iri(entityIri));
          filerService.updateEntity(subsetEntity, agentName);
        }
      }
    }
    for (TTIriRef subsetOriginal : subsetsOriginal) {
      if (subsetsArray.stream().noneMatch(s -> s.getIri().equals(subsetOriginal.getIri()))) {
        TTEntity subsetEntity = entityRepository.getBundle(subsetOriginal.getIri()).getEntity();
        TTArray isSubsetOf = subsetEntity.get(iri(IM.IS_SUBSET_OF));
        isSubsetOf.remove(iri(entityIri));
        filerService.updateEntity(subsetEntity, agentName);
      }
    }
  }

  public SetDiffObject getSetComparison(String setIriA, String setIriB) throws QueryException, JsonProcessingException {
    if (setIriA.isEmpty() && setIriB.isEmpty()) {
      throw new IllegalArgumentException("One of SetIriA or SetIriB are required");
    }
    Set<Concept> membersA = null;
    Set<Concept> membersB = null;
    if (!setIriA.isEmpty()) {
      membersA = getFullyExpandedMembers(setIriA,false,false,null);
    }
    if (!setIriB.isEmpty()) {
      membersB = getFullyExpandedMembers(setIriB,false,false,null);
    }
    SetDiffObject setDiffObject = new SetDiffObject();
    Map<String, Concept> membersMap = new HashMap<>();
    if (membersA != null) {
      for (Concept concept : membersA) {
        concept.setName(concept.getName() + " | " + concept.getCode());
        membersMap.put(concept.getIri(), concept);
      }
    }
    if (membersB != null) {
      for (Concept concept : membersB) {
        concept.setName(concept.getName() + " | " + concept.getCode());
        if (membersMap.containsKey(concept.getIri())) {
          setDiffObject.addSharedMember(concept);
          membersMap.remove(concept.getIri());
        } else {
          setDiffObject.addMemberB(concept);
        }
      }
    }
    List<Concept> membersAList = new ArrayList<>(List.copyOf(membersMap.values()));
    membersAList.sort(Comparator.comparing(Entity::getName));
    setDiffObject.setMembersA(membersAList);
    setDiffObject.getMembersB().sort(Comparator.comparing(Entity::getName));
    setDiffObject.getSharedMembers().sort(Comparator.comparing(Entity::getName));
    return setDiffObject;
  }
}

