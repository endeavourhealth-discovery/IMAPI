package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


@Slf4j
public class SemanticMapGenerator {
  private final EntityRepository entityRepository = new EntityRepository();
  private final QueryRepository queryRepository = new QueryRepository();
  private final SetRepository setRepository = new SetRepository();

  public void generateAllSemanticMaps(GRAPH insertGraph) throws TTFilerException, JsonProcessingException, QueryException {

    log.info("Getting semanticMaps ...");
    Map<String, TTEntity> iriToMember = new HashMap<>();
    Map<String, Set<Map<String, String>>> mapEntryToSource = queryRepository.getSemanticMaps();
    try (TTManager manager = new TTManager()) {
      TTDocument document = manager.createDocument();
      for (String mapIri : mapEntryToSource.keySet()) {
        for (Map<String, String> sourceEntity : mapEntryToSource.get(mapIri)) {
          for (String sourceEntityIri : sourceEntity.keySet()) {
            String sourceType = sourceEntity.get(sourceEntityIri);
            addSemanticMap(mapIri, sourceEntityIri, sourceType, document, iriToMember);
          }
        }
      }
      if (document.getEntities() != null) {
        if (!document.getEntities().isEmpty()) {
          try (TTTransactionFiler filer = new TTTransactionFiler(insertGraph)) {
            filer.fileDocument(document);
          }
        }
      }
    }


  }


  public void updateSemanticMap(TTEntity map, GRAPH insertGraph) throws QueryException, JsonProcessingException, TTFilerException {

    TTDocument document = new TTDocument();
    TTArray mapEntries = map.get(IM.HAS_MAP_ENTRY);
    Map<String, TTEntity> iriToMember = new HashMap<>();
    for (TTValue mapEntryIri : mapEntries.getElements()) {
      TTEntity mapEntryEntity = entityRepository.getBundle(mapEntryIri.asIriRef().getIri(), null).getEntity();
      if (mapEntryEntity.get(IM.SOURCE_ENTITY) != null) {
        String sourceEntityIri = mapEntryEntity.get(IM.SOURCE_ENTITY).asIriRef().getIri();
        TTEntity sourceEntity = entityRepository.getEntityPredicates(sourceEntityIri, Set.of(RDF.TYPE.toString())).getEntity();
        addSemanticMap(mapEntryIri.asIriRef().getIri(),
          sourceEntityIri,
          sourceEntity.getType().get(0).asIriRef().getIri(),
          document,
          iriToMember);
      }
    }
    if (document.getEntities() != null) {
      if (!document.getEntities().isEmpty()) {
        try (TTTransactionFiler filer = new TTTransactionFiler(insertGraph)) {
          filer.fileDocument(document);
        }
      }
    }


  }

  private void addSemanticMap(String mapEntryIri, String sourceIri, String sourceType, TTDocument document, Map<String, TTEntity> iriToMember) {
    TTEntity sourceEntity = entityRepository.getEntityPredicates(sourceIri, Set.of(RDF.TYPE.toString(), IM.HAS_MEMBER.toString())).getEntity();
    if (sourceType.equals(IM.CONCEPT_SET.toString())) {
      addSetMaps(sourceEntity, mapEntryIri, sourceIri, document, iriToMember);
    }
    else if (sourceEntity.isType(IM.CONCEPT.asIri())) {
        Set<String> isas= entityRepository.getIsAs(sourceEntity.getIri());
        for (String isa : isas) {
          TTEntity subEntity = new TTEntity()
            .setIri(isa)
            .setCrud(iri(IM.ADD_QUADS));
          document.addEntity(subEntity);
          subEntity.addObject(iri(IM.HAS_SEMANTIC_MAP), iri(mapEntryIri));
        }
      }
  }



  private void addSetMaps(TTEntity sourceEntity, String mapEntryIri, String sourceIri, TTDocument document, Map<String, TTEntity> iriToMember) {
    if (sourceEntity.get(IM.HAS_MEMBER) !=null) {
      for (TTValue member : sourceEntity.get(IM.HAS_MEMBER).getElements()) {
        TTEntity memberEntity = iriToMember.get(member.asIriRef().getIri());
        if (memberEntity == null) {
          memberEntity = new TTEntity();
          iriToMember.put(member.asIriRef().getIri(), memberEntity);
          memberEntity.setIri(member.asIriRef().getIri());
          memberEntity.setCrud(iri(IM.ADD_QUADS));
          document.addEntity(memberEntity);
        }
        memberEntity.addObject(iri(IM.HAS_SEMANTIC_MAP), iri(mapEntryIri));
      }
    }
    else {
      Set<TTIriRef> subSetIris = setRepository.getSubsetIrisWithNames(sourceEntity.getIri());
      for (TTIriRef subsetIri : subSetIris) {
        TTEntity subsetEntity = entityRepository.getEntityPredicates(subsetIri.getIri(), Set.of(RDF.TYPE.toString(), IM.HAS_MEMBER.toString())).getEntity();
        addSetMaps(subsetEntity, mapEntryIri, sourceIri, document, iriToMember);
      }
    }
  }
}
