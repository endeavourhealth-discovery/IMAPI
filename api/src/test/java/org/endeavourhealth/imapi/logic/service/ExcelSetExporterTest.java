package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.logic.exporters.ExcelSetExporter;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.iml.Query;
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
import java.util.zip.DataFormatException;

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
    SetRepository setRepository;

    @Test
    void getSetExport_NotNullIriNoConcept() throws DataFormatException, JsonProcessingException {
        when(entityTripleRepository.getEntityPredicates(any(), anySet())).thenReturn(new TTBundle().setEntity(new TTEntity()));
        XSSFWorkbook actual = excelSetExporter.getSetAsExcel("http://endhealth.info/im#25451000252115", true, true, false);
        assertNotNull(actual);
    }

    @Test
    void getSetExport_NotNullIriWithDefinition() throws DataFormatException, JsonProcessingException {
        when(entityTripleRepository.getEntityPredicates(any(), anySet())).thenReturn(new TTBundle().setEntity(mockDefinition()));
        when(setRepository.getSetExpansion(any(), anyBoolean(),any())).thenReturn(new HashSet<>());
        when(setRepository.getSetMembers(any(), anyBoolean())).thenReturn(new HashSet<>());
        when(setRepository.getSubsets(anyString())).thenReturn(new HashSet<>());
        ReflectionTestUtils.setField(excelSetExporter, "setExporter", setExporter);

        XSSFWorkbook actual = excelSetExporter.getSetAsExcel("http://endhealth.info/im#25451000252115", true, true, false);

        assertNotNull(actual);
        assertEquals(3, actual.getNumberOfSheets());
    }

    private TTEntity mockDefinition() throws JsonProcessingException {
        TTEntity definition = new TTEntity()
            .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
            .setName("Concept SetModel- Barts Covid vaccine study medication concepts");

        definition.set(IM.IS_CONTAINED_IN, new TTArray().add(iri("http://endhealth.info/im#CSET_BartsVaccineSafety", "Value sets for the Barts Vaccine safety study")));

        definition.set(IM.DEFINITION, TTLiteral.literal(new Query()
            .where(w->w
              .or(o->o
                .from(f->f
                .setIri("http://snomed.info/sct#39330711000001103").setName("COVID-19 vaccine (product)").setIncludeSubtypes(true)))
              .or(o->o
                .from(f->f
                    .setIri("http://snomed.info/sct#10363601000001109").setName("UK product (product)").setIncludeSubtypes(true))
                  .setPathTo(IM.ROLE_GROUP.getIri())
                  .property(p->p
                    .setIri("http://snomed.info/sct#10362601000001103")
                    .setName("Has VMP (attribute)")
                      .setIncludeSubtypes(true))
                  .setIs(TTAlias.iri("http://snomed.info/sct#39330711000001103")
                    .setName("COVID-19 vaccine (product)")
                    .setIncludeSubtypes(true))))));
        return definition;
    }

}
