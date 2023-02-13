package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.cdm.ProvActivity;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

public class TestWriteDelta {

    private final FilerService filerService = new FilerService();

    //@Test
    void writeDelta() throws Exception {
        TTEntity entity = new TTEntity("http://endhealth.info/im#Q_RegisteredGMS")
                .setName("Patients registered for GMS services on the reference date")
                .addType(IM.QUERY)
                .setCrud(IM.PROV_CREATION)
                .setGraph(IM.GRAPH_DISCOVERY);

        ProvActivity provActivity = new ProvActivity();
        provActivity.setIri("http://endhealth.info/im#Q_RegisteredGMS")
                    .setEffectiveDate("2022-11-08T12:53:27.907326800")
                    .setActivityType(new TTIriRef("http://endhealth.info/im#2001000252109"))
                    .setTargetEntity(new TTIriRef("http://endhealth.info/im#Q_RegisteredGMS"))
                    .setCrud(IM.PROV_CREATION);

        filerService.writeDelta(entity,provActivity);
    }
}
