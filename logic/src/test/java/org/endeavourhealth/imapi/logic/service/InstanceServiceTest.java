package org.endeavourhealth.imapi.logic.service;


import org.endeavourhealth.imapi.dataaccess.InstanceRepository;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.dto.InstanceDTO;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstanceServiceTest {
    @InjectMocks
    InstanceService instanceService;

    @Mock
    InstanceRepository instanceRepository;

    @Test
    void getInstancePredicates_NullIri(){
        InstanceDTO actual = instanceService.getInstancePredicates(null, Set.of(""));
        assertNull(actual);
    }

    @Test
    void getInstancePredicates_NotNullIri(){
        InstanceDTO actual = instanceService.getInstancePredicates("http://endhealth.info/im#25451000252114", Set.of("http://endhealth.info/im#25451000252115"));
        List<Tpl> tplList = new ArrayList<>();
        tplList.add(new Tpl()
                .setDbid(1)
                .setObject(new TTIriRef().setIri("http://endhealth.info/im#25451000252114"))
                .setPredicate(new TTIriRef().setIri("http://endhealth.info/im#25451000252115"))
                .setLiteral("Adverse reaction to Amlodipine Besilate")
                .setFunctional(true));
        tplList.add(new Tpl()
                .setDbid(3)
                .setLiteral("Adverse reaction to Amlodipine Besilate")
                .setObject(new TTIriRef().setIri("http://endhealth.info/im#25451000252116"))
                .setPredicate(new TTIriRef().setIri("http://endhealth.info/im#25451000252117"))
                .setFunctional(false));
//        when(instanceRepository.getTriplesRecursive(any(), anySet())).thenReturn(tplList);
        assertNotNull(actual);
    }

    @Test
    void search_NullRequest() {
        List<TTIriRef> actual = instanceService.search(null, Set.of(""));
        assertNotNull(actual);
    }

    @Test
    void search_NotNullRequest() {
        List<TTIriRef> actual = instanceService.search("term", Set.of(""));
        List<TTIriRef> ttIriRefList = new ArrayList<>();
        ttIriRefList.add(new TTIriRef().setIri("http://endhealth.info/im#25451000252116"));
//        when(instanceRepository.search(any(), anySet())).thenReturn(ttIriRefList);
        assertNotNull(actual);
    }

    @Test
    void typesCount() {
        List<SimpleCount> actual = instanceService.typesCount();
        assertNotNull(actual);
    }

}
