package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.repository.EntityRepository;
import org.endeavourhealth.dataaccess.repository.EntityTripleRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.dataaccess.repository.TermCodeRepository;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeServiceTest {

    @InjectMocks
    RuntimeService runtimeService;

    @Mock
    EntityRepository entityRepository;

    @Mock
    EntityTripleRepository entityTripleRepository;

    @Mock
    ConfigRepository configRepository;

    @Mock
    TermCodeRepository termCodeRepository;

    @InjectMocks
    ConfigService configService;

    @Test
    public void getEntityIdForSchemeCode_NullScheme() throws SQLException {
         String actual = runtimeService.getEntityIdForSchemeCode(null, "25451000252115");
         assertEquals("",actual);
    }
    @Test
    public void getEntityIdForSchemeCode_NullCode() throws SQLException {
        String actual = runtimeService.getEntityIdForSchemeCode("http://endhealth.info/im#891071000252105", null);
        assertEquals("",actual);
    }
    @Test
    public void getEntityIdForSchemeCode_NullSchemeCode() throws SQLException {
        String actual = runtimeService.getEntityIdForSchemeCode(null, null);
        assertEquals("",actual);
    }

    @Test
    public void getEntityIdForSchemeCode_NotNullSchemeCode() throws SQLException {
        TTIriRef entity = new TTIriRef().setIri("http://endhealth.info/im#25451000252115");
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(entity);
        String actual = runtimeService.getEntityIdForSchemeCode("http://endhealth.info/im#891071000252105", "25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getMappedCoreCodeForSchemeCode_NullScheme() throws SQLException {
        String actual = runtimeService.getMappedCoreCodeForSchemeCode(null, "25451000252115",false);
        assertEquals("",actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_NullCode() throws SQLException {
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115", null,false);
        assertEquals("",actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_NullSchemeCode() throws SQLException {
        String actual = runtimeService.getMappedCoreCodeForSchemeCode(null, null,false);
        assertEquals("",actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyTrueMapsSizeZero() throws SQLException {
        TTIriRef entity = new TTIriRef();
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(entity);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),any())).thenReturn(valueSetMembers);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeZero() throws SQLException {
        TTIriRef entity = new TTIriRef();
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(entity);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),any())).thenReturn(valueSetMembers);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",false);
        assertNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNull() throws SQLException {
        TTIriRef entity = new TTIriRef().setIri("http://endhealth.info/im#25451000252115");
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(entity);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        valueSetMembers.add(new ValueSetMember().setEntity(iri("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),any())).thenReturn(valueSetMembers);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getEntityDbidForSchemeCode_NullScheme() throws SQLException {
        Integer actual = runtimeService.getEntityDbidForSchemeCode(null, "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getEntityDbidForSchemeCode_NullCode() throws SQLException {
        Integer actual = runtimeService.getEntityDbidForSchemeCode("http://endhealth.info/im#25451000252115", null);
        assertNull(actual);
    }

    @Test
    public void getEntityDbidForSchemeCode_NullSchemeCode() throws SQLException {
        Integer actual = runtimeService.getEntityDbidForSchemeCode(null, null);
        assertNull(actual);
    }

    @Test
    public void getEntityDbidForSchemeCode_NotNullSchemeCode() throws SQLException {
        when(termCodeRepository.findDbidByCodeAndScheme_Iri(any(),any())).thenReturn(1);
        Integer actual = runtimeService.getEntityDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getMappedCoreEntityDbidForSchemeCode_NullScheme() throws SQLException {
        Integer actual = runtimeService.getMappedCoreEntityDbidForSchemeCode(null, "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getMappedCoreEntityDbidForSchemeCode_NullCode() throws SQLException {
        Integer actual = runtimeService.getMappedCoreEntityDbidForSchemeCode("http://endhealth.info/im#25451000252115", null);
        assertNull(actual);
    }

    @Test
    public void getMappedCoreEntityDbidForSchemeCode_NullSchemeCode() throws SQLException {
        Integer actual = runtimeService.getMappedCoreEntityDbidForSchemeCode(null, null);
        assertNull(actual);
    }

     @Test
    public void getMappedCoreEntityDbidForSchemeCode_NullEntity() throws SQLException {
         TTIriRef entity = new TTIriRef().setIri("http://endhealth.info/im#25451000252115");
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(entity);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        when(entityTripleRepository.getObjectBySubjectAndPredicate(any(),any())).thenReturn(valueSetMembers);
        Integer actual = runtimeService.getMappedCoreEntityDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getCodeForEntityDbid_NullDbid() throws SQLException {
        String actual = runtimeService.getCodeForEntityDbid(null);
        assertEquals("",actual);
    }

    @Test
    public void getCodeForEntityDbid_NotNullDbid() throws SQLException {
        String code = "25451000252115";
        when(entityRepository.findByDbid(any())).thenReturn(code);
        String actual = runtimeService.getCodeForEntityDbid(1);
        assertNotNull(actual);
    }

    @Test
    public void isInVSet_NullCode() throws JsonProcessingException, SQLException {

        Boolean actual = runtimeService.isInVSet(null, "http://endhealth.info/im#DiscoveryCodeScheme","http://endhealth.info/im#24951000252112");
        assertFalse(actual);
    }

    @Test
    public void isInVSet_NullV1Scheme() throws JsonProcessingException, SQLException {

        Boolean actual = runtimeService.isInVSet("25451000252115", null,"http://endhealth.info/im#24951000252112");
        assertFalse(actual);
    }

    @Test
    public void isInVSet_NullVSet() throws JsonProcessingException, SQLException {

        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", null);
        assertFalse(actual);
    }

    @Test
    public void isInVSet_NotNullCoreIncluded() throws JsonProcessingException, SQLException {

        runtimeService.configService = configService;
        String iri = "http://endhealth.info/im#DiscoveryCodeScheme";
        when(entityRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(iri);
        when(entityRepository.isCoreCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);
        when(entityRepository.isLegacyCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);

        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", "http://endhealth.info/im#24951000252112");
        assertTrue(actual);
    }

    @Test
    public void isInVSet_NotNullLegacyIncluded() throws JsonProcessingException, SQLException {
        String iri = "http://endhealth.info/im#DiscoveryCodeScheme";
        when(entityRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
        when(entityRepository.isLegacyCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(iri);
        when(entityRepository.isCoreCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);
        when(entityRepository.isLegacyCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);

        runtimeService.configService = configService;
        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", "http://endhealth.info/im#24951000252112");
        assertTrue(actual);
    }

    @Test
    public void isInVSet_NullCoreLegacyEIncluded() throws JsonProcessingException, SQLException {
        when(entityRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
        when(entityRepository.isLegacyCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
        runtimeService.configService = configService;
        Config config = new Config().setConfig("{\n" +
                "              \"SNOMED\" : \"http://endhealth.info/im#891101000252101\",\n" +
                "              \"READ2\" : \"http://endhealth.info/im#891141000252104\",\n" +
                "              \"CTV3\" : \"http://endhealth.info/im#891051000252101\",\n" +
                "              \"ICD10\" : \"http://endhealth.info/im#891021000252109\",\n" +
                "              \"OPCS4\" : \"http://endhealth.info/im#891041000252103\",\n" +
                "              \"EMIS_LOCAL\" : \"http://endhealth.info/im#891031000252107\",\n" +
                "              \"TPP_LOCAL\" : \"http://endhealth.info/im#631000252102\",\n" +
                "              \"BartsCerner\" : \"http://endhealth.info/im#891081000252108\",\n" +
                "              \"VISION\" : \"http://endhealth.info/im#1000131000252104\"\n" +
                "       }");
        when(configRepository.findByName(any())).thenReturn(config);
        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", "http://endhealth.info/im#24951000252112");
        assertFalse(actual);
    }



}
