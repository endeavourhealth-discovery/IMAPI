package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.ProvRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ProvService {

  final ProvRepository provRepository = new ProvRepository();

  public ProvAgent buildProvenanceAgent(TTEntity targetEntity, String agentName) {
    String root;

    if (null != targetEntity.getScheme() && null != targetEntity.getScheme().getIri())
      root = targetEntity.getScheme().getIri();
    else
      root = NAMESPACE.IM.toString();

    String uir = getPerson(agentName, root);
    ProvAgent agent = new ProvAgent()
      .setPersonInRole(new TTIriRef(uir))
      .setParticipationType(new TTIriRef(IM.AUTHOR_ROLE));
    agent.setName(agentName).setIri(uir.replace("uir.", "agent.")).setCrud(new TTIriRef(IM.ADD_QUADS));
    return agent;
  }

  public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent, String usedEntityIri) {
    ProvActivity activity = new ProvActivity()
      .setIri("urn:uuid:" + UUID.randomUUID())
      .setActivityType(new TTIriRef(IM.PROV_CREATION))
      .setEffectiveDate(LocalDateTime.now().toString())
      .addAgent(new TTIriRef(agent.getIri()))
      .setTargetEntity(new TTIriRef(targetEntity.getIri()));

    if (null != usedEntityIri) {
      activity.setActivityType(new TTIriRef(IM.PROV_UPDATE));
      activity.set(new TTIriRef(IM.PROVENANCE_USED), new TTIriRef(usedEntityIri));
    }

    activity.setCrud(new TTIriRef(IM.ADD_QUADS));
    return activity;
  }

  public TTEntity buildUsedEntity(TTEntity usedEntity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return new TTEntity()
        .setIri(usedEntity.getIri() + "/" + (usedEntity.getVersion()))
        .setName(usedEntity.getName())
        .set(new TTIriRef(IM.DEFINITION), new TTLiteral(om.writeValueAsString(usedEntity)))
        .setCrud(new TTIriRef(IM.ADD_QUADS));
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
