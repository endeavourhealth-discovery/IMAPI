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

  public void updateAllSemanticMaps() throws TTFilerException, JsonProcessingException, QueryException {

    log.info("Getting semanticMaps ...");
    Map<String, TTEntity> conceptMap = new HashMap<>();
    Map<String, Set<String>> sourceToMap = queryRepository.getSemanticMaps();
    setRepository.deleteSemanticMaps(GRAPH.IM);
    setRepository.updateSemanticMaps(getMappedConcepts(sourceToMap), GRAPH.IM);
  }



  private void addSetMaps(TTEntity sourceEntity, TTEntity mappedEntity, Map<String,Set<String>> sourceToMap, Set<TTEntity> mappedConcepts) {
    if (sourceEntity.get(IM.HAS_MEMBER) !=null) {
      for (TTValue member : sourceEntity.get(IM.HAS_MEMBER).getElements()) {;
          TTEntity memberEntity = new TTEntity()
            .setIri(member.asIriRef().getIri())
            .setCrud(iri(IM.ADD_QUADS));
          mappedConcepts.add(memberEntity);
          for (String mapEntry : sourceToMap.get(mappedEntity.getIri())) {
            memberEntity.addObject(iri(IM.HAS_MAP_ENTRY), iri(mapEntry));
          }
        }
    }
    else {
      Set<TTIriRef> subSetIris = setRepository.getSubsetIrisWithNames(sourceEntity.getIri());
      for (TTIriRef subsetIri : subSetIris) {
        TTEntity subsetEntity = entityRepository.getEntityPredicates(subsetIri.getIri(), Set.of(RDF.TYPE.toString(), IM.HAS_MEMBER.toString())).getEntity();
        addSetMaps(subsetEntity, mappedEntity,sourceToMap, mappedConcepts);
      }
    }
  }


  private Set<TTEntity> getMappedConcepts(Map<String, Set<String>> sourceToMap) {
    log.info("getting set members and children from map sources");
   Set<TTEntity> mappedConcepts = new HashSet<>();
    Set<TTEntity> sourceEntities= entityRepository.getEntities(sourceToMap.keySet(),Set.of(RDF.TYPE.toString(),IM.HAS_MEMBER.toString()));
    for (TTEntity sourceEntity : sourceEntities) {
      if (sourceEntity.isType(iri(IM.CONCEPT))) {
        Set<String> isas = entityRepository.getIsAs(sourceEntity.getIri());
        for (String isa : isas) {
          TTEntity subEntity = new TTEntity()
            .setIri(isa)
            .setCrud(iri(IM.ADD_QUADS));
          mappedConcepts.add(subEntity);
          for (String map : sourceToMap.get(sourceEntity.getIri())) {
            subEntity.addObject(iri(IM.HAS_MAP_ENTRY), iri(map));
          }
        }
      } else if (sourceEntity.isType(iri(IM.CONCEPT_SET))) {
        addSetMaps(sourceEntity, sourceEntity,sourceToMap,mappedConcepts);

      }
    }
    return mappedConcepts;
    }
}
