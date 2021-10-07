package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.endeavourhealth.imapi.dataaccess.repository.SetRepository;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SetServiceTest {
    @InjectMocks
    SetService setService;

    @Mock
    SetRepository setRepository;

    @Before
    public void init() throws SQLException {
        // Definition mock
        TTEntity definition = mockDefinition();

        mockExpansion(definition);
        mockv1Map(definition);
    }

    @Test
    public void getExcelDownload_Definition() throws SQLException, JsonProcessingException {
        Workbook wb = setService.getExcelDownload("http://endhealth.info/im#CSET_BartsCVSSMeds", false, false);
        assertNotNull(wb);
        assertEquals(2, wb.getNumberOfSheets());

        checkSummarySheet(wb.getSheetAt(0));
        checkDefinitionSheet(wb.getSheetAt(1));
    }

    @Test
    public void getExcelDownload_Expand() throws SQLException, JsonProcessingException {
        Workbook wb = setService.getExcelDownload("http://endhealth.info/im#CSET_BartsCVSSMeds", true, false);
        assertNotNull(wb);
        assertEquals(3, wb.getNumberOfSheets());

        checkSummarySheet(wb.getSheetAt(0));
        checkDefinitionSheet(wb.getSheetAt(1));
        checkExpandedSheet(wb.getSheetAt(2));
    }

    @Test
    public void getExcelDownload_v1() throws SQLException, JsonProcessingException {
        Workbook wb = setService.getExcelDownload("http://endhealth.info/im#CSET_BartsCVSSMeds", true, true);
        assertNotNull(wb);
        assertEquals(4, wb.getNumberOfSheets());

        checkSummarySheet(wb.getSheetAt(0));
        checkDefinitionSheet(wb.getSheetAt(1));
        checkExpandedSheet(wb.getSheetAt(2));
        checkIMv1Sheet(wb.getSheetAt(3));
    }


    private TTEntity mockDefinition() throws SQLException {
        TTEntity definition = new TTEntity()
            .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
            .setName("Concept Set- Barts Covid vaccine study medication concepts");

        definition.set(IM.IS_CONTAINED_IN, new TTArray().add(iri("http://endhealth.info/im#CSET_BartsVaccineSafety", "Value sets for the Barts Vaccine safety study")));

        definition.set(IM.HAS_MEMBER, new TTArray()
            .add(iri("http://snomed.info/sct#39330711000001103", "COVID-19 vaccine (product)"))
            .add(new TTNode().set(OWL.INTERSECTIONOF, new TTArray()
                .add(iri("http://snomed.info/sct#10363601000001109", "UK product (product)"))
                .add(new TTNode().set(iri("http://snomed.info/sct#10362601000001103","Has VMP (attribute)"), iri("http://snomed.info/sct#39330711000001103","COVID-19 vaccine (product)")))
            ))
        );

        when(setRepository.getSetDefinition(any()))
            .thenReturn(definition);
        return definition;
    }

    private void mockExpansion(TTEntity definition) throws SQLException {
        TTEntity expansion = new TTEntity()
            .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
            .setName("Concept Set- Barts Covid vaccine study medication concepts");

        expansion.addObject(IM.HAS_MEMBER, new TTEntity().setIri("http://snomed.info/sct#39330711000001103").setName("COVID-19 vaccine (product)").setCode("39330711000001103").setScheme(iri("http://snomed.info/sct#")));
        expansion.addObject(IM.HAS_MEMBER, new TTEntity().setIri("http://snomed.info/sct#39116211000001106").setName("Generic COVID-19 Vaccine AstraZeneca (ChAdOx1 S [recombinant]) 5x10,000,000,000 viral particles/0.5ml dose solution for injection multidose vials (product)").setCode("39116211000001106").setScheme(iri("http://snomed.info/sct#")));
        expansion.addObject(IM.HAS_MEMBER, new TTEntity().setIri("http://snomed.info/sct#39114911000001105").setName("COVID-19 Vaccine AstraZeneca (ChAdOx1 S [recombinant]) 5x10,000,000,000 viral particles/0.5ml dose solution for injection multidose vials (AstraZeneca UK Ltd) (product)").setCode("39114911000001105").setScheme(iri("http://snomed.info/sct#")));
        expansion.addObject(IM.HAS_MEMBER, new TTEntity().setIri("http://snomed.info/sct#39116111000001100").setName("Generic COVID-19 mRNA Vaccine Pfizer-BioNTech BNT162b2 30micrograms/0.3ml dose concentrate for suspension for injection multidose vials (product)").setCode("39116111000001100").setScheme(iri("http://snomed.info/sct#")));

        when(setRepository.getExpansion(definition))
            .thenReturn(expansion);
    }

    private void mockv1Map(TTEntity definition) throws SQLException {
        TTEntity v1 = new TTEntity()
            .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
            .setName("Concept Set- Barts Covid vaccine study medication concepts");

        v1.addObject(IM.HAS_MEMBER, new TTEntity().setCode("39330711000001103").setScheme(iri("http://snomed.info/sct#")).set(iri(IM.NAMESPACE + "im1dbid"), literal(2873859)));
        v1.addObject(IM.HAS_MEMBER, new TTEntity().setCode("39116211000001106").setScheme(iri("http://snomed.info/sct#")).set(iri(IM.NAMESPACE + "im1dbid"), literal(2654668)));
        v1.addObject(IM.HAS_MEMBER, new TTEntity().setCode("39114911000001105").setScheme(iri("http://snomed.info/sct#")).set(iri(IM.NAMESPACE + "im1dbid"), literal(1564467)));
        v1.addObject(IM.HAS_MEMBER, new TTEntity().setCode("39116111000001100").setScheme(iri("http://snomed.info/sct#")).set(iri(IM.NAMESPACE + "im1dbid"), literal(2796244)));

        when(setRepository.getIM1Expansion(definition))
            .thenReturn(v1);
    }

    private void checkSummarySheet(Sheet sheet) {
        assertEquals("Concept summary", sheet.getSheetName());

        assertEquals(2, sheet.getPhysicalNumberOfRows());

        checkCells(sheet.getRow(0), "Iri", "Name");
        checkCells(sheet.getRow(1), "http://endhealth.info/im#CSET_BartsCVSSMeds", "Concept Set- Barts Covid vaccine study medication concepts");
    }

    private void checkDefinitionSheet(Sheet sheet) {
        assertEquals("Definitions", sheet.getSheetName());

        assertEquals(2, sheet.getPhysicalNumberOfRows());

        checkCells(sheet.getRow(0), "ECL", "JSON");

        checkCells(sheet.getRow(1),
            "<<39330711000001103 OR (<<10363601000001109 : <<10362601000001103 = <<39330711000001103)",
            "{\"@id\":\"http://endhealth.info/im#CSET_BartsCVSSMeds\",\"http://endhealth.info/im#hasMembers\":[{\"@id\":\"http://snomed.info/sct#39330711000001103\",\"name\":\"COVID-19 vaccine (product)\"},{\"http://www.w3.org/2002/07/owl#intersectionOf\":[{\"@id\":\"http://snomed.info/sct#10363601000001109\",\"name\":\"UK product (product)\"},{\"http://snomed.info/sct#10362601000001103\":{\"@id\":\"http://snomed.info/sct#39330711000001103\",\"name\":\"COVID-19 vaccine (product)\"}}]}],\"http://endhealth.info/im#isContainedIn\":[{\"@id\":\"http://endhealth.info/im#CSET_BartsVaccineSafety\",\"name\":\"Value sets for the Barts Vaccine safety study\"}],\"http://www.w3.org/2000/01/rdf-schema#label\":\"Concept Set- Barts Covid vaccine study medication concepts\"}"
        );
    }

    private void checkExpandedSheet(Sheet sheet) {
        assertEquals("Expanded", sheet.getSheetName());

        assertEquals(5, sheet.getPhysicalNumberOfRows());

        checkCells(sheet.getRow(0), "Iri", "Name", "Code", "Scheme");
        checkCells(sheet.getRow(1), "http://snomed.info/sct#39330711000001103","COVID-19 vaccine (product)","39330711000001103","http://snomed.info/sct#");
        checkCells(sheet.getRow(2), "http://snomed.info/sct#39116211000001106","Generic COVID-19 Vaccine AstraZeneca (ChAdOx1 S [recombinant]) 5x10,000,000,000 viral particles/0.5ml dose solution for injection multidose vials (product)","39116211000001106","http://snomed.info/sct#");
        checkCells(sheet.getRow(3), "http://snomed.info/sct#39114911000001105","COVID-19 Vaccine AstraZeneca (ChAdOx1 S [recombinant]) 5x10,000,000,000 viral particles/0.5ml dose solution for injection multidose vials (AstraZeneca UK Ltd) (product)","39114911000001105","http://snomed.info/sct#");
        checkCells(sheet.getRow(4), "http://snomed.info/sct#39116111000001100","Generic COVID-19 mRNA Vaccine Pfizer-BioNTech BNT162b2 30micrograms/0.3ml dose concentrate for suspension for injection multidose vials (product)","39116111000001100","http://snomed.info/sct#");

    }

    private void checkIMv1Sheet(Sheet sheet) {
        assertEquals("IM v1 Map", sheet.getSheetName());

        assertEquals(5, sheet.getPhysicalNumberOfRows());

        checkCells(sheet.getRow(0), "IMv2 Code", "IMv2 Scheme", "IMv1 Dbid");
        checkCells(sheet.getRow(1), "39330711000001103", "http://snomed.info/sct#", "2873859");
        checkCells(sheet.getRow(2), "39116211000001106", "http://snomed.info/sct#", "2654668");
        checkCells(sheet.getRow(3), "39114911000001105", "http://snomed.info/sct#", "1564467");
        checkCells(sheet.getRow(4), "39116111000001100", "http://snomed.info/sct#", "2796244");
    }

    private void checkCells(Row row, String... values) {
        int i = 0;

        assertEquals(row.getPhysicalNumberOfCells(), values.length, "Cell count mismatch");

        for (String expected: values) {
            String actual = row.getCell(i++).getStringCellValue();
            assertEquals(expected, actual, "Column " + i);
        }
    }
}
