package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SetModelServiceTest {

  @InjectMocks
  SetService setService = new SetService();

  @Mock
  SetRepository setRepository = new SetRepository();

  @Test
  void getSetExport_NullIri() {
    SetOptions setOptions = new SetOptions(null, false, true, true, true, List.of());
    SetExporterOptions options = new SetExporterOptions(setOptions, true, false);
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(options));
  }

  @Test
  void getSetExport_EmptyIri() {
    SetOptions setOptions = new SetOptions(null, false, true, true, true, List.of());
    SetExporterOptions options = new SetExporterOptions(setOptions, true, false);
    assertThrows(IllegalArgumentException.class, () -> setService.getSetExport(options));
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

    assertEquals(actual, distList);

  }
}
