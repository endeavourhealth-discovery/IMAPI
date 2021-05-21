package org.endeavourhealth.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Config;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTripleRepository;
import org.endeavourhealth.dataaccess.repository.ConfigRepository;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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

    @InjectMocks
    ConfigService configService;

    @Test
    public void getConceptIdForSchemeCode_NullScheme(){
         String actual = runtimeService.getConceptIdForSchemeCode(null, "25451000252115");
         assertEquals("",actual);
    }
    @Test
    public void getConceptIdForSchemeCode_NullCode(){
        String actual = runtimeService.getConceptIdForSchemeCode("http://endhealth.info/im#891071000252105", null);
        assertEquals("",actual);
    }
    @Test
    public void getConceptIdForSchemeCode_NullSchemeCode(){
        String actual = runtimeService.getConceptIdForSchemeCode(null, null);
        assertEquals("",actual);
    }

    @Test
    public void getConceptIdForSchemeCode_NotNullSchemeCode(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        String actual = runtimeService.getConceptIdForSchemeCode("http://endhealth.info/im#891071000252105", "25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getMappedCoreCodeForSchemeCode_NullScheme(){
        String actual = runtimeService.getMappedCoreCodeForSchemeCode(null, "25451000252115",false);
        assertEquals("",actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_NullCode(){
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115", null,false);
        assertEquals("",actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_NullSchemeCode(){
        String actual = runtimeService.getMappedCoreCodeForSchemeCode(null, null,false);
        assertEquals("",actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyTrueMapsSizeZero(){
        Concept concept = new Concept();
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeZero(){
        Concept concept = new Concept();
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",false);
        assertNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeOne(){
        Concept concept = new Concept();
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setDbid(1)
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept()
                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",false);
        assertNotNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_SnomedOnlyFalseMapsSizeBiggerThanOne(){
        Concept concept = new Concept();
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setDbid(1)
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept()
                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
        tpl.add(new Tpl()
                .setDbid(2)
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000552115"))
                .setObject(new Concept()
                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",false);
        assertNotNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_MapsSizeBiggerThanOneObjectNotNullImCodeSchemeSmomed(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setDbid(1)
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept()
                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
        tpl.add(new Tpl()
                .setDbid(2)
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000552115"))
                .setObject(new Concept()
                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNotNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNotNullNotImCodeSchemeSmomed(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept()
                        .setIri("http://endhealth.info/im#25451000252115").setCode("25451000252115")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNotNull(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115"))
                .setObject(new Concept()
                        .setIri(IM.CODE_SCHEME_SNOMED.getIri()).setCode("25451000252115")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNotNull(actual);

    }

    @Test
    public void getMappedCoreCodeForSchemeCode_MapsSizeOneObjectNull(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setSubject(new Concept().setIri("http://endhealth.info/im#25451000252115")));

        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        String actual = runtimeService.getMappedCoreCodeForSchemeCode("http://endhealth.info/im#25451000252115",
                "25451000252115",true);
        assertNull(actual);

    }

    @Test
    public void getConceptDbidForSchemeCode_NullScheme(){
        Integer actual = runtimeService.getConceptDbidForSchemeCode(null, "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getConceptDbidForSchemeCode_NullCode(){
        Integer actual = runtimeService.getConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", null);
        assertNull(actual);
    }

    @Test
    public void getConceptDbidForSchemeCode_NullSchemeCode(){
        Integer actual = runtimeService.getConceptDbidForSchemeCode(null, null);
        assertNull(actual);
    }

    @Test
    public void getConceptDbidForSchemeCode_NotNullSchemeCode(){
        Concept concept = new Concept().setDbid(1);
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Integer actual = runtimeService.getConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullScheme(){
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode(null, "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullCode(){
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", null);
        assertNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullSchemeCode(){
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode(null, null);
        assertNull(actual);
    }

     @Test
    public void getMappedCoreConceptDbidForSchemeCode_NullConcept(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNull(actual);
    }

    @Test
    public void getMappedCoreConceptDbidForSchemeCode_NotNullConcept(){
        Concept concept = new Concept().setIri("http://endhealth.info/im#25451000252115");
        when(conceptRepository.findByCodeAndScheme(any(),any())).thenReturn(concept);
        Set<Tpl> tpl = new HashSet<>();
        tpl.add(new Tpl()
                .setDbid(1)
                .setObject(new Concept().setDbid(1)));
        tpl.add(new Tpl()
                .setDbid(2)
                .setObject(new Concept().setDbid(2)));
        when(conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(any(),any())).thenReturn(tpl);
        Integer actual = runtimeService.getMappedCoreConceptDbidForSchemeCode("http://endhealth.info/im#25451000252115", "25451000252115");
        assertNotNull(actual);
    }

    @Test
    public void getCodeForConceptDbid_NullDbid(){
        String actual = runtimeService.getCodeForConceptDbid(null);
        assertEquals("",actual);
    }

    @Test
    public void getCodeForConceptDbid_NotNullDbid(){
        Concept concept = new Concept().setCode("25451000252115");
        when(conceptRepository.findByDbid(any())).thenReturn(concept);
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
        Concept concept = new Concept();
        when(conceptRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(concept);
        when(conceptRepository.isCoreCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);
        when(conceptRepository.isLegacyCodeSchemeExcludedInVSet(any(),any(),any())).thenReturn(null);

        Boolean actual = runtimeService.isInVSet("25451000252115", "http://endhealth.info/im#DiscoveryCodeScheme", "http://endhealth.info/im#24951000252112");
        assertTrue(actual);
    }

    @Test
    public void isInVSet_NotNullLegacyIncluded() throws JsonProcessingException, SQLException {
        Concept concept = new Concept();
        when(conceptRepository.isCoreCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(null);
        when(conceptRepository.isLegacyCodeSchemeIncludedInVSet(any(),any(),any())).thenReturn(concept);
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