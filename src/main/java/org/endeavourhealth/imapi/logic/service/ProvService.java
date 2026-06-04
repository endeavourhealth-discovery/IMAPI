package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.ProvRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
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
      root = NamespaceVocab.
    ImVocab.
    toString();

    String uir = getPerson(agentName, root);
    ProvAgent agent = new ProvAgent()
      .setPersonInRole(new TTIriRefExtended(uir))
      .setParticipationType(new TTIriRefExtended(ImVocab. AUTHOR_ROLE));
    agent.setName(agentName).setIri(uir.replace("uir.", "agent.")).setCrud(new TTIriRefExtended(ImVocab. ADD_QUADS));
    return agent;
  }

  public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent, String usedEntityIri) {
    ProvActivity activity = new ProvActivity()
      .setIri("urn:uuid:" + UUID.randomUUID())
      .setActivityType(new TTIriRefExtended(ImVocab. PROV_CREATION))
      .setEffectiveDate(LocalDateTime.now().toString())
      .addAgent(new TTIriRefExtended(agent.getIri()))
      .setTargetEntity(new TTIriRefExtended(targetEntity.getIri()));

    if (null != usedEntityIri) {
      activity.setActivityType(new TTIriRefExtended(ImVocab. PROV_UPDATE));
      activity.set(new TTIriRefExtended(ImVocab. PROVENANCE_USED),new TTIriRefExtended(usedEntityIri));
    }

    activity.setCrud(new TTIriRefExtended(ImVocab. ADD_QUADS));
    return activity;
  }

  public TTEntity buildUsedEntity(TTEntity usedEntity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return new TTEntity()
        .setIri(usedEntity.getIri() + "/" + (usedEntity.getVersion()))
        .setName(usedEntity.getName())
        .set(new TTIriRefExtended(ImVocab. DEFINITION),new TTLiteral(om.writeValueAsString(usedEntity)))
        .setCrud(new TTIriRefExtended(ImVocab. ADD_QUADS));
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
