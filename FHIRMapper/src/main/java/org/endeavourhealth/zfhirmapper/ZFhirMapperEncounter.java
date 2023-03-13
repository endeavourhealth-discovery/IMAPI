package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContainedDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.ListResource;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.*;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.*;

public class ZFhirMapperEncounter {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");

        // 2023-02-03T13:15:29+00:00
        String str = "{\"id\": 1,\"contained\":[{\"entry\":[{\"item\":{\"reference\":\"Observation\\/1\"}}],\"id\":\"Items\",\"resourceType\":\"List\"}],\"extension\":[{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner\\/2173\"}},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-recorded-date-extension\",\"valueDateTime\":\"1991-08-19T13:15:29+00:00\"},{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-encounter-source\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"EMISNQRE82\",\"display\":\"g.p.surgery\",\"system\":\"emis-code\"}]}}],\"patient\":{\"reference\":\"Patient\\/1\"},\"resourceType\":\"Encounter\",\"serviceProvider\":{\"reference\":\"Organization\\/258\"}}";

        str = "{\"resourceType\":\"Encounter\",\"id\":\"140b9083-0229-4138-a42d-ef7613479d48\",\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-encounter\"]},\"contained\":[{\"resourceType\":\"List\",\"id\":\"Items\",\"entry\":[{\"item\":{\"reference\":\"Observation/115b3a6f-53b2-44d7-ae94-a348ec0da607\"}}]}],\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner/aeb875fa-d9a8-40e2-9e2d-cfe3a21b1f36\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-date-extension\",\"valueDateTime\":\"2023-02-03T13:15:29+00:00\"},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-encounter-source\",\"valueCodeableConcept\":{\"coding\":[{\"system\":\"http://www.endeavourhealth.org/fhir/emis-code\",\"code\":\"EMISNQRE82\",\"display\":\"GP Surgery\"},{\"system\":\"http://snomed.info/sct\",\"code\":\"1672871000006105\"},{\"system\":\"http://www.endeavourhealth.org/fhir/snomed-description\",\"code\":\"1672871000006114\"}],\"text\":\"GP Surgery\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-encounter-components-extension\",\"valueReference\":{\"reference\":\"#Items\"}}],\"status\":\"finished\",\"patient\":{\"reference\":\"Patient/9eaa8a53-6f83-4c41-9327-ba74ef80bfd4\"},\"episodeOfCare\":[{\"reference\":\"EpisodeOfCare/1be878b3-8e69-4b82-a75e-2f955899b105\"}],\"participant\":[{\"type\":[{\"coding\":[{\"system\":\"http://hl7.org/fhir/ValueSet/encounter-participant-type\",\"code\":\"PPRF\",\"display\":\"primary performer\"}]}],\"individual\":{\"reference\":\"Practitioner/aeb875fa-d9a8-40e2-9e2d-cfe3a21b1f36\"}}],\"period\":{\"start\":\"2023-02-03\"},\"serviceProvider\":{\"reference\":\"Organization/7e0c5e1c-cfdf-412d-bf51-51040d955b31\"}}";

