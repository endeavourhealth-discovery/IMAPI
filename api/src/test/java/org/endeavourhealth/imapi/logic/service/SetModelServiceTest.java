package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class SetModelServiceTest {

  @InjectMocks
  SetService setService = new SetService();

  @Mock
  SetRepository setRepository;

  @Test
  void getSetExport_NullIri() {
    SetOptions setOptions = new SetOptions(null, false, true, true, true, List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getSetExport_EmptyIri() {
    SetOptions setOptions = new SetOptions("", false, true, true, true, List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getSetExport_EmptyFormat() {
    SetOptions setOptions = new SetOptions("", false, true, true, true, List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getDistillation() {
    List<TTIriRef> conceptList = new ArrayList<>();
    conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#73211009"));
    conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#46635009"));
    conceptList.add(new TTIriRef().setIri("http://snomed.info/sct#44054006"));
    conceptList.add(new TTIriRef().setIri("http://endhealth.info/im#Q_RegisteredGMS"));

    String iris = "<http://snomed.info/sct#73211009> <http://snomed.info/sct#46635009> <http://snomed.info/sct#44054006> <http://endhealth.info/im#Q_RegisteredGMS>";

    when(setRepository.getDistillation(iris)).thenReturn(Set.of("http://snomed.info/sct#46635009", "http://snomed.info/sct#44054006"));

    Set<String> distList = new HashSet<>();
    distList.add("http://snomed.info/sct#73211009");
    distList.add("http://endhealth.info/im#Q_RegisteredGMS");

    List<TTIriRef> actual = setService.getDistillation(conceptList);

    assertEquals(actual, distList.stream().map(distIri -> new TTIriRef().setIri(distIri)).toList());

  }
}
