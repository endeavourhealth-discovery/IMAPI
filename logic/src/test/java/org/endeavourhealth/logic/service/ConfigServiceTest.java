package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Entity;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.entity.Tct;
import org.endeavourhealth.dataaccess.repository.EntityRepository;
import org.endeavourhealth.dataaccess.repository.EntityTctRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.imapi.model.search.EntitySummary;
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
    EntityRepository entityRepository;

    @Mock
    EntityTctRepository entityTctRepository;

    @Test
    public void getQuickAccess_NullConfig() throws JsonProcessingException, SQLException {
        when(configRepository.findByName(any())).thenReturn(null);
        List<EntitySummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }

    @Test
    public void getQuickAccess_NotNullConfigAndNullEntity() throws JsonProcessingException, SQLException {
        Config config = new Config()
                .setConfig("[\":SemanticEntity\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);
        when(entityRepository.getEntitySummaryByIri(any())).thenReturn(null);
        List<EntitySummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }

    @Test
    public void getQuickAccess_NotNullConfigAndEntity() throws JsonProcessingException, SQLException {
        Config config = new Config()
                .setConfig("[\":SemanticEntity\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);

        EntitySummary entitySummary = new EntitySummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"));
        when(entityRepository.getEntitySummaryByIri(any())).thenReturn(entitySummary);

        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTctRepository.findByDescendant_Iri_AndAncestor_IriIn(any(), any())).thenReturn(Collections.singletonList(ttIriRef));

        List<EntitySummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }


    @Test
    public void getQuickAccess_NullScheme() throws JsonProcessingException, SQLException {
        Config config = new Config()
                .setConfig("[\":SemanticEntity\", \":HealthRecord\", \":VSET_DataModel\", \":VSET_QueryValueSets\"]");
        when(configRepository.findByName(any())).thenReturn(config);

        EntitySummary entitySummary = new EntitySummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate"));
        when(entityRepository.getEntitySummaryByIri(any())).thenReturn(entitySummary);

        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTctRepository.findByDescendant_Iri_AndAncestor_IriIn(any(), any())).thenReturn(Collections.singletonList(ttIriRef));

        List<EntitySummary> actual = configService.getQuickAccess();
        assertNotNull(actual);

    }


}