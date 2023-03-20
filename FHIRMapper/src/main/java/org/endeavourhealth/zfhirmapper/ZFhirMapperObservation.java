package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.Observation;
import org.endeavourhealth.imapi.logic.codegen.PartialDateTime;
import org.endeavourhealth.imapi.logic.codegen.Patient;
import org.endeavourhealth.imapi.logic.codegen.TerminologyConcept;
import org.hl7.fhir.instance.model.api.IBaseDatatype;

import java.util.List;
import java.util.UUID;

public class ZFhirMapperObservation {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");
        FhirContext ctx = FhirContext.forDstu2();

        IParser parser = ctx.newJsonParser();

        // blood pressure (includes sys and diastolic readings)
        //String str = "{\"code\":{\"coding\":[{\"code\":\"246..\",\"display\":\"O/E - blood pressure reading\",\"system\":\"http://read.info/readv2\"},{\"code\":163020007,\"display\":\"On examination - blood pressure reading (finding)\"},{\"code\":254063019,\"system\":\"http://www.endeavourhealth.org/fhir/snomed-description\"}],\"text\":\"O/E - blood pressure reading\"},\"component\":[{\"code\":{\"coding\":[{\"code\":\"2469.\",\"display\":\"O/E - Systolic BP reading\",\"system\":\"http://read.info/readv2\"},{\"code\":72313002,\"display\":\"Systolic arterial pressure (observable entity)\",\"system\":\"http://snomed.info/sct\"}]},\"valueQuantity\":{\"unit\":\"mmHg\",\"value\":120}},{\"code\":{\"coding\":[{\"code\":\"246A.\",\"display\":\"O/E - Diastolic BP reading\",\"system\":\"http://read.info/readv2\"},{\"code\":1091811000000102,\"display\":\"Diastolic arterial pressure (observable entity)\",\"system\":\"http://snomed.info/sct\"}]},\"valueQuantity\":{\"unit\":\"mmHg\",\"value\":70}}],\"effectiveDateTime\":\"2021-12-28\",\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner/2996\"}}],\"id\":515736,\"related\":[{\"target\":{\"reference\":\"Observation/515737\"},\"type\":\"has-member\"},{\"target\":{\"reference\":\"Observation/515738\"},\"type\":\"has-member\"}],\"resourceType\":\"Observation\",\"subject\":{\"reference\":\"Patient/321\"}}";

        // bp reading with reference to parent observation
        //String str = "{\"code\":{\"coding\":[{\"code\":\"2469.\",\"display\":\"O/E - Systolic BP reading\",\"system\":\"http://read.info/readv2\"},{\"code\":72313002,\"display\":\"Systolic arterial pressure (observable entity)\"}]},\"effectiveDateTime\":\"2021-12-28\",\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner/2996\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/parent-resource\",\"valueReference\":{\"reference\":\"Observation/515736\"}}],\"id\":515737,\"resourceType\":\"Observation\",\"subject\":{\"reference\":\"Patient/321\"},\"valueQuantity\":{\"unit\":\"mmHg\",\"value\":120}}";

        //String str = "{\"code\":{\"coding\":[{\"code\":\"1Y...\",\"display\":\"Patient feels well\",\"system\":\"READ 3\"},{\"code\":267112005,\"display\":\"Patient feels well (finding)\",\"system\":\"SNOMED\"}]},\"effectiveDateTime\":\"2006-06-10\",\"encounter\":{\"reference\":\"Encounter/1\"},\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueDateTime\":\"2006-06-10\"}],\"id\":1,\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-observation\"]},\"performer\":[{\"reference\":\"Practitioner/4089\"}],\"resourceType\":\"Observation\",\"subject\":{\"reference\":\"Patient/1\"}}";
        // Systolic
        //String str = "{\"code\":{\"coding\":[{\"code\":\"2469.\",\"display\":\"O/E - Systolic BP reading\",\"system\":\"http://read.info/readv2\"},{\"code\":72313002,\"display\":\"Systolic arterial pressure (observable entity)\"}]},\"effectiveDateTime\":\"2021-12-28\",\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner/2996\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/parent-resource\",\"valueReference\":{\"reference\":\"Observation/515736\"}}],\"id\":515737,\"resourceType\":\"Observation\",\"subject\":{\"reference\":\"Patient/321\"},\"valueQuantity\":{\"unit\":\"mmHg\",\"value\":120}}";
        // text
        String str = "{\"code\":{\"coding\":[{\"code\":\"136..\",\"display\":\"Alcohol consumption\",\"system\":\"READ 3\"},{\"code\":160573003,\"display\":\"Alcohol intake (observable entity)\",\"system\":\"SNOMED\"}],\"text\":\"Alcohol consumption\"},\"effectiveDateTime\":\"1995-09-20\",\"encounter\":{\"reference\":\"Encounter\\/17\"},\"extension\":[{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-by-extension\",\"valueDateTime\":\"1995-09-20\"}],\"id\":30,\"meta\":{\"profile\":[\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-observation\"]},\"performer\":[{\"reference\":\"Practitioner\\/4089\"}],\"resourceType\":\"Observation\",\"subject\":{\"reference\":\"Patient\\/1\"},\"valueQuantity\":{\"unit\":\"U\\/week\",\"value\":0}}";

