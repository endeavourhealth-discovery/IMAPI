package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.dto.EntityDefinitionDto;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EntityServiceTest {
    @InjectMocks
    EntityService entityService;

    @Mock
    EntityRepository entityRepository;

    @Mock
    EntityTripleRepository entityTripleRepository;

    @Mock
    EntityTctRepository entityTctRepository;

    @Mock
    EntitySearchRepository entitySearchRepository;

    @Mock
    SetRepository SetRepository;

    @Mock
    TermCodeRepository termCodeRepository;

    @Mock
    EntityTypeRepository entityTypeRepository;

    @Mock
    ConfigService configService;

    @Test
    void getEntityPredicates_nullIriPredicates() {

        TTBundle actual = entityService.getEntityPredicates(null,null, 0);
        assertNotNull(actual);
        assertNotNull(actual.getEntity());
    }

    @Test
    void getEntityPredicates_EmptyIri() {

        TTBundle actual = entityService.getEntityPredicates("", null, 0);
        assertNotNull(actual);
        assertNotNull(actual.getEntity());
    }

    @Test
    void getEntityPredicates_notNullIriPredicates() {
        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.ROLE_GROUP).setFunctional(false));
        tplList.add(new Tpl()
                .setDbid(7)
                .setPredicate(IM.ROLE).setFunctional(false)
                .setParent(3));
        tplList.add(new Tpl()
                .setDbid(8)
                .setPredicate(OWL.ONPROPERTY)
                .setParent(7)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(9)
                .setPredicate(OWL.SOMEVALUESFROM)
                .setParent(7)
                .setObject(iri("http://endhealth.info/im#25451000252115")));

        when(entityTripleRepository.getTriplesRecursive(any(), anySet(), anyInt())).thenReturn(tplList);
        TTEntity actual = entityService.getEntityPredicates("http://endhealth.info/im#25451000252115",Set.of(IM.IS_A.getIri(),RDFS.LABEL.getIri()), 0).getEntity();
        assertNotNull(actual);
    }

    @Test
    void getEntityReference_NullIri() {
        TTIriRef actual = entityService.getEntityReference(null);

        assertNull(actual);

    }

    @Test
    void getEntityReference_NullEntity() {
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(null);
        TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115");

        assertNull(actual);

    }

    @Test
    void getEntityReference_NotNullEntity() {
        TTIriRef ttIriRef = new TTIriRef().setIri("http://endhealth.info/im#25451000252115").setName("http://endhealth.info/im#25451000252115");
        when(entityRepository.getEntityReferenceByIri("http://endhealth.info/im#25451000252115")).thenReturn(ttIriRef);
        TTIriRef actual = entityService.getEntityReference("http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_NullIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateChildren(null,null, 1, 10, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_EmptyIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateChildren("",null, 1, 10, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_NullIndexSize() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateChildren("http://endhealth.info/im#25451000252115", null,null, null, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateChildren_NotNullIriAndInactiveTrue() {

        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",null,
                0, 20,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115",null, 1, 20, true);
        assertNotNull(actual);
    }

    @Test
    void getImmediateChildren_NotNullIriAndInactiveFalse() {
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.findImmediateChildrenByIri("http://endhealth.info/im#25451000252115",null,
                                0, 20,false))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateChildren
                ("http://endhealth.info/im#25451000252115",null, 1, 20, false);

        assertNotNull(actual);

    }

    @Test
    void getImmediateParents_NullIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents(null, null, 1, 10, true);

        assertNotNull(actual);
    }

    @Test
    void getImmediateParents_EmptyIri() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents("",null, 1, 10, true);

        assertNotNull(actual);
    }

    @Test
    void getImmediateParents_NullIndexSize() {
        List<EntityReferenceNode> actual = entityService
                .getImmediateParents("http://endhealth.info/im#25451000252115",null, null, null, true);

        assertNotNull(actual);
    }

    @Test
    void getImmediateParents_NotNullIriAndInactiveTrue() {

        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")))
                .setParents(Collections.singletonList(
                        new EntityReferenceNode("http://endhealth.info/im#25451000252115",
                                "Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
                0, 20,true))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray()
                .add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", null, 1, 20, true);

        assertNotNull(actual);

    }

    @Test
    void getImmediateParents_NotNullIriAndInactiveFalse() {
        EntityReferenceNode entityReferenceNode = new EntityReferenceNode()
                .setChildren(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")))
                .setParents(Collections.singletonList(new EntityReferenceNode("http://endhealth.info/im#25451000252115")));
        when(entityTripleRepository.findImmediateParentsByIri("http://endhealth.info/im#25451000252115", null,
                0,10,false))
                .thenReturn(Collections.singletonList(entityReferenceNode));
        TTArray ttArray = new TTArray().add(iri("http://endhealth.info/im#25451000252115","Adverse reaction caused by drug (disorder)"));
        when(entityTypeRepository.getEntityTypes(any())).thenReturn(ttArray);
        List<EntityReferenceNode> actual = entityService.getImmediateParents
                ("http://endhealth.info/im#25451000252115", null, 1, 10, false);

        assertNotNull(actual);

    }

    @Test
    void isWhichType_NullIri() {
        List<TTIriRef> actual = entityService
                .isWhichType(null, Arrays.asList("A","B"));

        assertNotNull(actual);
    }

    @Test
    void isWhichType_EmptyIri() {
        List<TTIriRef> actual = entityService
                .isWhichType("", Arrays.asList("A","B"));

        assertNotNull(actual);
    }

    @Test
    void isWhichType_EmptyCandidates() {
        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115", Collections.emptyList());

        assertNotNull(actual);
    }

    @Test
    void isWhichType_NullCandidates() {
        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115", null);

        assertNotNull(actual);
    }

    @Test
    void isWhichType_NullIriAndCandidates() {
        List<TTIriRef> actual = entityService
                .isWhichType(null, null);

        assertNotNull(actual);
    }

    @Test
    void isWhichType_NotNullIriAndCandidates() {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://www.w3.org/2002/07/owl#Class")
                .setName("Class");

        when(entityTctRepository.findAncestorsByType(any(),any(),any()))
                .thenReturn(Collections.singletonList(ttIriRef));

        List<TTIriRef> actual = entityService
                .isWhichType("http://endhealth.info/im#25451000252115",
                        Collections.singletonList("http://endhealth.info/im#25451000252115"));

        assertNotNull(actual);
    }

    @Test
    void usages_NullIri() throws JsonProcessingException {
        List<TTIriRef> actual = entityService.usages(null,null,null);

        assertNotNull(actual);
    }

    @Test
    void usages_EmptyIri() throws JsonProcessingException {
        List<TTIriRef> actual = entityService.usages("",null,null);

        assertNotNull(actual);
    }

    @Test
    void usages_NotNullIri() throws JsonProcessingException {
        TTIriRef ttIriRef = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        when(entityTripleRepository.getActiveSubjectByObjectExcludeByPredicate( any(), any(),any(),any())).thenReturn(Collections.singletonList(ttIriRef));
        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

        List<TTIriRef> actual = entityService.usages("http://endhealth.info/im#25451000252115",1,10);

        assertNotNull(actual);
    }

    @Test
    void usages_XMLContainsIri() throws JsonProcessingException {

        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://endhealth.info/im#25451000252115"));

        List<TTIriRef> actual = entityService.usages("http://endhealth.info/im#25451000252115",1,10);

        assertNotNull(actual);
    }

    @Test
    void totalRecords_NullIri() throws JsonProcessingException {
        Integer actual = entityService.totalRecords(null);
        assertNotNull(actual);
    }

    @Test
    void totalRecords_NotNullIri() throws JsonProcessingException {
        when(entityTripleRepository.getCountOfActiveSubjectByObjectExcludeByPredicate( any(),any())).thenReturn(1000);
        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

        Integer actual = entityService.totalRecords("http://endhealth.info/im#25451000252115");
        assertEquals(1000, actual);
    }

    @Test
    void totalRecords_XMLIri() throws JsonProcessingException {
        when(configService.getConfig(any(), any(TypeReference.class))).thenReturn(Collections.singletonList("http://www.w3.org/2001/XMLSchema#string"));

        Integer actual = entityService.totalRecords("http://www.w3.org/2001/XMLSchema#string");
        assertEquals(0, actual);
    }

    @Test
    void advancedSearch_NullRequest() {
        List<SearchResultSummary> actual = entityService.advancedSearch(null);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NullTermFilter() {
        SearchRequest searchRequest = new SearchRequest().setTermFilter(null);

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_EmptyTermFilter() {
        SearchRequest searchRequest = new SearchRequest().setTermFilter("");

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NullSchemeFilter() {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(null);

        SearchResultSummary entitySearch = new SearchResultSummary()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate")
                .setDescription(null)
                .setCode("25451000252115")
                .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
                .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
                .thenReturn(Collections.singletonList(entitySearch));

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NotNullSchemeFilter() {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        SearchResultSummary entitySearch = new SearchResultSummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(entitySearch));

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void advancedSearch_NotNullMarkIfDescendentOf() {
        SearchRequest searchRequest = new SearchRequest()
                .setTermFilter("Encounter")
                .setMarkIfDescendentOf(Arrays.asList(":DiscoveryCommonDataModel", ":SemanticEntity", ":VSET_ValueSet"))
                .setSchemeFilter(Arrays.asList("http://endhealth.info/im#891071000252105",
                        "http://endhealth.info/im#891101000252101", "http://endhealth.info/im#891111000252103"));

        SearchResultSummary entitySearch = new SearchResultSummary()
            .setIri("http://endhealth.info/im#25451000252115")
            .setName("Adverse reaction to Amlodipine Besilate")
            .setDescription(null)
            .setCode("25451000252115")
            .setScheme(iri("http://endhealth.info/im#891071000252105", "Discovery code"))
            .setStatus(iri("http://endhealth.info/im#Active", "Active"));
        when(entitySearchRepository.advancedSearch(any()))
            .thenReturn(Collections.singletonList(entitySearch));

        List<SearchResultSummary> actual = entityService.advancedSearch(searchRequest);

        assertNotNull(actual);
    }

    @Test
    void getValueSetMembers_NullIri() {
        ExportValueSet actual = entityService.getValueSetMembers(null, true,false, null);

        assertNull(actual);

    }

    @Test
    void getValueSetMembers_EmptyIri() {
        ExportValueSet actual = entityService.getValueSetMembers("", true,false, null);

        assertNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandMemberTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setLiteral("Included")
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setLiteral("Excluded")
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
            .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
            .thenReturn(excluded);

        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
            .thenReturn(Collections.singleton(includedSet));

        List<ValueSetMember> valueSetMembers = new ArrayList<>();
        valueSetMembers.add(new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        when(SetRepository.expandMember(any(),anyInt())).thenReturn(valueSetMembers);

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), true, false, 0, null, valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandSubsetTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), false, true, 0, null, valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandFalse() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), false, false, 0, "Parent", valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_ExpandTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), true, true, 0, "Parent", valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_NullParentSetName() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), false, false, 0, null, valueSetIri.getIri());

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_NotEqualOriginalParentIriExpandSetTrue() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), false, true, 0, null, "http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }

    @Test
    void getValueSetMembers_NotEqualOriginalParentIriExpandSetFalse() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        ExportValueSet actual = entityService.getValueSetMembers(valueSetIri.getIri(), false, false, 0, null, "http://endhealth.info/im#25451000252115");

        assertNotNull(actual);

    }


    @Test
    void isValuesetMember_NullIriAndMember() {
        ValueSetMembership actual = entityService.isValuesetMember(null, null);

        assertNull(actual);
    }

    @Test
    void isValuesetMember_EmptyIriAndMember() {
        ValueSetMembership actual = entityService.isValuesetMember("", "");

        assertNull(actual);
    }

    @Test
    void isValuesetMember_NotNullIriAndHasMember() {
        TTIriRef ttIriRef1 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        TTIriRef ttIriRef2 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.DEFINITION.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.DEFINITION.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.NOT_MEMBER.getIri()));
        when(SetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(SetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = entityService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.DEFINITION.getIri());

        assertNotNull(actual);
    }

    @Test
    void isValuesetMember_NotNullIriAndNotMember() {
        TTIriRef ttIriRef1 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");

        TTIriRef ttIriRef2 = new TTIriRef()
                .setIri("http://endhealth.info/im#25451000252115")
                .setName("Adverse reaction to Amlodipine Besilate");
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.DEFINITION.getIri())))
                .thenReturn(Collections.singleton(ttIriRef1));
        when(entityTripleRepository.getObjectIriRefsBySubjectAndPredicate(any(),eq(IM.NOT_MEMBER.getIri())))
                .thenReturn(Collections.singleton(ttIriRef2));

        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember1 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.DEFINITION.getIri()));
        org.endeavourhealth.imapi.model.valuset.ValueSetMember valueSetMember2 = new org.endeavourhealth.imapi.model.valuset.ValueSetMember()
                .setEntity(iri(IM.NOT_MEMBER.getIri()));
        when(SetRepository.expandMember(ttIriRef1.getIri())).thenReturn(Collections.singletonList(valueSetMember1));
        when(SetRepository.expandMember(ttIriRef2.getIri())).thenReturn(Collections.singletonList(valueSetMember2));

        ValueSetMembership actual = entityService.isValuesetMember("http://endhealth.info/im#25451000252115",
                IM.NOT_MEMBER.getIri());

        assertNotNull(actual);
    }

    @Test
    void getEntityTermCodes_NullIri() {
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes(null);
        assertNotNull(actual);
    }

    @Test
    void getEntityTermCodes_EmptyIri() {
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes("");
        assertNotNull(actual);
    }

    @Test
    void getEntityTermCodes_NotNullIri() {
        org.endeavourhealth.imapi.model.TermCode termCode = new org.endeavourhealth.imapi.model.TermCode()
                .setCode("24951000252112")
                .setName("Adverse reaction to Testogel")
                .setScheme("http://endhealth.info/im#25451000252115")
                .setEntityTermCode("32231000252116");
        when(termCodeRepository.findAllByIri(any())).thenReturn(Collections.singletonList(termCode));
        List<org.endeavourhealth.imapi.model.TermCode> actual = entityService.getEntityTermCodes("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getDataModelProperties_NullEntity(){
        List<DataModelProperty> actual = entityService.getDataModelProperties((TTEntity) null);
        assertNotNull(actual);
    }

    @Test
    void getDataModelProperties_NotNullEntity(){
        List<DataModelProperty> actual = entityService.getDataModelProperties(new TTEntity()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(SHACL.PROPERTY, new TTArray().add(new TTNode()
                        .set(IM.INHERITED_FROM, new TTIriRef())
                        .set(SHACL.PATH, new TTIriRef())
                        .set(SHACL.CLASS,new TTIriRef())
                        .set(SHACL.DATATYPE, new TTIriRef())
                        .set(SHACL.MAXCOUNT,new TTLiteral())
                        .set(SHACL.MINCOUNT,new TTLiteral())
                )));
        assertNotNull(actual);
    }

    @Test
    void getDataModelProperties_NotInheritedFrom(){
        List<DataModelProperty> actual = entityService.getDataModelProperties(new TTEntity()
                .setIri("http://endhealth.info/im#25451000252115")
                .set(SHACL.PROPERTY, new TTArray().add(new TTNode()))
        );
        assertNotNull(actual);
    }

    @Test
    void valueSetMembersCSV_NullIri() {
        String actual = entityService.valueSetMembersCSV(null, true,true);
        assertNotNull(actual);
    }

    @Test
    void valueSetMembersCSV_NotNullIriExpandTrue() {

        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"))
                .setCode("25")
                .setScheme(iri("http://endhealth.info/im#emis","Emis"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        String actual = entityService.valueSetMembersCSV(valueSetIri.getIri(), true, true);
        assertNotNull(actual);
    }

    @Test
    void valueSetMembersCSV_NotNullIriExpandFalse() {
        TTIriRef valueSetIri = new TTIriRef().setIri("http://endhealth.info/im#ValueSet").setName("Value set");
        when(entityRepository.getEntityReferenceByIri(any())).thenReturn(valueSetIri );

        List<Tpl> included = new ArrayList<>();
        included.add(new Tpl()
                .setDbid(2)
                .setObject(iri("http://endhealth.info/im#IncludedMember","Included member"))
                .setPredicate(iri("http://www.w3.org/ns/shacl#or")));
        included.add(new Tpl()
                .setDbid(1)
                .setPredicate(iri("http://endhealth.info/im#definition")));
        List<Tpl> excluded = new ArrayList<>();
        excluded.add(new Tpl()
                .setDbid(3)
                .setObject(iri("http://endhealth.info/im#ExcludedMember","Excluded member"))
                .setPredicate(iri("http://endhealth.info/im#891071000252105")));
        excluded.add(new Tpl()
                .setDbid(4)
                .setPredicate(iri("http://endhealth.info/im#notMembers")));


        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.DEFINITION.getIri())),anyInt()))
                .thenReturn(included);
        when(entityTripleRepository.getTriplesRecursive(eq("http://endhealth.info/im#ValueSet"), eq(Collections.singleton(IM.NOT_MEMBER.getIri())),anyInt()))
                .thenReturn(excluded);


        ValueSetMember includedSet = new ValueSetMember()
                .setEntity(iri("http://endhealth.info/im#IncludedSet","Included set"))
                .setCode("25")
                .setScheme(iri("http://endhealth.info/im#emis","Emis"));

        when(entityTripleRepository.getSubjectByObjectAndPredicateAsValueSetMembers(valueSetIri.getIri(),IM.MEMBER_OF_GROUP.getIri()))
                .thenReturn(Collections.singleton(includedSet));

        String actual = entityService.valueSetMembersCSV(valueSetIri.getIri(), false, false);
        assertNotNull(actual);
    }

    @Test
    void getGraphData_NullIri() {
        GraphDto actual = entityService.getGraphData(null);
        assertNotNull(actual);
    }

    @Test
    void getGraphData_NotNullEntity() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.ROLE_GROUP));
        tplList.add(new Tpl()
                .setDbid(10)
                .setPredicate(IM.PROPERTY_GROUP));
        tplList.add(new Tpl()
                .setDbid(11)
                .setPredicate(IM.INHERITED_FROM)
                .setParent(10)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(12)
                .setPredicate(SHACL.PROPERTY)
                .setParent(10));
        tplList.add(new Tpl()
                .setDbid(13)
                .setPredicate(SHACL.PATH)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#25451000252116","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(14)
                .setPredicate(SHACL.CLASS)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#Class","Class")));
        tplList.add(new Tpl()
                .setDbid(15)
                .setPredicate(SHACL.DATATYPE)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#DataType","DataType")));
        tplList.add(new Tpl()
                .setDbid(16)
                .setPredicate(SHACL.MINCOUNT)
                .setParent(12)
                .setLiteral("1"));
        tplList.add(new Tpl()
                .setDbid(17)
                .setPredicate(SHACL.MAXCOUNT)
                .setParent(12)
                .setLiteral("10"));
        tplList.add(new Tpl()
                .setDbid(21)
                .setPredicate(RDFS.SUBCLASSOF)
                .setObject(iri("http://endhealth.info/im#25451000252121"))
                .setLiteral("3")
                .setParent(12));
        tplList.add(new Tpl()
                .setDbid(20)
                .setPredicate(RDFS.SUBCLASSOF)
                .setObject(iri("http://endhealth.info/im#25451000252120")));
        tplList.add(new Tpl()
                .setDbid(22)
                .setPredicate(RDFS.SUBCLASSOF)
                .setObject(iri("http://endhealth.info/im#25451000252130")));
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getGraphData_RoleGroup() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.ROLE_GROUP));
        tplList.add(new Tpl()
                .setDbid(4)
                .setPredicate(OWL.ONPROPERTY)
                .setParent(3)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(6)
                .setPredicate(OWL.SOMEVALUESFROM)
                .setParent(3)
                .setObject(iri("http://endhealth.info/im#25451000252115")));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getGraphData_LeafNodes() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A)
                .setObject(iri("http://endhealth.info/im#25451000252115")));
        tplList.add(new Tpl()
                .setDbid(10)
                .setPredicate(IM.PROPERTY_GROUP));
        tplList.add(new Tpl()
                .setDbid(12)
                .setPredicate(SHACL.PROPERTY)
                .setParent(10));
        tplList.add(new Tpl()
                .setDbid(13)
                .setPredicate(SHACL.PATH)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#25451000252116","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(14)
                .setPredicate(SHACL.CLASS)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#Class","Class")));
        tplList.add(new Tpl()
                .setDbid(15)
                .setPredicate(SHACL.DATATYPE)
                .setParent(12)
                .setObject(iri("http://endhealth.info/im#DataType","DataType")));
        tplList.add(new Tpl()
                .setDbid(16)
                .setPredicate(SHACL.MINCOUNT)
                .setParent(12)
                .setLiteral("1"));
        tplList.add(new Tpl()
                .setDbid(17)
                .setPredicate(SHACL.MAXCOUNT)
                .setParent(12)
                .setLiteral("10"));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getGraphData_ParentIsList() {

        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));

        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        GraphDto actual = entityService.getGraphData("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getEntityDefinitionDto_NullIri() {
        EntityDefinitionDto actual = entityService.getEntityDefinitionDto(null);
        assertNotNull(actual);

    }

    @Test
    void getEntityDefinitionDto_GetTypeNotNull() {
        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(RDF.TYPE).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(4)
                .setPredicate(RDFS.COMMENT)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(5)
                .setPredicate(IM.HAS_STATUS)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        EntityDefinitionDto actual = entityService.getEntityDefinitionDto(null);
        assertNotNull(actual);

    }

    @Test
    void getEntityDefinitionDto_GetTypeNull() {
        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(4)
                .setPredicate(RDFS.COMMENT)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(5)
                .setPredicate(IM.HAS_STATUS)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        EntityDefinitionDto actual = entityService.getEntityDefinitionDto(null);
        assertNotNull(actual);

    }

    @Test
    void getEntityDefinitionDto_HasSubclassOf() {
        List<Tpl> tplList= new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDFS.LABEL)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(IM.IS_A).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(RDFS.SUBCLASSOF).setFunctional(false)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        tplList.add(new Tpl()
                .setDbid(4)
                .setPredicate(RDFS.COMMENT)
                .setLiteral("Adverse reaction to Amlodipine Besilate"));
        tplList.add(new Tpl()
                .setDbid(5)
                .setPredicate(IM.HAS_STATUS)
                .setObject(iri("http://endhealth.info/im#25451000252115","Adverse reaction to Amlodipine Besilate")));
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(), anyInt())).thenReturn(tplList);
        EntityDefinitionDto actual = entityService.getEntityDefinitionDto(null);
        assertNotNull(actual);

    }

    @Test
    void getSummary_NullIri() {
        SearchResultSummary actual = entityService.getSummary(null);
        assertNull(actual);
    }

    @Test
    void getSummary_NotNullIri() {
        SearchResultSummary summary = new SearchResultSummary();
        when(entitySearchRepository.getSummary(any())).thenReturn(summary);
        SearchResultSummary actual = entityService.getSummary(null);
        assertNotNull(actual);
    }

    @Test
    void getConceptShape_NullIri() {
        TTEntity actual = entityService.getConceptShape(null);
        assertNull(actual);
    }

    @Test
    void getConceptShape_NotContainNodeShape() {
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setPredicate(RDF.TYPE));
        tplList.add(new Tpl()
                .setDbid(2)
                .setPredicate(SHACL.PROPERTY));
        tplList.add(new Tpl()
                .setDbid(3)
                .setPredicate(SHACL.OR));
        when(entityTripleRepository.getTriplesRecursive(any(), anySet(), anyInt())).thenReturn(tplList);
        TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
        assertNull(actual);
    }

    @Test
    void getConceptShape_ContainsNodeShape() {
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setFunctional(false)
                .setPredicate(RDF.TYPE)
                .setObject(iri("http://www.w3.org/ns/shacl#NodeShape","Node shape")));
        tplList.add(new Tpl()
                .setDbid(2)
                .setFunctional(false)
                .setPredicate(SHACL.PROPERTY));
        tplList.add(new Tpl()
                .setDbid(3)
                .setFunctional(false)
                .setPredicate(SHACL.OR));
        when(entityTripleRepository.getTriplesRecursive(any(), anySet(), anyInt())).thenReturn(tplList);
        TTEntity actual = entityService.getConceptShape("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getSummaryFromConfig_NullIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        TTEntity actual = entityService.getSummaryFromConfig(null, configs);
        assertNotNull(actual);
    }

    @Test
    void getSummaryFromConfig_EmptyIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        TTEntity actual = entityService.getSummaryFromConfig("", configs);
        assertNotNull(actual);
    }

    @Test
    void getSummaryFromConfig_NullConfig() {
        TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", null);
        assertNotNull(actual);
    }

    @Test
    void getSummaryFromConfig_NotNullIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF.getIri()));
        List<Tpl> tpl = new ArrayList<>();
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(),anyInt())).thenReturn(tpl);
        TTEntity actual = entityService.getSummaryFromConfig("http://endhealth.info/im#25451000252115", configs);
        assertNotNull(actual);
    }

    @Test
    void getJsonDownload_NullIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        DownloadDto actual = entityService.getJsonDownload(null, configs, false, false, false, false,
                false, false, false, false, false, false);
        assertNull(actual);
    }

    @Test
    void getJsonDownload_EmptyIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        DownloadDto actual = entityService.getJsonDownload("", configs, false, false, false, false,
                false, false, false, false, false, false);
        assertNull(actual);
    }

    @Test
    void getJsonDownload_OnlySummary() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF.getIri()));
        List<Tpl> tpl = new ArrayList<>();
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(),anyInt())).thenReturn(tpl);
        DownloadDto actual = entityService.getJsonDownload("http://endhealth.info/im#25451000252115", configs, false, false, false,
                false, false, false, false, false, false, false);
        assertNotNull(actual);
    }

    @Test
    void getJsonDownload_All() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF.getIri()));
        List<Tpl> tpl = new ArrayList<>();
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(),anyInt())).thenReturn(tpl);
        DownloadDto actual = entityService.getJsonDownload("http://endhealth.info/im#25451000252115", configs, true, true, true,
                true, true, true, true, true, true, true);
        assertNotNull(actual);
    }

    @Test
    void getExcelDownload_NullIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        XlsHelper actual = entityService.getExcelDownload(null, configs, false, false, false, false,
                false, false, false, false, false, false);
        assertNull(actual);

    }

    @Test
    void getExcelDownload_EmptyIri() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        XlsHelper actual = entityService.getExcelDownload("", configs, false, false, false, false,
                false, false, false, false, false, false);
        assertNull(actual);

    }

    @Test
    void getExcelDownload_SummaryOnly() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF.getIri()));
        List<Tpl> tpl = new ArrayList<>();
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(),anyInt())).thenReturn(tpl);
        XlsHelper actual = entityService.getExcelDownload("http://endhealth.info/im#25451000252115", configs, false, false, false, false,
                false, false, false, false, false, false);
        assertNotNull(actual);

    }

    @Test
    void getExcelDownload_All() {
        List<ComponentLayoutItem> configs = new ArrayList<>();
        configs.add(new ComponentLayoutItem().setPredicate(IM.IS_CHILD_OF.getIri()));
        List<Tpl> tpl = new ArrayList<>();
        when(entityTripleRepository.getTriplesRecursive(any(),anySet(),anyInt())).thenReturn(tpl);
        XlsHelper actual = entityService.getExcelDownload("http://endhealth.info/im#25451000252115", configs, true, true, true,
                true, true, true, true, true, true, true);
        assertNotNull(actual);
    }

    @Test
    void getNamespaces_() {
        List<Namespace> namespace = new ArrayList<>();
        when(entityTripleRepository.findNamespaces()).thenReturn(namespace);
        List<Namespace> actual = entityService.getNamespaces();
        assertNotNull(actual);

    }

    @Test
    void getInferredBundle_NullIri() {
        TTBundle actual = entityService.getInferredBundle(null);
        assertNotNull(actual);
    }

    @Test
    void getInferredBundle_EmptyIri() {
        TTBundle actual = entityService.getInferredBundle("");
        assertNotNull(actual);
    }

    @Test
    void getConcept_NullIri() {
        TTDocument actual = entityService.getConcept(null);
        assertNotNull(actual);
    }

    @Test
    void getConcept_EmptyIri() {
        TTDocument actual = entityService.getConcept("");
        assertNotNull(actual);
    }

    @Test
    void getConcept_Namespace() {
        List<Namespace> namespaces = new ArrayList<>();
        namespaces.add(new Namespace("http://endhealth.info/im#25451000252115","",""));
        when(entityTripleRepository.findNamespaces()).thenReturn(namespaces);
        TTDocument actual = entityService.getConcept("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getMatchedFrom_NullIri() {
        Collection<SimpleMap> actual = entityService.getMatchedFrom(null);
        assertNotNull(actual);
    }

    @Test
    void getMatchedFrom_EmptyIri() {
        Collection<SimpleMap> actual = entityService.getMatchedFrom("");
        assertNotNull(actual);
    }

}
