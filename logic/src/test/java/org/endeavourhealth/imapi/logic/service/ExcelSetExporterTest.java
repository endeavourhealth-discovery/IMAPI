package org.endeavourhealth.imapi.logic.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.dataaccess.EntityRepositoryImpl2;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.logic.exporters.ExcelSetExporter;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ExcelSetExporterTest {
    @InjectMocks
    ExcelSetExporter excelSetExporter;

    @Mock
    EntityTripleRepository entityTripleRepository;

    @Mock
    EntityRepositoryImpl2 entityRepositoryImpl2;

    @Test
    void getSetExport_NotNullIriNoConcept() throws DataFormatException {
        when(entityTripleRepository.getEntityPredicates(any(), anySet(), anyInt())).thenReturn(new TTBundle().setEntity(new TTEntity()));
        XSSFWorkbook actual = excelSetExporter.getSetAsExcel("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
    }

    @Test
    void getSetExport_NotNullIriWithDefinition() throws DataFormatException {
        when(entityTripleRepository.getEntityPredicates(any(), anySet(), anyInt())).thenReturn(new TTBundle().setEntity(mockDefinition()));
        when(entityRepositoryImpl2.getSetExpansion(any(), anyBoolean())).thenReturn(new HashSet<>());
        XSSFWorkbook actual = excelSetExporter.getSetAsExcel("http://endhealth.info/im#25451000252115");
        assertNotNull(actual);
        assertEquals(3, actual.getNumberOfSheets());
    }

    private TTEntity mockDefinition() {
        TTEntity definition = new TTEntity()
            .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
            .setName("Concept Set- Barts Covid vaccine study medication concepts");

        definition.set(IM.IS_CONTAINED_IN, new TTArray().add(iri("http://endhealth.info/im#CSET_BartsVaccineSafety", "Value sets for the Barts Vaccine safety study")));

        definition.set(IM.DEFINITION, new TTNode()
            .set(SHACL.OR,new TTArray()
                .add(iri("http://snomed.info/sct#39330711000001103", "COVID-19 vaccine (product)"))
                .add(new TTNode().set(SHACL.AND, new TTArray()
                    .add(iri("http://snomed.info/sct#10363601000001109", "UK product (product)"))
                    .add(new TTNode()
                        .set(iri("http://snomed.info/sct#10362601000001103","Has VMP (attribute)")
                            ,iri("http://snomed.info/sct#39330711000001103","COVID-19 vaccine (product)")))
                ))
            ));

        return definition;
    }

}
