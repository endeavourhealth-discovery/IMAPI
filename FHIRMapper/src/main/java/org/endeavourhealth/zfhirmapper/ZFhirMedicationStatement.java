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
import org.endeavourhealth.imapi.logic.codegen.*;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class ZFhirMedicationStatement {
    public static void main(String[] argv) throws Exception {
        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        //String pathToCsv = "/media/sf_in/rx_statement.txt";
        //String outFile = "/media/sf_in/rx_statement_out.txt";

        String pathToCsv = "D:\\pojo\\in\\Ten_rows\\rx_statement.txt";
        pathToCsv = "/media/sf_in/Ten_rows/rx_statement.txt";

        int c = 1;

        //try (IMPFiler filer = new IMPFilerCSV("d:\\pojo\\out\\Ten_rows\\rx_statement_")) {
        try (IMPFiler filer = new IMPFilerCSV("/tmp/rx_statement_")) {
            File file = new File(pathToCsv);
            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                Collection<IMDMBase> pojos = RunMapper(line, parser);
                filer.fileIMPs(pojos);

                if (c % 100 == 0) System.out.println(c);
                c++;
            }
        }
    }

    public static Collection<IMDMBase> RunMapper(String str, IParser parser) throws Exception {
        List<IMDMBase> result = new ArrayList<>();

        ca.uhn.fhir.model.dstu2.resource.MedicationStatement parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.MedicationStatement.class, str);

        UUID id = UUID.fromString(parsed.getId().getIdPart());

        // Patient
        ResourceReferenceDt patientRef = parsed.getPatient();
        UUID fhirNor = UUID.fromString(patientRef.getReference().getIdPart());

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

        MedicationAuthorisation rx = new MedicationAuthorisation(id);

        /*
        TerminologyConcept omConcept = new TerminologyConcept(UUID.randomUUID())
                .setCode(dcde)
                .setScheme(system)
                .setProperty("custom-term", fhirDisplay);

        rx.setOriginalConcept(omConcept.getId())
                .setProperty("concepts", Arrays.asList(omConcept));
         */

        rx.setProperty("custom_code", dcde);
        rx.setProperty("custom_term", fhirDisplay);
        rx.setProperty("custom_scheme", system);

        rx.setDosage(dosageText);
        rx.setEffectiveDate(PartialDateTime.parse(dateAsserted));

        rx.setProperty("custom_PractitionerInRole", fhirPractitioner);

        rx.setOrderQuantityUnits(fhirUnit);
        if (fhirQty.matches("\\\\d+")) {
            rx.setOrderQuantityNumberOfUnits(Integer.parseInt(fhirQty));
        }

        rx.setCourseType(fhirRxTypeCode);
        rx.setProperty("custom_number_of_repeats_issued",numberOfRepeatIssues);

        if (!fhirCancelDate.isEmpty()) {
            rx.setProperty("custom_cancelled_date", fhirCancelDate);
        }

        Patient patient = new Patient(fhirNor);
        rx.setPatient(patient.getId());

        result.add(rx);

        return result;
    }
}
