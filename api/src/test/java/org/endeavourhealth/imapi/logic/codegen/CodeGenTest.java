package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jsonldjava.utils.Obj;
import io.cucumber.java.sl.In;
import org.joda.time.Partial;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CodeGenTest {

    CodeGenJava codeGen;

    @BeforeEach
    void init() {
        codeGen = new CodeGenJava();
    }


    @Test
    @DisplayName("Should capitalise and concatenate words correctly")
    void testGetFieldName() {


        String fieldName = codeGen.capitalise("gp registration ADMINISTRATION status history");
        String expectedOutput = "GpRegistrationAdministrationStatusHistory";

        assertEquals(expectedOutput, fieldName, "camelCase conversion correct");
    }

    @Test
    void testGetFieldNameQuoteWrap() {

        String fieldName = codeGen.capitalise("\"stated gender\"");
        String expectedOutput = "StatedGender";

        assertEquals(expectedOutput, fieldName, "Wrapper quotes filtered out correctly");
    }
    @Test
    void testGetFieldNameCharacterFilter() {

        String fieldName = codeGen.capitalise("\"medication (request) / prescription\"");
        String expectedOutput = "MedicationRequestPrescription";

        assertEquals(expectedOutput, fieldName, "Non-alphanumeric characters removed correctly");
    }

    @Test
    void testGetSuffix() {

        String fieldName = codeGen.getSuffix("http://www.w3.org/2001/XMLSchema#long");
        String expectedOutput = "long";

        assertEquals(expectedOutput, fieldName, "Non-alphanumeric characters removed correctly");
    }

    @Test
    void testGetSuffixIndexing() {

        String fieldName = codeGen.getSuffix("http://www.w3.org/2001/XML#Schema#long");
        String expectedOutput = "long";

        assertEquals(expectedOutput, fieldName, "Non-alphanumeric characters removed correctly");
    }

    @Test
    void testSerialise() throws JsonProcessingException {

        IMDMAddress home = new IMDMAddress().setPostcode("NE1").setProperty("city", "London");
        IMDMAddress work = new IMDMAddress().setPostcode("LS1").setProperty("city", "Leeds");

        LocalDateTime date = LocalDateTime.of(2019, 3, 28, 14, 33, 48, 640000);

        List<IMDMAddress> list = new ArrayList<>();
        list.add(home);
        list.add(work);

        IMDMPatient patient = new IMDMPatient()
            .setName("Fred Bloggs")
            .setDateOfBirth(PartialDateTime.parse("1973-09-26"))
            .setProperty("age", 21)
            .setProperty("address", list);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(patient);

        System.out.println(json);

        IMDMPatient actual = om.readValue(json, IMDMPatient.class);

        assertEquals(patient.getName(), actual.getName(), "Deserialized name not equal");
        assertEquals(patient.getDateOfBirth(), actual.getDateOfBirth(), "Deserialized DOB not equal");
        assertEquals(patient.getProperty("age").toString(), actual.getProperty("age").toString(), "Deserialized age not equal");

        List<IMDMAddress> outputList = actual.getProperty("address");

        assertEquals(list.get(0).getPostcode(), outputList.get(0).getPostcode(), "Deserialized postcode not equal");
        assertNotEquals(list.get(0).getPostcode(), outputList.get(1).getPostcode(), "Deserialized postcode not equal");

    }


    @Test
    void testPartialDateTime() {

        PartialDateTime testYear = new PartialDateTime(1980);
        String testYearString = "1980";
        PartialDateTime testMonth = new PartialDateTime(1980, 1);
        String testMonthString = "1980-01";
        PartialDateTime testDay = new PartialDateTime(1980, 1,5);
        String testDayString = "1980-01-05";
        PartialDateTime testTime = new PartialDateTime(1980, 1,5,2,3,24);
        String testTimeString = "1980-01-05T02:03:24Z";
        PartialDateTime testNano = new PartialDateTime(1980, 1,5,2,3,24, 55);
        String testNanoString = "1980-01-05T02:03:24.55Z";
        PartialDateTime testOffset = new PartialDateTime(1980, 1,5,2,3,24, 535, "+02:00");
        String testOffsetString = "1980-01-05T02:03:24.535+02:00";
        PartialDateTime testNanoLength = new PartialDateTime(1980, 1,5,23,30,4, 5, "+02:00");
        String testNanoLengthString = "1980-01-05T23:30:04.5+02:00";

        assertEquals(testYear.getDateTime(), PartialDateTime.parse(testYearString).getDateTime(), "YYYY parsing incorrectly");
        assertEquals(testMonth.getDateTime(), PartialDateTime.parse(testMonthString).getDateTime(), "YYYY-MM parsing incorrectly");
        assertEquals(testDay.getDateTime(), PartialDateTime.parse(testDayString).getDateTime(), "YYYY-MM-DD parsing incorrectly");
        assertEquals(testTime.getDateTime(), PartialDateTime.parse(testTimeString).getDateTime(), "YYYY-MM-DDTHH:MM:SSZ parsing incorrectly");
        assertEquals(testNano.getDateTime(), PartialDateTime.parse(testNanoString).getDateTime(), "YYYY-MM-DDTHH:MM:SS.NNNZ parsing incorrectly");
        assertEquals(testOffset.getDateTime(), PartialDateTime.parse(testOffsetString).getDateTime(), "YYYY-MM-DDTHH:MM:SS.NNN+ZZ:ZZ parsing incorrectly");
        assertEquals(testNanoLength.getDateTime(), PartialDateTime.parse(testNanoLengthString).getDateTime(), "YYYY-MM-DDTHH:MM:SS.NNN+ZZ:ZZ parsing incorrectly");
    }


    @Test
    void testPartialDateTimeEquality() {
        PartialDateTime pdt1 = new PartialDateTime(LocalDateTime.of(2000, 1, 1, 9, 0), PartialDateTime.Precision.YYYY_MM_DD);
        PartialDateTime pdt2 = new PartialDateTime(LocalDateTime.of(2000, 1, 1, 10, 0), PartialDateTime.Precision.YYYY_MM_DD);

        assertTrue(pdt1.equals(pdt2));
    }
}