        ca.uhn.fhir.model.dstu2.resource.Observation parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Observation.class, str);

        String fhirNor = parsed.getSubject().getReference().getIdPart();

        // encounter reference
        ResourceReferenceDt EncounterReference = parsed.getEncounter();
        System.out.println(EncounterReference.getReference().toString());
        String fhirEncounterRef = EncounterReference.getReference().getIdPart();

        // practitioner reference
        List<ResourceReferenceDt> PractitionerReference = parsed.getPerformer();
        if (PractitionerReference.size() > 0) {
            System.out.println(PractitionerReference.get(0).getReference().getValue());
        }

        // codes & terms
        Integer s = parsed.getCode().getCoding().size();
        s--; String fhirOriginalDisplay = ""; String fhirOriginalCode = ""; String fhirOriginalSystem = "";
        for (int i = 0; i <= s; i++) {
            fhirOriginalDisplay = parsed.getCode().getCoding().get(i).getDisplay();
            fhirOriginalCode= parsed.getCode().getCoding().get(i).getCode();
            fhirOriginalSystem = parsed.getCode().getCoding().get(i).getSystem();
            //System.out.println(fhirCodeDisplay);
            //System.out.println(fhirCode);
            //System.out.println(fhirSystem);
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
        System.out.println(clinDate.getValue().toString());

        String fhirClinDate = ZMapperCommon.FormatDate(clinDate.getValue());

        System.out.println(fhirClinDate);

        // parent observation (extension)
        String fhirParentObs = ""; String fhirPractitioner = "";

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

        System.out.println(fhirParentObs);
        System.out.println(fhirPractitioner);

        // id
        UUID fhirId = UUID.fromString(parsed.getId().getIdPart());

        //String x = "" + fhirId;
        //System.out.println(x);
        //System.out.println(fhirId);

        // patient reference (subject)
        ResourceReferenceDt subject = parsed.getSubject();
        //String nor = subject.getReference().getValue();
        UUID nor = UUID.fromString(subject.getReference().getIdPart());
        Patient patient = new Patient(nor);

        Observation observation = new Observation(fhirId)
                .setEffectiveDate(PartialDateTime.parse(fhirClinDate));

        observation.setPatient(patient.getId());

        if (!fhirParentObs.isEmpty())  observation.setProperty("custom-parent-observation", fhirParentObs);

        TerminologyConcept omConcept = new TerminologyConcept(UUID.randomUUID())
                .setCode(fhirOriginalCode)
                .setScheme(fhirOriginalSystem)
                .setProperty("concept-term", fhirOriginalDisplay);

        observation.setOriginalConcept(omConcept.getId());

        if (!fhirValue.isEmpty()) {
            observation.setProperty("custom-value-value", fhirValue);
            observation.setProperty("custom-value-units", fhirUnits);
        }

        if (fhirEncounterRef != null)  observation.setProperty("custom-encounter-reference", fhirEncounterRef);

        // no setter for Practitioner
        if (fhirPractitioner != null) {
        }

        if (fhirText !=null) {
            observation.setText(fhirText);
        }

        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(observation);

        System.out.println(json);
    }
}
