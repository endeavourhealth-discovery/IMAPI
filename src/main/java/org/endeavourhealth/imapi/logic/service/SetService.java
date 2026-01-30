package org.endeavourhealth.imapi.logic.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetTextFileExporter;
import org.endeavourhealth.imapi.logic.reasoner.SetMemberGenerator;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.SetDiffObject;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.ECLQueryRequest;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArrayList;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;


@Slf4j
@Component
public class SetService {

  private final SetTextFileExporter setTextFileExporter = new SetTextFileExporter();
  private final SetRepository setRepository;
  private final EntityRepository entityRepository = new EntityRepository();
  private final FilerService filerService = new FilerService();
  private final SetExporter setExporter = new SetExporter();

  public SetService() {
    setRepository = new SetRepository();
  }

  SetService(SetRepository setRepository) {
    this.setRepository = setRepository;
  }


  private static void getContains(SetContent result, List<ValueSet.ValueSetExpansionContainsComponent> contains) {
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

  private static void getIncludes(SetOptions options, ValueSet.ConceptSetFilterComponent filter, List<ValueSet.ConceptSetFilterComponent> filters, ValueSet.ConceptSetComponent includeConcept, List<ValueSet.ConceptSetComponent> includes, SetContent result) {
    TTEntity entityDefinition = new EntityRepository().getEntityPredicates(options.getSetIri(), asHashSet(IM.DEFINITION)).getEntity();
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
  }

  private static SetDiffObject getSetDiffObject(Set<Concept> membersA, Set<Concept> membersB) {
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



  public Pageable<Node> getDirectOrEntailedMembersFromIri(String iri, boolean entailments, Integer pageNumber, Integer pageSize) {
    return setRepository.getMembers(iri, entailments, pageNumber, pageSize);
  }

  public Pageable<Node> getMembersFromQuery(EclSearchRequest request) throws QueryException {
    EclService eclService = new EclService();
    SearchResponse response= eclService.eclSearch(request);
    Pageable<Node> result = new Pageable<>();
    result.setTotalCount(response.getTotalCount());
    result.setCurrentPage(response.getPage());
    result.setPageSize(response.getCount());
    result.setResult(new ArrayList<>());
    if (response.getEntities()!=null) {
      for (SearchResultSummary entity : response.getEntities()) {
        Node node= new Node();
        node.setIri(entity.getIri());
        node.setName(entity.getName());
        node.setCode(entity.getCode());
        result.getResult().add(node);
      }
    }
    return result;
  }


  public SetContent getSetContent(SetOptions options) throws QueryException, JsonProcessingException {
    SetContent result = new SetContent();

    log.trace("Fetching metadata for {}...", options.getSetIri());
    TTEntity entity = new EntityRepository().getEntityPredicates(options.getSetIri(), asHashSet(RDFS.LABEL, RDFS.COMMENT, IM.HAS_STATUS, IM.VERSION, IM.DEFINITION)).getEntity();

    if (null != entity) {
      result.setName(entity.getName()).setDescription(entity.getDescription()).setVersion(entity.getVersion());

      if (null != entity.getStatus()) result.setStatus(entity.getStatus().getName());

      if (options.includeDefinition() && null != entity.get(iri(IM.DEFINITION))) {
        Query qryDef = entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
        ECLQueryRequest eclQueryRequest = new ECLQueryRequest();
        eclQueryRequest.setShowNames(true);
        eclQueryRequest.setQuery(qryDef);
        new IMQToECL().getECLFromQuery(eclQueryRequest);
        result.setSetDefinition(eclQueryRequest.getEcl());
      }
    }

    if (options.includeSubsets()) {
      log.trace("Fetching subsets...");
      result.setSubsets(setRepository.getSubsetIrisWithNames(options.getSetIri()).stream().map(TTIriRef::getIri).collect(Collectors.toSet()));
    }

    if (options.includeCore() || options.includeLegacy() || options.includeSubsets()) {
      log.trace("Expanding...");
      result.setConcepts(getExpandedSetMembers(options.getSetIri(), options.includeCore(), options.includeLegacy(), options.includeSubsets(), options.getSchemes(), options.getSubsumptions()));
    }

    return result;
  }

  public String getFHIRSetExport(SetOptions options) throws QueryException, JsonProcessingException {
    log.debug("Exporting set to FHIR ValueSet");
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

      getIncludes(options, filter, filters, includeConcept, includes, result);
      getContains(result, contains);
    }
    compose.setInclude(includes);
    valueSet.setCompose(compose);
    expansion.setContains(contains);
    valueSet.setExpansion(expansion);

    FhirContext ctx = FhirContext.forR4();
    IParser parser = ctx.newJsonParser();
    return parser.encodeResourceToString(valueSet);
  }

  public Set<Concept> getFullyExpandedMembers(
    String iri,
    boolean includeLegacy,
    boolean includeSubset,
    List<String> schemes,
    List<String> subsumptions
  ) throws QueryException, JsonProcessingException {
    return getExpandedSetMembers(iri, true, includeLegacy, includeSubset, schemes, subsumptions);
  }

  public Set<TTIriRef> getSubsets(String iri) {
    return setRepository.getSubsetIrisWithNames(iri);
  }

  public List<TTIriRef> getDistillation(List<TTIriRef> conceptList) {
    List<String> iriList = conceptList.stream().map(c -> "<" + c.getIri() + ">").toList();
    String iris = String.join(" ", iriList);
    Set<String> isas = setRepository.getDistillation(iris);
    conceptList.removeIf(c -> isas.contains(c.getIri()));
    return conceptList;
  }

  private String getEcl(TTEntity entity) throws JsonProcessingException {
    if (entity.get(iri(IM.DEFINITION)) == null) return null;
    ECLQueryRequest eclQueryRequest = new ECLQueryRequest();
    eclQueryRequest.setQuery(entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class));
    eclQueryRequest.setShowNames(true);
    new IMQToECL().getECLFromQuery(eclQueryRequest);
    return eclQueryRequest.getEcl();
  }

