package org.endeavourhealth.logic.service;


import org.endeavourhealth.dataaccess.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndividualServiceTest {
    @InjectMocks IndividualService individualService;

    @Mock
    InstanceTplRepository instanceTplRepository;
    @Mock
    InstanceTplDataRepository instanceTplDataRepository;

    @Test
    public void getIndividual() throws Exception {

    }
}
