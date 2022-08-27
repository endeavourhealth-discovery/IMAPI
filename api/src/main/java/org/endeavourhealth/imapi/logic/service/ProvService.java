package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProvService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProvAgent buildProvenanceAgent(TTEntity targetEntity, String agentName) {
        String root;

        if (targetEntity.getGraph() != null)
            root = targetEntity.getGraph().getIri();
        else if (targetEntity.getScheme().getIri() != null)
            root = targetEntity.getScheme().getIri();
        else
            root = IM.NAMESPACE;

        String uir = getPerson(agentName, root);
        ProvAgent agent = new ProvAgent()
                .setPersonInRole(TTIriRef.iri(uir))
                .setParticipationType(IM.AUTHOR_ROLE);
        agent.setName(agentName).setIri(uir.replace("uir.", "agent.")).setCrud(IM.ADD_QUADS);
        return agent;
    }

    public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent, String usedEntityIri) {
        ProvActivity activity = new ProvActivity()
                .setIri("urn:uuid:" + UUID.randomUUID())
                .setActivityType(IM.PROV_CREATION)
                .setEffectiveDate(LocalDateTime.now().toString())
                .addAgent(TTIriRef.iri(agent.getIri()))
                .setTargetEntity(TTIriRef.iri(targetEntity.getIri()));

        if (null != usedEntityIri) {
            activity.set(IM.PROV_USED, TTIriRef.iri(usedEntityIri));
        }

        activity.setCrud(IM.ADD_QUADS);
        return activity;
    }

    public TTEntity buildUsedEntity(TTEntity usedEntity) throws JsonProcessingException {
        return new TTEntity()
                .setIri(usedEntity.getIri() + "/" + (usedEntity.getVersion()))
                .set(IM.DEFINITION, new TTLiteral(objectMapper.writeValueAsString(usedEntity)))
                .setCrud(IM.ADD_QUADS);
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

}
