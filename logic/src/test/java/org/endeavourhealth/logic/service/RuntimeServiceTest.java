package org.endeavourhealth.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTripleRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.dataaccess.repository.TermCodeRepository;
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
    ConceptRepository conceptRepository;

    @Mock
    ConceptTripleRepository conceptTripleRepository;

    @Mock
    ConfigRepository configRepository;

    @Mock
    TermCodeRepository termCodeRepository;

    @InjectMocks
    ConfigService configService;

    @Test
    public void getConceptIdForSchemeCode_NullScheme() throws SQLException {
         String actual = runtimeService.getConceptIdForSchemeCode(null, "25451000252115");
         assertEquals("",actual);
    }
    @Test
    public void getConceptIdForSchemeCode_NullCode() throws SQLException {
        String actual = runtimeService.getConceptIdForSchemeCode("http://endhealth.info/im#891071000252105", null);
        assertEquals("",actual);
    }
    @Test
    public void getConceptIdForSchemeCode_NullSchemeCode() throws SQLException {
        String actual = runtimeService.getConceptIdForSchemeCode(null, null);
        assertEquals("",actual);
    }

    @Test
    public void getConceptIdForSchemeCode_NotNullSchemeCode() throws SQLException {
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
        String actual = runtimeService.getConceptIdForSchemeCode("http://endhealth.info/im#891071000252105", "25451000252115");
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
        Concept concept = new Concept();
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(valueSetMembers);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeZero() throws SQLException {
        Concept concept = new Concept();
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(valueSetMembers);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",false);
        assertNull(actual);

    }

//    @Test
//    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeOne() throws SQLException {
//        Concept concept = new Concept();
//        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
//        Set<Tpl> tpl = new HashSet<>();
//        tpl.add(new Tpl()
//                .setDbid(1)
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
//                .setObject(new Concept()
//                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
//        Set<ValueSetMember> valueSetMembers = new HashSet<>();
//        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(valueSetMembers);
//        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
//                "25451000252115",false);
//        assertNotNull(actual);
//
//    }

//    @Test
//    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeBiggerThanOne() throws SQLException {
//        Concept concept = new Concept();
//        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
//        Set<Tpl> tpl = new HashSet<>();
//        tpl.add(new Tpl()
//                .setDbid(1)
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
//                .setObject(new Concept()
//                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
//        tpl.add(new Tpl()
//                .setDbid(2)
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000552115"))
//                .setObject(new Concept()
//                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
//        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
//        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
//                "25451000252115",false);
//        assertNotNull(actual);
//
//    }

//    @Test
//    public void getMappedCoreCodeForSchemeCode_MapsSizeBiggerThanOneObjectNotNullImCodeSchemeSmomed() throws SQLException {
//        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
//        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
//        Set<Tpl> tpl = new HashSet<>();
//        tpl.add(new Tpl()
//                .setDbid(1)
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
//                .setObject(new Concept()
//                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
//        tpl.add(new Tpl()
//                .setDbid(2)
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000552115"))
//                .setObject(new Concept()
//                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
//        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
//        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
//                "25451000252115",true);
//        assertNotNull(actual);
//
//    }

//    @Test
//    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNotNullNotImCodeSchemeSmomed() throws SQLException {
//        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
//        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
//        Set<Tpl> tpl = new HashSet<>();
//        tpl.add(new Tpl()
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
//                .setObject(new Concept()
//                        .setIri("http://endhealth.info/im#25451000252115").setCode("25451000252115")));
//
//        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
//        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
//                "25451000252115",true);
//        assertNull(actual);
//
//    }

//    @Test
//    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNotNull() throws SQLException {
//        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
//        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
//        Set<Tpl> tpl = new HashSet<>();
//        tpl.add(new Tpl()
//                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
//                .setObject(new Concept()
//                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
//
//        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
//        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
//                "25451000252115",true);
//        assertNotNull(actual);
//
//    }

    @Test
    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNull() throws SQLException {
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        valueSetMembers.add(new ValueSetMember().setConcept(iri("http://endhealth.info/im#25451000252115")));
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(valueSetMembers);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getConceptDbidForSchemeCode_NullScheme() throws SQLException {
        Integer actual = runtimeService.getConceptDbidForSchemeCode(null, "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getConceptDbidForSchemeCode_NullCode() throws SQLException {
        Integer actual = runtimeService.getConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", null);
        assertNull(actual);
    }

    @Test
    public void getConceptDbidForSchemeCode_NullSchemeCode() throws SQLException {
        Integer actual = runtimeService.getConceptDbidForSchemeCode(null, null);
        assertNull(actual);
    }

    @Test
    public void getConceptDbidForSchemeCode_NotNullSchemeCode() throws SQLException {
        Concept concept = new Concept().setDbid(1);
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
        Integer actual = runtimeService.getConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullScheme() throws SQLException {
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode(null, "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullCode() throws SQLException {
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", null);
        assertNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullSchemeCode() throws SQLException {
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode(null, null);
        assertNull(actual);
    }

     @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullConcept() throws SQLException {
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
        Set<ValueSetMember> valueSetMembers = new HashSet<>();
        when(conceptTripleRepository.getMemberBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(valueSetMembers);
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNull(actual);
    }

//    @Test
//    public void getMappedCoreConceptDbidForSchemeCode_NotNullConcept() throws SQLException {
//        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
//        when(termCodeRepository.findByCodeAndScheme_Iri(any(),any())).thenReturn(concept);
//        Set<ValueSetMember> valueSetMembers = new HashSet<>();
//        valueSetMembers.add(new Tpl()
//                .setDbid(1)
//                .setObject(new Concept().setDbid(1)));
//        valueSetMembers.add(new Tpl()
//                .setDbid(2)
//                .setObject(new Concept().setDbid(2)));
//        when(conceptTripleRepository.getMemberIriRefsBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(valueSetMembers);
//        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
//        assertNotNull(actual);
//    }

    @Test
    public void getCodeForConceptDbid_NullDbid() throws SQLException {
        String actual = runtimeService.getCodeForConceptDbid(null);
        assertEquals("",actual);
    }

    @Test
    public void getCodeForConceptDbid_NotNullDbid() throws SQLException {
        String code = "25451000252115";
        when(conceptRepository.findByDbid(any())).thenReturn(code);
        String actual = runtimeService.getCodeForConceptDbid(1);
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
        when(conceptRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(iri);
        when(conceptRepository.isCoreCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);
        when(conceptRepository.isLegacyCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);

        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", "http://endhealth.info/im#24951000252112");
        assertTrue(actual);
    }

    @Test
    public void isInVSet_NotNullLegacyIncluded() throws JsonProcessingException, SQLException {
        String iri = "http://endhealth.info/im#DiscoveryCodeScheme";
        when(conceptRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
        when(conceptRepository.isLegacyCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(iri);
        when(conceptRepository.isCoreCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);
        when(conceptRepository.isLegacyCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);

        runtimeService.configService = configService;
        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", "http://endhealth.info/im#24951000252112");
        assertTrue(actual);
    }

    @Test
    public void isInVSet_NullCoreLegacyEIncluded() throws JsonProcessingException, SQLException {
        when(conceptRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
        when(conceptRepository.isLegacyCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
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