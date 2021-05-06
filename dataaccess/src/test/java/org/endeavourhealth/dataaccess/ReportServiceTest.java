package org.endeavourhealth.dataaccess;

import org.endeavourhealth.dataaccess.entity.SimpleCount;
import org.endeavourhealth.dataaccess.repository.SimpleCountRepository;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {

    @InjectMocks
    ReportService reportService;

    @Mock
    SimpleCountRepository simpleCountRepository;

    @Test
    public void getConceptTypeReport_GetDataFromRepo(){
        SimpleCount simpleCount = new SimpleCount().setCount(20);
        when(simpleCountRepository.getConceptTypeReport()).thenReturn(Collections.singletonList(simpleCount));
        List<org.endeavourhealth.imapi.model.report.SimpleCount> actual = reportService.getConceptTypeReport();
        assertNotNull(actual);
    }

    @Test
    public void getConceptTypeReport_GetDataFromCaChe(){
        List<org.endeavourhealth.imapi.model.report.SimpleCount> actual = reportService.getConceptTypeReport();
        assertNotNull(actual);
    }

    @Test
    public void getConceptSchemeReport_GetDataFromRepo(){
        SimpleCount simpleCount = new SimpleCount().setCount(20);
        when(simpleCountRepository.getConceptSchemeReport()).thenReturn(Collections.singletonList(simpleCount));
        List<org.endeavourhealth.imapi.model.report.SimpleCount> actual = reportService.getConceptSchemeReport();
        assertNotNull(actual);
    }

    @Test
    public void getConceptStatusReport_GetDataFromRepo(){
        SimpleCount simpleCount = new SimpleCount().setCount(20);
        when(simpleCountRepository.getConceptStatusReport()).thenReturn(Collections.singletonList(simpleCount));
        List<org.endeavourhealth.imapi.model.report.SimpleCount> actual = reportService.getConceptStatusReport();
        assertNotNull(actual);
    }

    @Test
    public void getConceptCategoryReport_GetDataFromRepo(){
        List<SimpleCount> simpleCount = new ArrayList<>();
           simpleCount.add(new SimpleCount().setCount(20));
           simpleCount.add(new SimpleCount().setIri(IM.VALUESET.getIri()).setCount(20));
           simpleCount.add(new SimpleCount().setIri(IM.RECORD.getIri()).setCount(20));
        when(simpleCountRepository.getConceptTypeReport()).thenReturn(simpleCount);
        List<org.endeavourhealth.imapi.model.report.SimpleCount> actual = reportService.getConceptCategoryReport();
        assertNotNull(actual);
    }

}