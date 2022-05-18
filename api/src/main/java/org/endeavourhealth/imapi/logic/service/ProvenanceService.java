package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.time.LocalDateTime;

public class ProvenanceService {

    public ProvAgent buildProvenanceAgent(String agentIri) {
        ProvAgent agent = new ProvAgent()
                .setPersonInRole(TTIriRef.iri(agentIri))
                .setParticipationType(IM.AUTHOR_ROLE);
        agent.setIri(agentIri);
        return agent;
    }

    public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent) {
        return new ProvActivity()
                .setIri("http://prov.endhealth.info/im#Q_RegisteredGMS")
                .setActivityType(IM.PROV_CREATION)
                .setEffectiveDate(LocalDateTime.now().toString())
                .addAgent(TTIriRef.iri(agent.getIri()))
                .setTargetEntity(TTIriRef.iri(targetEntity.getIri()));
    }
}
