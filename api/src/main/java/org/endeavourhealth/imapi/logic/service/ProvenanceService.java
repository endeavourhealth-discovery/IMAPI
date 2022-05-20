package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProvenanceService {

    public ProvAgent buildProvenanceAgent(TTEntity targetEntity, String agentName) {
        String uir = getPerson(agentName, targetEntity.getScheme());
        ProvAgent agent = new ProvAgent()
                .setPersonInRole(TTIriRef.iri(uir))
                .setParticipationType(IM.AUTHOR_ROLE);
        agent.setName(agentName).setIri(uir.replace("uir.", "agent."));
        return agent;
    }

    public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent) {
        return new ProvActivity()
                .setIri("urn:uuid:" + UUID.randomUUID())
                .setActivityType(IM.PROV_CREATION)
                .setEffectiveDate(LocalDateTime.now().toString())
                .addAgent(TTIriRef.iri(agent.getIri()))
                .setTargetEntity(TTIriRef.iri(targetEntity.getIri()));
    }

    private String getPerson(String name, TTIriRef scheme) {
        StringBuilder uri = new StringBuilder();
        name.chars().forEach(c -> {
            if (Character.isLetterOrDigit(c))
                uri.append(Character.toString(c));
        });
        String root = scheme.getIri();
        root = root.substring(0, root.lastIndexOf("#"));
        return root.replace("org.", "uir.") + "/personrole#" +
                uri;
    }
}
