package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.logic.codegen.MedicationAuthorisation;
import org.endeavourhealth.imapi.logic.codegen.PartialDateTime;
import org.endeavourhealth.imapi.logic.codegen.Patient;
import org.endeavourhealth.imapi.logic.codegen.TerminologyConcept;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

public class ZFhirMedicationStatement {
    public static void main(String[] argv) throws Exception {
        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        //String str = "{\"dateAsserted\":\"1990-10-23\",\"dosage\":[{\"text\":\"3 times\\/day\"}],\"extension\":[{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-quantity-extension\",\"valueQuantity\":{\"unit\":\"capsule(s)\",\"value\":\"\"}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-type-extension\",\"valueCoding\":{\"code\":\"acute\",\"display\":\"Acute\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/ValueSet\\/primarycare-medication-authorisation-type\"}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner\\/1547\"}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-date-extension\",\"valueDateTime\":\"1990-10-23\"},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-firstissuedate-extension\",\"valueDate\":\"2011-03-17\"},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-mostrecentissuedate-extension\",\"valueDate\":\"2019-03-25\"},{\"extension\":[{\"url\":\"date\",\"valueDate\":\"2008-06-12\"}],\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-cancellation-extension\"}],\"id\":116625,\"informationSource\":{\"reference\":\"Practitioner\\/1547\"},\"medicationCodeableConcept\":{\"coding\":[{\"code\":317148009,\"display\":\"Alverine 60mg capsules\",\"system\":\"sct\"}]},\"patient\":{\"reference\":\"Patient\\/2633\"},\"resourceType\":\"MedicationStatement\"}";
        //String thingy = RunMapper(str, parser);

        String pathToCsv = "/media/sf_in/rx_statement.txt";
        String outFile = "/media/sf_in/rx_statement_out.txt";

        FileWriter csvWriter = new FileWriter(outFile);

        int c = 1;

        File file = new File(pathToCsv);
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        while (it.hasNext()) {
            String line = it.nextLine();
            String pojo = RunMapper(line, parser);
            csvWriter.append(c + "\t" + pojo + "\n");
            if (c%100 == 0) csvWriter.flush();
            System.out.println(c);
            c++;
        }

        csvWriter.close();
    }

    public static String RunMapper(String str, IParser parser) throws Exception {
        //str = "{\"resourceType\":\"MedicationStatement\",\"id\":\"084b71c2-baff-4291-bd34-f1e8a7b0b631\",\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation\"]},\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-quantity-extension\",\"valueQuantity\":{\"value\":10,\"unit\":\"ml\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-numberofrepeatsissued-extension\",\"valueInteger\":1},{\"extension\":[{\"url\":\"date\",\"valueDate\":\"2023-03-04\"}],\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-cancellation-extension\"},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner/b6e55044-9531-4bb2-bfd7-3db84b4b0b00\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-date-extension\",\"valueDateTime\":\"2023-01-20T08:14:58+00:00\"},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-type-extension\",\"valueCoding\":{\"system\":\"http://endeavourhealth.org/fhir/ValueSet/primarycare-medication-authorisation-type\",\"code\":\"acute\",\"display\":\"Acute\"}}],\"patient\":{\"reference\":\"Patient/9eaa8a53-6f83-4c41-9327-ba74ef80bfd4\"},\"informationSource\":{\"reference\":\"Practitioner/b6e55044-9531-4bb2-bfd7-3db84b4b0b00\"},\"dateAsserted\":\"2023-01-20\",\"status\":\"completed\",\"reasonForUseReference\":{\"reference\":\"Condition/fe770b45-445f-4375-b755-1a8ac4361397\"},\"medicationCodeableConcept\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"330286001\",\"display\":\"Chloramphenicol 0.5% eye drops\"}],\"text\":\"Chloramphenicol 0.5% eye drops\"},\"dosage\":[{\"text\":\"One Drop To Be Used In The Affected Eye(s) Four Times A Day\"}]}";
        //str = "{\"dateAsserted\":\"1994-07-15\",\"dosage\":[{\"text\":\"As Directed\"}],\"extension\":[{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-quantity-extension\",\"valueQuantity\":{\"unit\":\"pre-filled disposable injection\",\"value\":1}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-type-extension\",\"valueCoding\":{\"code\":\"repeat\",\"display\":\"Repeat\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/ValueSet\\/primarycare-medication-authorisation-type\"}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner\\/655\"}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-date-extension\",\"valueDateTime\":\"1994-07-15\"},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-numberofrepeatsissued-extension\",\"valueInteger\":1},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-firstissuedate-extension\",\"valueDate\":\"2015-03-28\"},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-mostrecentissuedate-extension\",\"valueDate\":\"2015-03-28\"},{\"extension\":[{\"url\":\"date\",\"valueDate\":\"2002-09-07\"}],\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-medication-authorisation-cancellation-extension\"}],\"id\":17071,\"informationSource\":{\"reference\":\"Practitioner\\/655\"},\"medicationCodeableConcept\":{\"coding\":[{\"code\":40085311000001103,\"display\":\"Adjuvanted quadrivalent influenza vaccine (surface antigen, inactivated) suspension for injection 0.5ml pre-filled syringes (Seqirus UK Ltd) (product)\",\"system\":\"sct\"}]},\"patient\":{\"reference\":\"Patient\\/203\"},\"resourceType\":\"MedicationStatement\"}";

        ca.uhn.fhir.model.dstu2.resource.MedicationStatement parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.MedicationStatement.class, str);

