package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.ProvRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteralJava;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ProvService {

  final ProvRepository provRepository = new ProvRepository();

  public ProvAgent buildProvenanceAgent(TTEntityJava targetEntity, String agentName) {
    String root;

    if (null != targetEntity.getScheme() && null != targetEntity.getScheme().getIri())
      root = targetEntity.getScheme().getIri();
    else
      root = NamespaceVocab.
        ImVocab.
        toString();

    String uir = getPerson(agentName, root);
    ProvAgent agent = new ProvAgent()
      .setPersonInRole(TTIriRefExtensionsKt.iri(new TTIriRef(), uir))
      .setParticipationType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.AUTHOR_ROLE));
    agent.setName(agentName).setIri(uir.replace("uir.", "agent.")).setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ADD_QUADS));
    return agent;
  }

  public ProvActivity buildProvenanceActivity(TTEntityJava targetEntity, ProvAgent agent, String usedEntityIri) {
    ProvActivity activity = new ProvActivity()
      .setIri("urn:uuid:" + UUID.randomUUID())
      .setActivityType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROV_CREATION))
      .setEffectiveDate(LocalDateTime.now().toString())
      .addAgent(TTIriRefExtensionsKt.iri(new TTIriRef(), agent.getIri()))
      .setTargetEntity(TTIriRefExtensionsKt.iri(new TTIriRef(), targetEntity.getIri()));

    if (null != usedEntityIri) {
      activity.setActivityType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROV_UPDATE));
      activity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.PROVENANCE_USED), TTIriRefExtensionsKt.iri(new TTIriRef(), usedEntityIri));
    }

    activity.setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ADD_QUADS));
    return activity;
  }

  public TTEntityJava buildUsedEntity(TTEntityJava usedEntity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return new TTEntityJava()
        .setIri(usedEntity.getIri() + "/" + (usedEntity.getVersion()))
        .setName(usedEntity.getName())
        .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION), new TTLiteralJava(om.writeValueAsString(usedEntity)))
        .setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ADD_QUADS));
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

  public List<TTEntityJava> getProvHistory(String iri) {
    return provRepository.getProvHistory(iri);
  }
}
