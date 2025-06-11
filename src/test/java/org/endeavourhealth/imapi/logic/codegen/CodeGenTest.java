package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.endeavourhealth.imapi.model.codegen.IMDMBase;
import org.endeavourhealth.imapi.model.codegen.PartialDateTime;
import org.endeavourhealth.imapi.model.codegen.Precision;
import org.endeavourhealth.imapi.model.codegen.TimeUnits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void testSeparate() {
    String fieldName = codeGen.separate("MedicationRequestPrescription");
    String expectedOutput = "medication request prescription";
    assertEquals(expectedOutput, fieldName, "Name separated correctly");
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

    Address home = new Address(UUID.fromString("a8039ce3-46da-4756-8a61-da26f8e8af21")).setPostCode("NE1").setProperty("city", "London");
    Address work = new Address(UUID.fromString("ca948dc1-80df-41fd-9911-bde9dcfce65c")).setPostCode("LS1").setProperty("city", "Leeds");

    Patient patient = new Patient(UUID.fromString("964fa5a1-aca8-4fd7-b2de-2aa4bbd005b1"))
      .setForenames("Fred Bloggs")
      .setDateOfBirth("1973-09-26")
      .setProperty("age", 21)
      .setProperty("address", Arrays.asList(
        new Address(UUID.fromString("a8039ce3-46da-4756-8a61-da26f8e8af21")),
        new Address(UUID.fromString("ca948dc1-80df-41fd-9911-bde9dcfce65c"))
      ));

    List<IMDMBase> resources = Arrays.asList(
      patient,
      home,
      work
    );

    ObjectMapper om = new ObjectMapper();
    om.registerModule(new JavaTimeModule());
    String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(resources);

    System.out.println(json);

    List<IMDMBase> actualResources = om.readValue(json, new TypeReference<>() {
    });
    assertEquals(resources.size(), actualResources.size());

    Patient actual = (Patient) actualResources.get(0);
    assertEquals(patient.getForenames(), actual.getForenames(), "Deserialized name not equal");
    assertEquals(patient.getDateOfBirth(), actual.getDateOfBirth(), "Deserialized DOB not equal");
    assertEquals(patient.getProperty("age").toString(), actual.getProperty("age").toString(), "Deserialized age not equal");

    Address actualHome = (Address) resources.get(1);
    assertEquals(home.getPostCode(), actualHome.getPostCode(), "Deserialized postcode not equal");

    Address actualWork = (Address) resources.get(2);
    assertEquals(work.getPostCode(), actualWork.getPostCode(), "Deserialized postcode not equal");

  }


  @Test
  void testPartialDateTime() {

    PartialDateTime testYear = new PartialDateTime(new TimeUnits(1980), Precision.YYYY, ZoneOffset.UTC);
    String testYearString = "1980";
    PartialDateTime testMonth = new PartialDateTime(new TimeUnits(1980, 1), Precision.YYYY_MM, ZoneOffset.UTC);
    String testMonthString = "1980-01";
    PartialDateTime testDay = new PartialDateTime(new TimeUnits(1980, 1, 5), Precision.YYYY_MM_DD, ZoneOffset.UTC);
    String testDayString = "1980-01-05";
    PartialDateTime testTime = new PartialDateTime(new TimeUnits(1980, 1, 5, 2, 3, 24), Precision.YYYY_MM_DD_HH_MM_SS, ZoneOffset.UTC);
    String testTimeString = "1980-01-05T02:03:24Z";
    PartialDateTime testNano = new PartialDateTime(new TimeUnits(1980, 1, 5, 2, 3, 24, 55), Precision.YYYY_MM_DD_HH_MM_SS_NNN, ZoneOffset.UTC);
    String testNanoString = "1980-01-05T02:03:24.55Z";
    PartialDateTime testOffset = new PartialDateTime(new TimeUnits(1980, 1, 5, 2, 3, 24, 535), Precision.YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ, ZoneOffset.of("+02:00"));
    String testOffsetString = "1980-01-05T02:03:24.535+02:00";
    PartialDateTime testNanoLength = new PartialDateTime(new TimeUnits(1980, 1, 5, 23, 30, 4, 5), Precision.YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ, ZoneOffset.of("+02:00"));
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
    PartialDateTime pdt1 = new PartialDateTime(LocalDateTime.of(2000, 1, 1, 9, 0), Precision.YYYY_MM_DD);
    PartialDateTime pdt2 = new PartialDateTime(LocalDateTime.of(2000, 1, 1, 10, 0), Precision.YYYY_MM_DD);

    assertEquals(pdt1, pdt2);
  }
}
