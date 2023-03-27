package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.endeavourhealth.imapi.logic.codegen.*;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ZFhirMapperEpisodeOfCare {
    public static void main(String[] argv) throws Exception {

        String pathToCsv = "d:\\pojo\\in\\Ten_rows\\episode_of_care.txt";
        int c = 1;

        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        try (IMPFiler filer = new IMPFilerCSV("d:\\pojo\\out\\Ten_rows\\episode_of_care_")) {

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

        ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.EpisodeOfCare.class, str);

        String fhirPatientType = "";
        String fhirPatientTypeTerm = "";
        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        Integer s = fhirExtension.size();
        // should only contain the registration type of the patient
        if (s > 0) {
            String url = fhirExtension.get(0).getUrl();
            IBaseDatatype valCoding = fhirExtension.get(0).getValue();
            if (valCoding instanceof CodingDt) {
                CodingDt z = (CodingDt) valCoding;
                fhirPatientType = z.getCode();
                fhirPatientTypeTerm = z.getDisplay();
            }
        }

        String startDate = "";
        String endDate = "";
        PeriodDt period = parsed.getPeriod();
        if (period instanceof PeriodDt) {
            if (period.getEnd() != null) {
                endDate = ZMapperCommon.FormatDate(period.getEnd());
            }
            if (period.getStart() != null) {
                startDate = ZMapperCommon.FormatDate(period.getStart());
            }
        }

        UUID fhirId = UUID.fromString(parsed.getId().getIdPart());

        EpisodeOfCare eoc = new EpisodeOfCare(fhirId);
        eoc.setPatientType(fhirPatientType);
        eoc.setProperty("patient-type-term", fhirPatientTypeTerm);

        if (!startDate.isEmpty()) {
            eoc.setEffectiveDate(PartialDateTime.parse(startDate));
        }
        if (!endDate.isEmpty()) {
            eoc.setEndDate(PartialDateTime.parse(endDate));
        }

        // Patient
        ResourceReferenceDt patientRef = parsed.getPatient();
        System.out.println(patientRef.getReference().getIdPart());
        UUID fhirNor = UUID.fromString(patientRef.getReference().getIdPart());
        Patient patient = new Patient(fhirNor);
        eoc.setPatient(patient.getId());

        // Practitioner
        ResourceReferenceDt careRef = parsed.getCareManager();
        System.out.println(careRef.getReference().getIdPart());
        UUID fhirPractitionerId = UUID.fromString(careRef.getReference().getIdPart());
        PractitionerInRole pract = new PractitionerInRole(fhirPractitionerId);
        // no om setter?

        // Organization
        ResourceReferenceDt orgRef = parsed.getManagingOrganization();
        System.out.println(orgRef.getReference().getIdPart());
        UUID fhirOragisationId = UUID.fromString(orgRef.getReference().getIdPart());
        Organisation organisation = new Organisation(fhirOragisationId);
        eoc.setRecordOwner(organisation.getId());

        result.add(eoc);

        return result;
    }
}
