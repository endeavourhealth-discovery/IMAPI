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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.endeavourhealth.imapi.logic.codegen.*;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.File;
import java.util.*;

public class ZFhirMapperEncounter {
    public static void main(String[] argv) throws Exception {

        String pathToCsv = "d:\\pojo\\in\\Ten_rows\\encounter.txt";
        int c = 1;

        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        try (IMPFiler filer = new IMPFilerCSV("d:\\pojo\\out\\Ten_rows\\encounter_")) {

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


    public static Collection<IMDMBase> RunMapper (String str, IParser parser) throws Exception {
        List<IMDMBase> result = new ArrayList<>();

        ca.uhn.fhir.model.dstu2.resource.Encounter parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Encounter.class, str);

        ResourceReferenceDt ref = parsed.getPatient();
        UUID fhirNor = UUID.fromString(ref.getReference().getIdPart());
        System.out.println(fhirNor);

        ref = parsed.getServiceProvider();
        UUID fhirOrg = UUID.fromString(ref.getReference().getIdPart());
        System.out.println(fhirOrg);

        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        Integer s = fhirExtension.size() - 1;
        String fhirClinDate = "";
        UUID fhirPractitioner = UUID.randomUUID();
        String fhirOrginialCode = "";
        String fhirOrginalTerm = "";
        String fhirOriginalScheme = "";

        String fhirText = "";
        for (int i = 0; i <= s; i++) {
            String url = fhirExtension.get(i).getUrl();
            //String fhirText = fhirExtension.get(i).

            IBaseDatatype refBase = fhirExtension.get(i).getValue();

            if (refBase instanceof DateTimeDt == false && url.contains("primarycare-recorded-by-extension")) {
                ResourceReferenceDt z = (ResourceReferenceDt) refBase;
                System.out.println(z.getReference().getValue());
                fhirPractitioner = UUID.fromString(z.getReference().getIdPart());
            }

            if (refBase instanceof DateTimeDt == true && url.contains("primarycare-recorded-date-extension")) {
                fhirClinDate = ((DateTimeDt) refBase).getValueAsString();
            }

            if (url.contains("primarycare-encounter-source")) {
                if (refBase instanceof CodeableConceptDt) {
                    CodeableConceptDt codeable = ((CodeableConceptDt) refBase);

                    fhirText = codeable.getText();

                    s = codeable.getCoding().size();
                    s--;
                    for (i = 0; i <= s; i++) {
                        // need to support an array list of CodeDt
                        CodingDt code = codeable.getCoding().get(0);
                        // CodingDt code = codeable.getCoding().get(i);
                        fhirOrginialCode = code.getCode();
                        fhirOrginalTerm = code.getDisplay();
                        fhirOriginalScheme = code.getSystem();

                        String system = code.getSystem();
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
                        ResourceReferenceDt obsRef = (((ListResource.Entry) En.get(i)).getItem());
                        System.out.println("linked obs: " + obsRef.getReference().getValue());
                        Observation obs = new Observation(UUID.fromString(obsRef.getReference().getIdPart()));
                        result.add(obs);
                    }
                }
            }

            java.util.List<Encounter.Participant> partIndividualList = parsed.getParticipant();
            s = partIndividualList.size() - 1;
            for (int i = 0; i <= s; i++) {
                if (partIndividualList.get(i).getIndividual() != null) {
                    ResourceReferenceDt indiviudual = partIndividualList.get(i).getIndividual();
                    System.out.println(indiviudual.getReference().getIdPart());
                }
                int si = partIndividualList.get(i).getType().size() - 1;
                for (int z = 0; z <= si; z++) {
                    java.util.List<CodingDt> coding = partIndividualList.get(i).getType().get(z).getCoding();
                    // la la la
                }
            }

            fhirClinDate = ZMapperCommon.Piece(fhirClinDate, "T", 1, 1);

            TerminologyConcept omConcept = new TerminologyConcept(UUID.randomUUID())
                    .setCode(fhirOrginialCode)
                    .setScheme(fhirOriginalScheme)
                    .setProperty("concept-term", fhirOrginalTerm);

            UUID fhirId = UUID.fromString(parsed.getId().getIdPart());
            Consultation encounter = new Consultation(fhirId)
                    .setEffectiveDate(PartialDateTime.parse(fhirClinDate))
                    .setProperty("concepts", Arrays.asList(omConcept));

            Organisation zorg = new Organisation(fhirOrg);
            encounter.setProvider(zorg.getId());
            //result.add(zorg); ??

            Patient znor = new Patient(fhirNor);
            encounter.setPatient(znor.getId());
            //result.add(znor); ??

            PractitionerInRole zpract = new PractitionerInRole(fhirPractitioner);
            encounter.setRecordOwner(fhirPractitioner);

            // om missing setter for practitioner
            //result.add(zpract); ??

            encounter.setText(fhirText);

            result.add(encounter);

            ObjectMapper om = new ObjectMapper();
            //om.registerModule(new JavaTimeModule());

            // Serialization test
            String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(encounter);
            System.out.println(json);
        }

        return result;
    }
}
