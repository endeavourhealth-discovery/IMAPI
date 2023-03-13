package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.*;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.util.List;

public class ZFhirMapperEpisodeOfCare {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");
        String str = "{\"careManager\":{\"reference\":\"Practitioner\\/1\"},\"extension\":[{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-patient-registration-type-extension\",\"valueCoding\":{\"code\":\"R\",\"display\":\"Regular\\/GMS\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/ValueSet\\/primarycare-patient-registration-type\"}}],\"id\":1,\"managingOrganization\":{\"reference\":\"Organization\\/258\"},\"patient\":{\"reference\":\"Patient\\/1\"},\"period\":{\"start\":\"1994-05-25T00:00:00+00:00\"},\"resourceType\":\"EpisodeOfCare\",\"status\":\"active\"}";

        FhirContext ctx = FhirContext.forDstu2();

        IParser parser = ctx.newJsonParser();

        ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare.class, str);

        String fhirPatientType = ""; String fhirPatientTypeTerm = "";
        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        Integer s = fhirExtension.size();
        // should only contain the registration type of the patient
        if (s>0) {
            System.out.println("yes, we have an extension");
            String url = fhirExtension.get(0).getUrl();
            System.out.println(url);
            IBaseDatatype valCoding = fhirExtension.get(0).getValue();
            if (valCoding instanceof CodingDt) {
                CodingDt z = (CodingDt) valCoding;
                System.out.println(z.getSystem());
                System.out.println(z.getCode());
                System.out.println(z.getDisplay());
                fhirPatientType = z.getCode();
                fhirPatientTypeTerm = z.getDisplay();
            }
        }

        String startDate = ""; String endDate = "";
        PeriodDt period = parsed.getPeriod();
        if (period instanceof PeriodDt) {
            //System.out.println("yes, we have a start and end date");
            if (period.getEnd() != null) {
                endDate = ZMapperCommon.FormatDate(period.getEnd());
            }
            if (period.getStart()!=null) {
                startDate = ZMapperCommon.FormatDate(period.getStart());
            }
        }

        String fhirId = parsed.getId().getIdPart();

        EpisodeOfCare eoc = new EpisodeOfCare(fhirId);
        eoc.setPatientType(fhirPatientType);
        eoc.setProperty("patient-type-term", fhirPatientTypeTerm);

        if (!startDate.isEmpty()) {
            //if (ZMapperCommon.Piece(startDate,"T",2,2).isEmpty()) {
            //    startDate = startDate + "T00:00:00";
            //}
            //LocalDateTime omStart = LocalDateTime.parse(startDate);
            //LocalDateTime omStart = LocalDateTime.parse("1994-05-25T00:00:00");
            //LocalDateTime omStart = LocalDateTime.parse("1994-5-25T00:00:00");
            //eoc.setEffectiveDate(omStart);
            eoc.setEffectiveDate(PartialDateTime.parse(startDate));
        }
        if (!endDate.isEmpty()) { eoc.setEndDate(PartialDateTime.parse(endDate));}

        // Patient
        ResourceReferenceDt patientRef = parsed.getPatient();
        System.out.println(patientRef.getReference().getIdPart());
        String fhirNor = patientRef.getReference().getIdPart();
        Patient patient = new Patient(fhirNor);
        eoc.setPatient(patient);

        // Practitioner
        ResourceReferenceDt careRef = parsed.getCareManager();
        System.out.println(careRef.getReference().getIdPart());
        String fhirPractitionerId = careRef.getReference().getIdPart();
        PractitionerInRole pract = new PractitionerInRole(fhirPractitionerId);
        // no om setter?

        // Organization
        ResourceReferenceDt orgRef = parsed.getManagingOrganization();
        System.out.println(orgRef.getReference().getIdPart());
        String fhirOragisationId = orgRef.getReference().getIdPart();
        Organisation organisation = new Organisation(fhirOragisationId);
        eoc.setRecordOwner(organisation);

        ObjectMapper om = new ObjectMapper();
        //om.registerModule(new JavaTimeModule());

        // Serialization test
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(eoc);
        System.out.println(json);
    }
}
