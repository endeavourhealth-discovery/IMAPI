package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.logic.exporters.ExcelSetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)

public class ExcelSetExporterTest {

  @InjectMocks
  SetExporter setExporter;

  @InjectMocks
  ExcelSetExporter excelSetExporter;

  @Mock
  EntityTripleRepository entityTripleRepository;

  @Mock
  EntityRepository2 entityRepository2;

  @Mock
  SetRepository setRepository;

  @Test
  void getSetExport_NotNullIriNoConcept() throws JsonProcessingException, QueryException {
    when(entityTripleRepository.getEntityPredicates(any(), anySet())).thenReturn(new TTBundle().setEntity(new TTEntity()));
    SetExporterOptions options = new SetExporterOptions("http://endhealth.info/im#25451000252115", true, true,
      true, true, true, false, List.of());
    XSSFWorkbook actual = excelSetExporter.getSetAsExcel(options);
    assertNotNull(actual);
  }

  @Test
  void getSetExport_NotNullIriWithDefinition() throws JsonProcessingException, QueryException {
    when(entityTripleRepository.getEntityPredicates(any(), anySet())).thenReturn(new TTBundle().setEntity(mockDefinition()));
    when(entityRepository2.getBundle(any(), anySet())).thenReturn(new TTBundle().setEntity(new TTEntity().setName("Test")));
    when(setRepository.getSetExpansion(any(), anyBoolean(), any(), anyList())).thenReturn(new HashSet<>());
    when(setRepository.getSubsetIrisWithNames(anyString())).thenReturn(new HashSet<>());
    ReflectionTestUtils.setField(excelSetExporter, "setExporter", setExporter);

    SetExporterOptions options = new SetExporterOptions("http://endhealth.info/im#25451000252115", true, true,
      true, true, true, false, List.of());
    XSSFWorkbook actual = excelSetExporter.getSetAsExcel(options);

    assertNotNull(actual);
    assertEquals(3, actual.getNumberOfSheets());
  }

  private TTEntity mockDefinition() throws JsonProcessingException {
    TTEntity definition = new TTEntity()
      .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
      .setName("Concept SetModel- Barts Covid vaccine study medication concepts");

    definition.set(TTIriRef.iri(IM.IS_CONTAINED_IN), new TTArray().add(iri("http://endhealth.info/im#CSET_BartsVaccineSafety",
      "Value sets for the Barts Vaccine safety study")));

    definition.set(TTIriRef.iri(IM.DEFINITION), TTLiteral.literal(new Query()
      .match(w -> w
        .setBoolMatch(Bool.or)
        .match(f -> f
          .addInstanceOf(new Node().setIri("http://snomed.info/sct#39330711000001103").setName("COVID-19 vaccine (product)").setDescendantsOrSelfOf(true)))
        .match(f -> f
          .addInstanceOf(new Node().setIri("http://snomed.info/sct#10363601000001109").setName("UK product (product)").setDescendantsOrSelfOf(true))
          .where(p -> p
            .setIri(IM.ROLE_GROUP)
            .match(m1 -> m1
              .where(p1 -> p1
                .setIri("http://snomed.info/sct#10362601000001103")
                .setName("Has VMP (attribute)")
                .setDescendantsOrSelfOf(true)
                .addIs(new Node().setIri("http://snomed.info/sct#39330711000001103")
                  .setName("COVID-19 vaccine (product)")
                  .setDescendantsOrSelfOf(true)))))))));
    return definition;
  }

}
