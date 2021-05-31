package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.entity.Tct;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTctRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceTest {
    @InjectMocks
    ConfigService configService;

    @Mock
    ConfigRepository configRepository;

    @Mock
    ConceptRepository conceptRepository;

    @Mock
    ConceptTctRepository conceptTctRepository;

    @Test
    public void getQuickAccess_NullConfig() throws JsonProcessingException, SQLException {
        when(configRepository.findByName(any())).thenReturn(null);
        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }

    @Test
    public void getQuickAccess_NotNullConfigAndNullConcept() throws JsonProcessingException, SQLException {
        Config config = new Config()
                .setConfig("[\":SemanticConcept\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);
        when(conceptRepository.getConceptSummaryByIri(any())).thenReturn(null);
        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }

    @Test
    public void getQuickAccess_NotNullConfigAndConcept() throws JsonProcessingException, SQLException {
        Config config = new Config()
                .setConfig("[\":SemanticConcept\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);

        ConceptSummary conceptSummary = new ConceptSummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"));
        when(conceptRepository.getConceptSummaryByIri(any())).thenReturn(conceptSummary);

        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(conceptTctRepository.findByDescendant_Iri_AndAncestor_IriIn(any(), any())).thenReturn(Collections.singletonList(ttIriRef));

        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }


    @Test
    public void getQuickAccess_NullScheme() throws JsonProcessingException, SQLException {
        Config config = new Config()
                .setConfig("[\":SemanticConcept\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);

        ConceptSummary conceptSummary = new ConceptSummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"));
        when(conceptRepository.getConceptSummaryByIri(any())).thenReturn(conceptSummary);

        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(conceptTctRepository.findByDescendant_Iri_AndAncestor_IriIn(any(), any())).thenReturn(Collections.singletonList(ttIriRef));

        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }


}