        String id = parsed.getId().getIdPart();

        // Patient
        ResourceReferenceDt patientRef = parsed.getPatient();
        String fhirNor = patientRef.getReference().getIdPart();

        ResourceReferenceDt practRef = parsed.getInformationSource();
        String fhirPract = practRef.getReference().getIdPart();

        String dateAsserted = ZMapperCommon.FormatDate(parsed.getDateAsserted());

        List<MedicationStatement.Dosage> doseage = parsed.getDosage();
        String dosageText = doseage.get(0).getText();

        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        Integer s = fhirExtension.size()-1;
        String fhirCancelDate = ""; String fhirQty = ""; String fhirUnit = "";
        String fhirRxTypeDisplay = ""; String fhirRxTypeCode = "";
        String fhirPractitioner = ""; String numberOfRepeatIssues = "";
        for ( int i=0 ; i<=s; i++) {
            String url = fhirExtension.get(i).getUrl();

            IBaseDatatype refBase = fhirExtension.get(i).getValue();

            if (refBase instanceof QuantityDt && url.equals("http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-quantity-extension")) {
                if (((QuantityDt) refBase).getValue() !=null ) {
                    fhirQty = ((QuantityDt) refBase).getValue().toString();
                }
                fhirUnit = ((QuantityDt) refBase).getUnit();
                System.out.println(fhirQty);
                System.out.println(fhirUnit);
            }

            if (url.equals("http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-type-extension")) {
                if (refBase instanceof CodingDt) {
                    CodingDt code = ((CodingDt) refBase);
                    System.out.println(code.getDisplay());
                    fhirRxTypeDisplay = code.getDisplay();
                    fhirRxTypeCode = code.getCode();
                }
            }

            if (url.contains("primarycare-medication-authorisation-numberofrepeatsissued-extension")) {
                System.out.println("number of issues");
                IntegerDt noofissues = (IntegerDt) refBase;
                numberOfRepeatIssues = noofissues.getValueAsString();
            }

            if (url.equals("http://endeavourhealth.org/fhir/StructureDefinition/primarycare-medication-authorisation-cancellation-extension")) {
                ExtensionDt tempExt  = fhirExtension.get(i).getExtension().get(0);
                System.out.println(tempExt.getUrl());
                IBaseDatatype baseValue = tempExt.getValue();
                if (baseValue instanceof DateDt) {
                    Date cancelDate = ((DateDt) baseValue).getValue();
                    fhirCancelDate = ZMapperCommon.FormatDate(cancelDate);
                }
            }

            if (url.equals("http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension")) {
                fhirPractitioner = ((ResourceReferenceDt) refBase).getReference().getIdPart();
            }
        }

        IBaseDatatype ref = parsed.getMedication();
        CodeableConceptDt codeable = ((CodeableConceptDt) ref);
        List<CodingDt> c = codeable.getCoding();

        String dcde = ""; String fhirDisplay = ""; String system = ""; String fhirText = "";
        if (c.size()> 0) {
            dcde = c.get(0).getCode();
            fhirDisplay = c.get(0).getDisplay();
            system = c.get(0).getSystem();
            fhirText = codeable.getText();
        }

        ObjectMapper om = new ObjectMapper();

        MedicationAuthorisation rx = new MedicationAuthorisation(id);

        TerminologyConcept omConcept = new TerminologyConcept()
                .setCode(dcde)
                .setScheme(system)
                .setProperty("custom-term", fhirDisplay);

        rx.setOriginalConcept(omConcept);
        rx.setDosage(dosageText);
        rx.setEffectiveDate(PartialDateTime.parse(dateAsserted));

        rx.setProperty("PractitionerInRole", fhirPractitioner);

        rx.setOrderQuantityUnits(fhirUnit);
        if (fhirQty.matches("\\\\d+")) {
            rx.setOrderQuantityNumberOfUnits(Integer.parseInt(fhirQty));
        }

        rx.setCourseType(fhirRxTypeCode);
        rx.setProperty("number-of-repeats-issued",numberOfRepeatIssues);

        Patient patient = new Patient(fhirNor);
        rx.setPatient(patient);

        // Serialization
        String json = om.writeValueAsString(rx);
        return json;
    }
}
