package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.endeavourhealth.imapi.logic.codegen.*;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.io.File;
import java.util.*;

public class ZFhirMapperObservation {
    public static void main(String[] argv) throws Exception {
        FhirContext ctx = FhirContext.forDstu2();

        IParser parser = ctx.newJsonParser();

        String pathToCsv = "d:\\pojo\\in\\Ten_rows\\observation.txt";
        pathToCsv = "d:\\pojo\\in\\Ten_rows\\individual_bp.txt";
        //pathToCsv = "d:\\pojo\\in\\Ten_rows\\full_bp.txt";
        int c = 1;

        // observation_
        // observation_individual_bp_
        // observation_full_bp_
        try (IMPFiler filer = new IMPFilerCSV("d:\\pojo\\out\\Ten_rows\\observation_individual_bp_")) {

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

        ca.uhn.fhir.model.dstu2.resource.Observation parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Observation.class, str);

        String fhirNor = parsed.getSubject().getReference().getIdPart();

        // encounter reference
        ResourceReferenceDt EncounterReference = parsed.getEncounter();
        System.out.println(EncounterReference.getReference().toString());
        String fhirEncounterRef = EncounterReference.getReference().getIdPart();

        String fhirPractitioner = "";

        // practitioner reference
        List<ResourceReferenceDt> PractitionerReference = parsed.getPerformer();
        if (PractitionerReference.size() > 0) {
            System.out.println(PractitionerReference.get(0).getReference().getValue());
            fhirPractitioner = PractitionerReference.get(0).getReference().getIdPart();
        }

        // codes & terms
        Integer s = parsed.getCode().getCoding().size();
        s--; String fhirOriginalDisplay = ""; String fhirOriginalCode = ""; String fhirOriginalSystem = "";
        String snomedDisplay = ""; String snomedCode = "";
        String otherDisplay = ""; String otherCode = ""; String otherScheme = "";
        for (int i = 0; i <= s; i++) {
            fhirOriginalDisplay = parsed.getCode().getCoding().get(i).getDisplay();
            fhirOriginalCode= parsed.getCode().getCoding().get(i).getCode();
            fhirOriginalSystem = parsed.getCode().getCoding().get(i).getSystem();
            if (fhirOriginalSystem.toLowerCase().contains("snomed") && (snomedCode.isEmpty())) {
                snomedDisplay = fhirOriginalDisplay;
                snomedCode = fhirOriginalCode;
            }
            if (fhirOriginalSystem.toLowerCase().contains("read")) {
                otherDisplay = fhirOriginalDisplay;
                otherCode = fhirOriginalCode;
                otherScheme = fhirOriginalSystem;
            }
        }

        // go for the first orginal term in the array
        fhirOriginalDisplay = parsed.getCode().getCoding().get(0).getDisplay();
        fhirOriginalCode= parsed.getCode().getCoding().get(0).getCode();
        fhirOriginalSystem = parsed.getCode().getCoding().get(0).getSystem();

        String fhirText = parsed.getCode().getText();

        IBaseDatatype refBase = parsed.getValue();
        if (refBase instanceof QuantityDt) {
            String fhirQty = ((QuantityDt) refBase).getValue().toString();
            String fhirUnit = ((QuantityDt) refBase).getUnit();
        }

        // values
        IDatatype value = parsed.getValue();
        String fhirValue = ""; String fhirUnits = "";
        if (value instanceof QuantityDt) {
            QuantityDt quantity = (QuantityDt)value;
            System.out.println(quantity.getValue());
            fhirValue = quantity.getValue().toString();
            System.out.println(quantity.getUnit());
            fhirUnits = quantity.getUnit();
        }

        // clinical effective date
        IDatatype clinicalEffectDate = parsed.getEffective();
        DateTimeDt clinDate = (DateTimeDt)clinicalEffectDate;

        String fhirClinDate = ZMapperCommon.FormatDate(clinDate.getValue());

        // parent observation (extension)
        String fhirParentObs = "";

        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        if (fhirExtension.size() > 0) {
            s = fhirExtension.size() - 1;
            for (int i = 0; i <= s; i++) {
                String url = fhirExtension.get(i).getUrl();

                IBaseDatatype ref = fhirExtension.get(i).getValue();
                if (ref instanceof ResourceReferenceDt) {
                    ResourceReferenceDt z = (ResourceReferenceDt) ref;
                    System.out.println(z.getReference().getValue());

                    if (url.contains("parent-resource")) {
                        fhirParentObs = z.getReference().getIdPart();
                    }
                    if (url.contains("primarycare-recorded-by-extension")) {
                        fhirPractitioner = z.getReference().getIdPart();
                    }
                }
            }
        }

        UUID fhirId = UUID.fromString(parsed.getId().getIdPart());

        // patient reference (subject)
        ResourceReferenceDt subject = parsed.getSubject();
        UUID nor = UUID.fromString(subject.getReference().getIdPart());
        Patient patient = new Patient(nor);

        Observation observation = new Observation(fhirId)
                .setEffectiveDate(PartialDateTime.parse(fhirClinDate));

        observation.setPatient(patient.getId());

        if (!fhirParentObs.isEmpty())  observation.setProperty("custom_parent_observation", fhirParentObs);

        /*
        TerminologyConcept omConcept = new TerminologyConcept(UUID.randomUUID())
                .setCode(fhirOriginalCode)
                .setScheme(fhirOriginalSystem)
                .setProperty("concept-term", fhirOriginalDisplay);

        observation.setOriginalConcept(omConcept.getId())
                .setProperty("concepts", Arrays.asList(omConcept));
         */

        if (snomedCode != null) observation.setConcept("http://snomed.info/sct#"+snomedCode);

        if (otherCode != null) {
            observation.setProperty("custom_legacy_code", otherCode);
            observation.setProperty("custom_legacy_scheme", otherScheme);
            observation.setProperty("custom_legacy_term", otherDisplay);
        }

        if (!fhirValue.isEmpty()) {
            observation.setProperty("custom_value_value", fhirValue);
            observation.setProperty("custom_value_units", fhirUnits);
        }

        if (fhirEncounterRef != null)  observation.setProperty("custom_encounter_reference", fhirEncounterRef);

        // no setter for Practitioner
        if (fhirPractitioner != null) {
            observation.setProperty("custom_practitioner", fhirPractitioner);
        }

        if (snomedDisplay !=null) {
            observation.setText(snomedDisplay);
        }

        result.add(observation);

        return result;
    }
}
