package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.library.model.imq.QueryException;
import org.endeavourhealth.library.model.tripletree.TTArray;
import org.endeavourhealth.library.model.tripletree.TTDocument;
import org.endeavourhealth.library.model.tripletree.TTEntity;
import org.endeavourhealth.library.model.tripletree.TTValue;
import org.endeavourhealth.library.transforms.TTManager;
import org.endeavourhealth.library.vocabulary.GRAPH;
import org.endeavourhealth.library.vocabulary.IM;
import org.endeavourhealth.library.vocabulary.RDF;

import java.util.*;

import static org.endeavourhealth.library.model.tripletree.TTIriRef.iri;


@Slf4j
public class SemanticMapGenerator {
  private final EntityRepository entityRepository = new EntityRepository();
  private final QueryRepository queryRepository = new QueryRepository();
  private final SetRepository setRepo = new SetRepository();

  public void generateAllSemanticMaps(GRAPH insertGraph) throws TTFilerException, JsonProcessingException, QueryException {

    log.info("Getting semanticMaps ...");
    Map<String, TTEntity> iriToMember= new HashMap<>();
    Map<String, Map<String, String>> mapEntries = queryRepository.getSemanticMaps();
    try (TTManager manager = new TTManager()) {
      TTDocument document = manager.createDocument();
      for (String mapIri : mapEntries.keySet()) {
        Map<String, String> mapEntry = mapEntries.get(mapIri);
        for (String sourceEntityIri : mapEntry.keySet()) {
          String sourceType = mapEntry.get(sourceEntityIri);
          addSemanticMap(mapIri, sourceEntityIri, sourceType, document,iriToMember);
        }
      }
      if (document.getEntities()!=null) {
        if (!document.getEntities().isEmpty()) {
          try (TTTransactionFiler filer = new TTTransactionFiler(insertGraph)) {
            filer.fileDocument(document);
          }
        }
      }
    }


  }



  public void updateSemanticMap( TTEntity map, GRAPH insertGraph) throws QueryException, JsonProcessingException, TTFilerException {

    TTDocument document = new TTDocument();
    TTArray mapEntries = map.get(IM.HAS_MAP_ENTRY);
    Map<String,TTEntity> iriToMember= new HashMap<>();
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
    if (document.getEntities()!=null) {
      if (!document.getEntities().isEmpty()) {
        try (TTTransactionFiler filer = new TTTransactionFiler(insertGraph)) {
          filer.fileDocument(document);
        }
      }
    }


  }

  private void addSemanticMap(String mapEntryIri,String sourceIri,String sourceType,TTDocument document,Map<String,TTEntity> iriToMember) {
    if (sourceType.equals(IM.CONCEPT_SET.toString())) {
      TTEntity sourceEntity= entityRepository.getEntityPredicates(sourceIri, Set.of(RDF.TYPE.toString(), IM.HAS_MEMBER.toString())).getEntity();
      for (TTValue member : sourceEntity.get(IM.HAS_MEMBER).getElements()) {
        TTEntity memberEntity = iriToMember.get(member.asIriRef().getIri());
        if (memberEntity == null) {
          memberEntity = new TTEntity();
          iriToMember.put(member.asIriRef().getIri(), memberEntity);
          memberEntity.setIri(member.asIriRef().getIri());
          memberEntity.setCrud(iri(IM.UPDATE_PREDICATES));
          document.addEntity(memberEntity);
        }
        memberEntity.addObject(iri(IM.HAS_SEMANTIC_MAP), iri(mapEntryIri));
      }
    } else {
      Set<String> subTypes= entityRepository.getIsAs(sourceIri);
      for (String subType : subTypes) {
        TTEntity subEntity= iriToMember.get(subType);
        if (subEntity==null) {
          subEntity = new TTEntity();
          iriToMember.put(subType,subEntity);
          subEntity.setIri(subType);
          subEntity.setCrud(iri(IM.UPDATE_PREDICATES));
          document.addEntity(subEntity);
        }
        subEntity.addObject(iri(IM.HAS_SEMANTIC_MAP), iri(mapEntryIri));

        }

    }



  }

}
