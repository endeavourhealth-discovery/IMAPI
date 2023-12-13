package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.ProvRepository;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.cdm.ProvAgent;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ProvService {

    ProvRepository provRepository = new ProvRepository();
    EntityRepository2 entityRepository2 = new EntityRepository2();


    public ProvAgent buildProvenanceAgent(TTEntity targetEntity, String agentName) {
        String root;

        if (null != targetEntity.getGraph())
            root = targetEntity.getGraph().getIri();
        else if (null != targetEntity.getScheme() && null != targetEntity.getScheme().getIri())
            root = targetEntity.getScheme().getIri();
        else
            root = IM.NAMESPACE.iri;

        String uir = getPerson(agentName, root);
        ProvAgent agent = new ProvAgent()
                .setPersonInRole(TTIriRef.iri(uir))
                .setParticipationType(IM.AUTHOR_ROLE.asTTIriRef());
        agent.setName(agentName).setIri(uir.replace("uir.", "agent.")).setCrud(IM.ADD_QUADS.asTTIriRef());
        return agent;
    }

    public ProvActivity buildProvenanceActivity(TTEntity targetEntity, ProvAgent agent, String usedEntityIri) {
        ProvActivity activity = new ProvActivity()
                .setIri("urn:uuid:" + UUID.randomUUID())
                .setActivityType(IM.PROV_CREATION.asTTIriRef())
                .setEffectiveDate(LocalDateTime.now().toString())
                .addAgent(TTIriRef.iri(agent.getIri()))
                .setTargetEntity(TTIriRef.iri(targetEntity.getIri()));

        if (null != usedEntityIri) {
            activity.setActivityType(IM.PROV_UPDATE.asTTIriRef());
            activity.set(IM.PROV_USED.asTTIriRef(), TTIriRef.iri(usedEntityIri));
        }

        activity.setCrud(IM.ADD_QUADS.asTTIriRef());
        return activity;
    }

    public TTEntity buildUsedEntity(TTEntity usedEntity) throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            return new TTEntity()
                .setIri(usedEntity.getIri() + "/" + (usedEntity.getVersion()))
                .setName(usedEntity.getName())
                .set(IM.DEFINITION.asTTIriRef(), new TTLiteral(om.writeValueAsString(usedEntity)))
                .setGraph(IM.GRAPH_PROV.asTTIriRef())
                .setCrud(IM.ADD_QUADS.asTTIriRef());
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