        str = "{\"contained\":[{\"entry\":[{\"item\":{\"reference\":\"Observation/446952\"}},{\"item\":{\"reference\":\"Observation/446953\"}},{\"item\":{\"reference\":\"Observation/446954\"}},{\"item\":{\"reference\":\"Observation/446955\"}},{\"item\":{\"reference\":\"Observation/446956\"}},{\"item\":{\"reference\":\"Observation/446957\"}},{\"item\":{\"reference\":\"Observation/446958\"}},{\"item\":{\"reference\":\"Observation/446959\"}},{\"item\":{\"reference\":\"Observation/446960\"}},{\"item\":{\"reference\":\"Observation/446961\"}},{\"item\":{\"reference\":\"Observation/446962\"}},{\"item\":{\"reference\":\"Observation/446963\"}},{\"item\":{\"reference\":\"Observation/446964\"}},{\"item\":{\"reference\":\"Observation/446965\"}},{\"item\":{\"reference\":\"Observation/446966\"}},{\"item\":{\"reference\":\"Observation/446967\"}},{\"item\":{\"reference\":\"Observation/446968\"}},{\"item\":{\"reference\":\"Observation/446969\"}},{\"item\":{\"reference\":\"Observation/446970\"}},{\"item\":{\"reference\":\"Observation/446971\"}},{\"item\":{\"reference\":\"Observation/446972\"}},{\"item\":{\"reference\":\"Observation/446973\"}},{\"item\":{\"reference\":\"Observation/446974\"}},{\"item\":{\"reference\":\"Observation/446975\"}},{\"item\":{\"reference\":\"Observation/446976\"}},{\"item\":{\"reference\":\"Observation/446977\"}},{\"item\":{\"reference\":\"Observation/446978\"}},{\"item\":{\"reference\":\"Observation/446979\"}},{\"item\":{\"reference\":\"Observation/446980\"}},{\"item\":{\"reference\":\"Observation/446981\"}},{\"item\":{\"reference\":\"Observation/446982\"}},{\"item\":{\"reference\":\"Observation/446983\"}},{\"item\":{\"reference\":\"Observation/446984\"}},{\"item\":{\"reference\":\"Observation/446985\"}},{\"item\":{\"reference\":\"Observation/446986\"}},{\"item\":{\"reference\":\"Observation/446987\"}}],\"id\":\"Items\",\"resourceType\":\"List\"}],\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-by-extension\",\"valueReference\":{\"reference\":\"Practitioner/570\"}},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-recorded-date-extension\",\"valueDateTime\":\"2003-08-04\"},{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-encounter-source\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"EMISNQRE82\",\"display\":\"g.p.surgery\",\"system\":\"emis-code\"},{\"code\":1672871000006114,\"system\":\"http://www.endeavourhealth.org/fhir/snomed-description\"}],\"text\":\"GP Surgery\"}}],\"id\":219732,\"patient\":{\"reference\":\"Patient/844\"},\"resourceType\":\"Encounter\",\"serviceProvider\":{\"reference\":\"Organization/207\"}}";

        FhirContext ctx = FhirContext.forDstu2();

