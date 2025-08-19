package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.ProvRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class ProvService {

  ProvRepository provRepository = new ProvRepository();

  public ProvAgent buildProvenanceAgent(TTEntity targetEntity, String agentName) {
    String root;

    if (null != targetEntity.getScheme() && null != targetEntity.getScheme().getIri())
      root = targetEntity.getScheme().getIri();
    else
      root = Namespace.IM.toString();

    String uir = getPerson(agentName, root);
    ProvAgent agent = new ProvAgent()
      .setPersonInRole(TTIriRef.iri(uir))
      .setParticipationType(iri(IM.AUTHOR_ROLE));
    agent.setName(agentName).setIri(uir.replace("uir.", "agent.")).setCrud(iri(IM.ADD_QUADS));
    return agent;
  }

  public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent, String usedEntityIri) {
    ProvActivity activity = new ProvActivity()
      .setIri("urn:uuid:" + UUID.randomUUID())
      .setActivityType(iri(IM.PROV_CREATION))
      .setEffectiveDate(LocalDateTime.now().toString())
      .addAgent(TTIriRef.iri(agent.getIri()))
      .setTargetEntity(TTIriRef.iri(targetEntity.getIri()));

    if (null != usedEntityIri) {
      activity.setActivityType(iri(IM.PROV_UPDATE));
      activity.set(iri(IM.PROVENANCE_USED), TTIriRef.iri(usedEntityIri));
    }

    activity.setCrud(iri(IM.ADD_QUADS));
    return activity;
  }

  public TTEntity buildUsedEntity(TTEntity usedEntity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return new TTEntity()
        .setIri(usedEntity.getIri() + "/" + (usedEntity.getVersion()))
        .setName(usedEntity.getName())
        .set(iri(IM.DEFINITION), new TTLiteral(om.writeValueAsString(usedEntity)))
        .setCrud(iri(IM.ADD_QUADS));
    }
  }

  private String getPerson(String name, String root) {
    StringBuilder uri = new StringBuilder();
    name.chars().forEach(c -> {
      if (Character.isLetterOrDigit(c))
        uri.append(Character.toString(c));
    });
    root = root.substring(0, root.lastIndexOf("#"));
    return root.replace("org.", "uir.") + "/personrole#" +
      uri;
  }

  public List<TTEntity> getProvHistory(String iri) {
    return provRepository.getProvHistory(iri);
  }
}