  public byte[] getSetExport(String format, boolean includeIM1id, SetOptions options) throws IOException, QueryException, GeneralCustomException {
    if (null == options.getSetIri() || options.getSetIri().isEmpty())
      throw new IllegalArgumentException("Iri needs to be set.");

    if (null == format || format.isEmpty())
      throw new IllegalArgumentException("File type format needs to be set.");

    TTEntity setEntity = entityRepository.getBundle(options.getSetIri(), asHashSet(RDFS.LABEL, IM.DEFINITION)).getEntity();
    String ecl= null;

    if (options.includeDefinition()) {
       ecl = getEcl(setEntity);
    }

    LinkedHashSet<Concept> concepts = getExpandedSetMembers(options.getSetIri(), options.includeCore(), options.includeLegacy(), options.includeSubsets(), options.getSchemes(),
      options.getSubsumptions()).stream().sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));
    if (concepts.isEmpty()) {
      if (setEntity.get(iri(IM.DEFINITION)) != null) {
        new SetMemberGenerator().generateMembers(options.getSetIri(), Graph.IM);
        concepts = getExpandedSetMembers(options.getSetIri(), options.includeCore(), options.includeLegacy(), options.includeSubsets(),
          options.getSchemes(),
          options.getSubsumptions()).stream().sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));

      }
    }
    // throw new GeneralCustomException("Set does not have members.", HttpStatus.INTERNAL_SERVER_ERROR);

    switch (format) {
      case "xlsx", "csv", "tsv":
        return setTextFileExporter.generateFile(format, concepts, setEntity.getName(), includeIM1id, options.includeSubsets(), options.includeLegacy(),options.getSubsumptions(),ecl);
      case "object":
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
          SetContent result = getSetContent(options);
          objectOutputStream.writeObject(result);
          return outputStream.toByteArray();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      case "FHIR":
        return getFHIRSetExport(options).getBytes();
      default:
        throw new IllegalArgumentException("Unrecognised file type.");
    }
  }

  public Set<Concept> getExpandedSetMembers(
    String iri,
    boolean core,
    boolean legacy,
    boolean subsets,
    List<String> schemes,
    List<String> subsumption
  ) throws QueryException, JsonProcessingException {
    if (!(core || legacy || subsets)) return new HashSet<>();
    boolean hasMembers = entityRepository.hasPredicates(iri, asHashSet(IM.HAS_MEMBER));
    if (!hasMembers && (entityRepository.hasPredicates(iri, asHashSet(IM.DEFINITION)))) {
        new SetMemberGenerator().generateMembers(iri, Graph.IM);
    }

    Set<Concept> result = null;

    if (core || legacy) {
      result = setRepository.getExpansionFromIri(iri, legacy, schemes, subsumption);
    }

    if (null == result) result = new HashSet<>();

    if (subsets) {
      expandSubsets(iri, core, legacy, schemes, result, subsumption);
      result = result.stream().sorted(Comparator.comparing(m -> (null == m.getIsContainedIn() || m.getIsContainedIn().isEmpty()) ? "" : m.getIsContainedIn().iterator().next().getName())).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    return result;
  }

  private void expandSubsets(String iri, boolean core, boolean legacy, List<String> schemes, Set<Concept> result,
                             List<String> subsumptions) throws QueryException, JsonProcessingException {
    log.trace("Expanding subsets for {}...", iri);
    Set<TTIriRef> subSetIris = setRepository.getSubsetIrisWithNames(iri);
    log.trace("Found {} subsets...", subSetIris.size());
    for (TTIriRef subset : subSetIris) {
      Set<Concept> subsetMembers = getExpandedSetMembers(subset.getIri(), core, legacy, true, schemes, subsumptions);
      if (null != subsetMembers && !subsetMembers.isEmpty()) {
        TTEntity subsetEntity = new TTEntity(subset.getIri()).setName(subset.getName());
        subsetMembers.forEach(ss -> {
          ss.addIsContainedIn(subsetEntity);
          if (ss.getMatchedFrom() != null)
            ss.getMatchedFrom().forEach(matchedFrom -> matchedFrom.addIsContainedIn(subsetEntity));
        });
        result.addAll(subsetMembers);
      }
    }
  }

  public void updateSubsetsFromSuper(String agentName, TTEntity entity, Graph updateGraph) throws TTFilerException, JsonProcessingException {
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

  public SetDiffObject getSetComparison(Optional<String> setIriA, Optional<String> setIriB) throws QueryException, JsonProcessingException {
    if (setIriA.isEmpty() && setIriB.isEmpty()) {
      throw new IllegalArgumentException("One of SetIriA or SetIriB are required");
    }
    Set<Concept> membersA = null;
    Set<Concept> membersB = null;
    if (setIriA.isPresent() && !setIriA.get().isEmpty()) {
      membersA = getFullyExpandedMembers(setIriA.get(), false, false, null, new ArrayList<>());
    }
    if (setIriB.isPresent() && !setIriB.get().isEmpty()) {
      membersB = getFullyExpandedMembers(setIriB.get(), false, false, null, new ArrayList<>());
    }
    return getSetDiffObject(membersA, membersB);
  }

  public void publishSetToIM1(String iri) throws QueryException, JsonProcessingException {
    log.trace("Looking up set...");
    String name = entityRepository.getBundle(iri, asHashSet(RDFS.LABEL)).getEntity().getName();
    Set<Concept> members = getExpandedSetMembers(iri, true, true, true, List.of(), asArrayList(IM.SUBSUMED_BY));
    setExporter.publishSetToIM1(iri, name, members);
  }
}

