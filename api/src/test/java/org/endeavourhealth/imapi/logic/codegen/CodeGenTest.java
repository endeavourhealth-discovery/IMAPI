package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;

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
        class IMDMPatient extends IMDMBase<IMDMPatient> {
            IMDMPatient() {
                super("IMDMPatient");
            }

            public String getName() {
                return (String) properties.get("name");
            }

            public IMDMPatient setName(String name) {
                properties.put("name", name);
                return this;
            }
        }

        class IMDMAddress extends IMDMBase<IMDMAddress> {

            IMDMAddress() {
                super("IMDMAddress");
            }

            public String getPostcode() {
                return (String) properties.get("postcode");
            }

            public IMDMAddress setPostcode(String postcode) {
                properties.put("postcode", postcode);
                return this;
            }
        }

        IMDMAddress address = new IMDMAddress().setPostcode("NE1").setProperty("city", "London");

        IMDMPatient patient = new IMDMPatient().setName("Fred Bloggs").setProperty("age", 21)
                .setProperty("address", address);

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(patient);

        System.out.println(json);

        assertTrue(true);

    }
}