        IParser parser = ctx.newJsonParser();
        ca.uhn.fhir.model.dstu2.resource.Encounter parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Encounter.class, str);

        // patient_id
        ResourceReferenceDt ref = parsed.getPatient();
        String fhirNor = ref.getReference().getIdPart();
        System.out.println(fhirNor);

        ref = parsed.getServiceProvider();
        //String org = ref.getReference().getValue();
        String fhirOrg = ref.getReference().getIdPart();
        System.out.println(fhirOrg);

        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        Integer s = fhirExtension.size()-1;
        String fhirClinDate = ""; String fhirPractitioner = "";
        String fhirOrginialCode = ""; String fhirOrginalTerm = ""; String fhirOriginalScheme = "";

        String fhirText = "";
        for ( int i=0 ; i<=s; i++) {
            String url = fhirExtension.get(i).getUrl();
            //String fhirText = fhirExtension.get(i).

            IBaseDatatype refBase = fhirExtension.get(i).getValue();

            if (refBase instanceof DateTimeDt == false && url.contains("primarycare-recorded-by-extension")) {
                ResourceReferenceDt z = (ResourceReferenceDt) refBase;
                System.out.println(z.getReference().getValue());
                fhirPractitioner = z.getReference().getIdPart();
            }

            if (refBase instanceof DateTimeDt == true && url.contains("primarycare-recorded-date-extension")) {
                fhirClinDate = ((DateTimeDt) refBase).getValueAsString();
            }

            if (url.contains("primarycare-encounter-source")) {
                if (refBase instanceof CodeableConceptDt) {
                    CodeableConceptDt codeable = ((CodeableConceptDt) refBase);

                    fhirText = codeable.getText();

                    s= codeable.getCoding().size();
                    s--;
                    for (i=0 ; i<=s; i++) {
                        // need to support an array list of CodeDt
                        CodingDt code = codeable.getCoding().get(0);
                        // CodingDt code = codeable.getCoding().get(i);
                        fhirOrginialCode = code.getCode();
                        fhirOrginalTerm = code.getDisplay();
                        fhirOriginalScheme = code.getSystem();

                        String system = code.getSystem();
                        System.out.println(fhirOrginialCode);
                        System.out.println(fhirOrginalTerm);
                        System.out.println(system);
                    }
                }
            }
        }

        System.out.println(fhirPractitioner);
        System.out.println(fhirClinDate);

        // observations linked to consultation
        ContainedDt contained = parsed.getContained();
        List<IResource> e = contained.getContainedResources();

        for (IBaseResource nextBaseRes : e) {
            IResource next = (IResource) nextBaseRes;

            if (next instanceof ListResource) {
                ListResource l = ((ListResource) next);
                List En = l.getEntry();
                s = En.size();
                s--;
                for (int i = 0; i <= s; i++) {
                    System.out.println(En.get(i).getClass().getName());
                    if (En.get(i) instanceof ListResource.Entry) {
                        // En.get(0).getClass(ListResource.Entry.class);
                        ResourceReferenceDt obsRef = (((ListResource.Entry) En.get(i)).getItem());
                        System.out.println("linked obs: " + obsRef.getReference().getValue());
                    }
                }
            }

            System.out.println(next.getId());

            /*
            Set<String> containedIds = new HashSet<String>();

            for (IResource nextContained : next.getContained().getContainedResources()) {
                System.out.println("here");
            }
            */

            java.util.List<Encounter.Participant> partIndividualList = parsed.getParticipant();
            s = partIndividualList.size() - 1;
            for (int i = 0; i <= s; i++) {
                if (partIndividualList.get(i).getIndividual() !=null) {
                    ResourceReferenceDt indiviudual = partIndividualList.get(i).getIndividual();
                    System.out.println(indiviudual.getReference().getIdPart());
                }
                int si = partIndividualList.get(i).getType().size()-1;
                for (int z=0; z<=si; z++) {
                    java.util.List<CodingDt> coding = partIndividualList.get(i).getType().get(z).getCoding();
                    // la la la
                }
            }

            //fhirClinDate = ZMapperCommon.Piece(fhirClinDate, "\\+", 1 ,1);
            fhirClinDate = ZMapperCommon.Piece(fhirClinDate, "T", 1 ,1);
            //LocalDateTime omClinDate = LocalDateTime.parse(fhirClinDate);
            // LocalDate omClinDate = LocalDate.parse(fhirClinDate);

            TerminologyConcept omConcept = new TerminologyConcept()
                    .setCode(fhirOrginialCode)
                    .setScheme(fhirOriginalScheme)
                    .setProperty("concept-term", fhirOrginalTerm);

            // omConcept.setComment("test comment");
            // omConcept.setHasTermCode()

            String fhirId = parsed.getId().getIdPart();
            Consultation encounter = new Consultation(fhirId)
                    .setEffectiveDate(PartialDateTime.parse(fhirClinDate))
                    .setProperty("concepts", Arrays.asList(omConcept));

            // ArrayList

            Organisation zorg = new Organisation(fhirOrg);
            encounter.setProvider(zorg);

            Patient znor = new Patient(fhirNor);
            encounter.setPatient(znor);

            PractitionerInRole zpract = new PractitionerInRole(fhirPractitioner);
            // om missing setter for practitioner

            encounter.setText(fhirText);

            ObjectMapper om = new ObjectMapper();
            //om.registerModule(new JavaTimeModule());

            // Serialization test
            String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(encounter);
            System.out.println(json);
        }
    }
}
