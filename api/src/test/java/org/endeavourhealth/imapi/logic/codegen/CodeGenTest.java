package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.utils.Obj;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;

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

        List<IMDMAddress> list = new ArrayList<>();

        list.add(home);
        list.add(work);

        IMDMPatient patient = new IMDMPatient().setName("Fred Bloggs").setProperty("age", 21)
                .setProperty("address", list);

        ObjectMapper om = new ObjectMapper();

        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(patient);

        IMDMPatient actual = om.readValue(json, IMDMPatient.class);

        assertEquals(patient.getName(), actual.getName(), "Deserialized name not equal");
        assertEquals(patient.getProperty("age").toString(), actual.getProperty("age").toString(), "Deserialized age not equal");

        List<IMDMAddress> outputList = actual.getProperty("address");

        assertEquals(list.get(0).getPostcode(), outputList.get(0).getPostcode(), "Deserialized postcode not equal");
        assertNotEquals(list.get(0).getPostcode(), outputList.get(1).getPostcode(), "Deserialized postcode not equal");

    }
}