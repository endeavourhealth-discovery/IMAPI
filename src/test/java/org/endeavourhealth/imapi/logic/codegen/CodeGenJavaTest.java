package org.endeavourhealth.imapi.logic.codegen;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CodeGenJavaTest {

    @Test
    void capitalise_ShouldCapitaliseSingleWord() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("Word", codeGen.capitalise("word"));
    }

    @Test
    void capitalise_ShouldCapitaliseMultipleWords() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("MultipleWords", codeGen.capitalise("multiple words"));
    }

    @Test
    void capitalise_ShouldHandleQuotesAndSpecialChars() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("NameWithName", codeGen.capitalise("\"name\" with name"));
    }

    @Test
    void capitalise_ShouldThrowExceptionOnNull() {
        CodeGenJava codeGen = new CodeGenJava();
        assertThrows(IllegalArgumentException.class, () -> codeGen.capitalise(null));
    }

    @Test
    void separate_ShouldSeparateCamelCase() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("word", codeGen.separate("Word"));
        assertEquals("multiple words", codeGen.separate("MultipleWords"));
    }

    @Test
    void separate_ShouldThrowExceptionOnNull() {
        CodeGenJava codeGen = new CodeGenJava();
        assertThrows(IllegalArgumentException.class, () -> codeGen.separate(null));
    }

    @Test
    void capitalizeFirstCharacter_ShouldCapitalizeAndSeparate() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("Patient name", codeGen.capitalizeFirstCharacter("PatientName"));
    }

    @Test
    void getSuffix_ShouldReturnPartAfterHash() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("integer", codeGen.getSuffix("http://www.w3.org/2001/XMLSchema#integer"));
        assertEquals("string", codeGen.getSuffix("http://www.w3.org/2001/XMLSchema#string"));
    }

    @Test
    void getDataType_ShouldReturnXsdType() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("String", codeGen.getDataType(iri("http://www.w3.org/2001/XMLSchema#string"), false, false));
        assertEquals("Integer", codeGen.getDataType(iri("http://www.w3.org/2001/XMLSchema#integer"), false, false));
    }

    @Test
    void getDataType_ShouldReturnUUIDForDataModel() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("UUID", codeGen.getDataType(iri("http://endhealth.info/im#SomeModel"), true, false));
    }

    @Test
    void getDataType_ShouldReturnList() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("List<String>", codeGen.getDataType(iri("http://www.w3.org/2001/XMLSchema#string"), false, true));
    }

    @Test
    void getDataType_ShouldHandleSpecialIris() {
        CodeGenJava codeGen = new CodeGenJava();
        assertEquals("String", codeGen.getDataType(iri("http://endhealth.info/im#Status"), false, false));
        assertEquals("PartialDateTime", codeGen.getDataType(iri("http://endhealth.info/im#DateTime"), false, false));
    }

    @Test
    void generate_ShouldGenerateZipWithJavaFiles() throws IOException {
        try (MockedStatic<IMDB> mockedImdb = mockStatic(IMDB.class)) {
            IMDB mockConn = mock(IMDB.class);
            mockedImdb.when(IMDB::getConnection).thenReturn(mockConn);

            // Mock getModelList
            TupleQuery modelListQuery = mock(TupleQuery.class);
            TupleQueryResult modelListResult = mock(TupleQueryResult.class);
            when(mockConn.prepareTupleSparql(contains("SELECT ?iri"))).thenReturn(modelListQuery);
            when(modelListQuery.evaluate()).thenReturn(modelListResult);
            when(modelListResult.hasNext()).thenReturn(true, false);
            BindingSet modelBinding = mock(BindingSet.class);
            Value modelIriValue = mock(Value.class);
            when(modelListResult.next()).thenReturn(modelBinding);
            when(modelBinding.getValue("iri")).thenReturn(modelIriValue);
            when(modelIriValue.stringValue()).thenReturn("http://endhealth.info/im#Patient");

            // Mock getDataModel for Patient
            TupleQuery patientQuery = mock(TupleQuery.class);
            TupleQueryResult patientResult = mock(TupleQueryResult.class);
            when(mockConn.prepareTupleSparql(contains("SELECT ?iri ?model ?comment ?propname"))).thenReturn(patientQuery);
            when(patientQuery.evaluate()).thenReturn(patientResult);
            when(patientResult.hasNext()).thenReturn(true, false);
            BindingSet patientBinding = mock(BindingSet.class);
            when(patientResult.next()).thenReturn(patientBinding);
            
            Value patientNameValue = mock(Value.class);
            when(patientBinding.getValue("model")).thenReturn(patientNameValue);
            when(patientNameValue.stringValue()).thenReturn("Patient");
            
            Value propNameValue = mock(Value.class);
            when(patientBinding.getValue("propname")).thenReturn(propNameValue);
            when(propNameValue.stringValue()).thenReturn("name");
            
            Value typeValue = mock(Value.class);
            when(patientBinding.getValue("type")).thenReturn(typeValue);
            when(typeValue.stringValue()).thenReturn("http://www.w3.org/2001/XMLSchema#string");
            
            Literal dmValue = mock(Literal.class);
            when(patientBinding.getValue("dm")).thenReturn(dmValue);
            when(dmValue.booleanValue()).thenReturn(false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                CodeGenJava codeGen = new CodeGenJava();
                codeGen.generate(zos);
            }

            byte[] zipBytes = baos.toByteArray();
            assertNotNull(zipBytes);
            assertTrue(zipBytes.length > 0);

            try (ZipInputStream zis = new ZipInputStream(new java.io.ByteArrayInputStream(zipBytes))) {
                java.util.zip.ZipEntry entry = zis.getNextEntry();
                assertNotNull(entry);
                assertEquals("Patient.java", entry.getName());
                
                String content = new String(zis.readAllBytes());
                assertTrue(content.contains("public class Patient extends IMDMBase<Patient>"));
                assertTrue(content.contains("public String getName()"));
                assertTrue(content.contains("public Patient setName(String name)"));
            }
        }
    }
}
