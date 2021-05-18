package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.repository.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IndividualServiceTest {
    @InjectMocks IndividualService individualService;

    @Mock IndividualRepository individualRepository;
    @Mock IndividualTplRepository individualTplRepository;
    @Mock IndividualTplDataRepository individualTplDataRepository;

    @Test
    public void getIndividual() throws Exception {
        when(individualRepository.getByIri("http://endhealth.info/im#25451000252115")).thenReturn(
            new TTInstance().setIri("http://endhealth.info/im#25451000252115").setName("Amlodipine Besilate")
        );
        when(individualTplRepository.findAllBySubjectIri("http://endhealth.info/im#25451000252115")).thenReturn(
            new TTNode().set(IM.IS_A, IM.CODE_SCHEME_BARTS)
        );
        when(individualTplDataRepository.findAllBySubjectIri("http://endhealth.info/im#25451000252115")).thenReturn(
            new TTNode().set(RDFS.LABEL, literal("Legacy concept"))
        );

        TTInstance actual = individualService.getIndividual("http://endhealth.info/im#25451000252115") ;

        assertNotNull(actual);
    }
}
