package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.entity.Tct;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTctRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void getQuickAccess_NullConfig() throws JsonProcessingException {
        when(configRepository.findByName(any())).thenReturn(null);
        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }

    @Test
    public void getQuickAccess_NotNullConfigAndNullConcept() throws JsonProcessingException {
        Config config = new Config()
                .setConfig("[\":SemanticConcept\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);
        when(conceptRepository.findByIri(any())).thenReturn(null);
        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }

    @Test
    public void getQuickAccess_NotNullConfigAndConcept() throws JsonProcessingException {
        Config config = new Config()
                .setConfig("[\":SemanticConcept\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);

        Concept concept = new Concept()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setScheme(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));
        when(conceptRepository.findByIri(any())).thenReturn(concept);

        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));
        when(conceptTctRepository.findByDescendant_Iri_AndAncestor_IriIn(any(), any())).thenReturn(Collections.singleton(tct));

        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }


    @Test
    public void getQuickAccess_NullScheme() throws JsonProcessingException {
        Config config = new Config()
                .setConfig("[\":SemanticConcept\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);

        Concept concept = new Concept()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115");
        when(conceptRepository.findByIri(any())).thenReturn(concept);

        Tct tct = new Tct()
                .setAncestor(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115")
                        .setName("Adverse reaction to Amlodipine Besilate"));
        when(conceptTctRepository.findByDescendant_Iri_AndAncestor_IriIn(any(), any())).thenReturn(Collections.singleton(tct));

        List<ConceptSummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }


}