package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.interfacemanager.model.SetOptions;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SetModelServiceTest {

  @Mock
  SetRepository setRepository;

  SetService setService;

  @BeforeEach
  void initMocks() {
    setService = new SetService(setRepository);
  }

  @Test
  void getSetExport_NullIri() {
    SetOptions setOptions = new SetOptions().setIri(null).includeDefinition(false).includeCore(true).includeLegacy(true).includeSubsets(true).schemes(List.of()).subsumptions(List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getSetExport_EmptyIri() {
    SetOptions setOptions = new SetOptions().setIri("").includeDefinition(false).includeCore(true).includeLegacy(true).includeSubsets(true).schemes(List.of()).subsumptions(List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getSetExport_EmptyFormat() {
    SetOptions setOptions = new SetOptions().setIri("").includeDefinition(false).includeCore(true).includeLegacy(true).includeSubsets(true).schemes(List.of()).subsumptions(List.of());
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(null, true, setOptions));
  }

  @Test
  void getDistillation() {
    List<TTIriRef> conceptList = new ArrayList<>();
    conceptList.add(new TTIriRef().iri("http://snomed.info/sct#73211009"));
    conceptList.add(new TTIriRef().iri("http://snomed.info/sct#46635009"));
    conceptList.add(new TTIriRef().iri("http://snomed.info/sct#44054006"));
    conceptList.add(new TTIriRef().iri("http://endhealth.info/im#Q_RegisteredGMS"));

    String iris = "<http://snomed.info/sct#73211009> <http://snomed.info/sct#46635009> <http://snomed.info/sct#44054006> <http://endhealth.info/im#Q_RegisteredGMS>";

    when(setRepository.getDistillation(iris)).thenReturn(Set.of("http://snomed.info/sct#46635009", "http://snomed.info/sct#44054006"));

    Set<String> distList = new HashSet<>();
    distList.add("http://snomed.info/sct#73211009");
    distList.add("http://endhealth.info/im#Q_RegisteredGMS");

    List<TTIriRef> actual = setService.getDistillation(conceptList);

    assertEquals(actual, distList.stream().map(distIri -> new TTIriRef().iri(distIri)).toList());

  }
}
