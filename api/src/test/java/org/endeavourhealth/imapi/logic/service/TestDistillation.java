package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDistillation {

    private final static EntityRepository entityRepository = new EntityRepository();

    @Test
    void getDistillation(){
        List<TTIriRef> conceptList = new ArrayList<>();
        conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#73211009"));
        conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#46635009"));
        conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#44054006"));
        conceptList.add(new TTIriRef().setIri("http://endhealth.info/im#Q_RegisteredGMS"));

        List<TTIriRef> distList = entityRepository.getDistillation(conceptList);
        distList.forEach(c -> System.out.println(c.getIri()));
    }
}
