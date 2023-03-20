package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.endeavourhealth.imapi.logic.codegen.Organisation;
import org.endeavourhealth.imapi.logic.codegen.PractitionerInRole;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class ZFhirMapperPractitioner {
    public static void main(String[] argv) throws Exception {

        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        String pathToCsv = "/media/sf_in/practitioner.txt";
        String outFile = "/media/sf_in/practitioner_out.txt";

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
        ca.uhn.fhir.model.dstu2.resource.Practitioner parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Practitioner.class, str);

        System.out.println(parsed.getId().getValue().toString());
        System.out.println(parsed.getId().getIdPart());

        NarrativeDt text = parsed.getText();

        System.out.println(parsed.getName().getText());
        System.out.println(parsed.getName().getUse());

        System.out.println(parsed.getText());
        System.out.println(parsed.getId().getIdPart());
        System.out.println(parsed.getActive());

        java.util.List<StringDt> fhirFamilyList = parsed.getName().getFamily();
        String fhirFamily = ""; String fhirGiven = "";
        int s = fhirFamilyList.size(); s--;
        for (int i=0 ; i<=s; i++) {
            System.out.println(fhirFamilyList.get(i));
            fhirFamily = fhirFamilyList.get(i).toString();
        }

        java.util.List<StringDt> fhirGivenList = parsed.getName().getGiven();
        s = fhirGivenList.size()-1;
        for (int i=0 ; i<=s; i++) {
            System.out.println(fhirGivenList.get(i));
            fhirGiven = fhirGiven + fhirGivenList.get(i) + " ";
        }
        int l = fhirGiven.length();
        fhirGiven = fhirGiven.substring(0, l - 1);

        java.util.List<Practitioner.PractitionerRole> fhirRolesList = parsed.getPractitionerRole();
        s= fhirRolesList.size()-1;
        UUID fhirOrganizationId = null;
        String roleTypeCode = ""; String roleTypeTerm = "";
        for ( int i=0 ; i<=s; i++) {

            if (fhirRolesList.get(i).getManagingOrganization() !=null) {
                ResourceReferenceDt managingOrg = parsed.getPractitionerRole().get(i).getManagingOrganization();
                System.out.println(managingOrg.getReference().getIdPart());
                fhirOrganizationId = UUID.fromString(managingOrg.getReference().getIdPart());
            }

            java.util.List<CodingDt> roles = fhirRolesList.get(i).getRole().getCoding();
            int z = roles.size()-1;
            for (int r =0 ; r<=z; r++) {
                CodingDt codes = roles.get(r);
                System.out.println(codes.getCode());
                roleTypeCode = codes.getCode();
                System.out.println(codes.getSystem());
                roleTypeTerm = codes.getDisplay();
                System.out.println(codes.getDisplay());
            }
        }

        Organisation organisation = new Organisation(fhirOrganizationId);

        UUID fhirId = UUID.fromString(parsed.getId().getIdPart());
        PractitionerInRole pract = new PractitionerInRole(fhirId)
                .setFamilyName(fhirFamily)
                .setCallingName(fhirGiven)
                .setServiceOrOrganisation(fhirOrganizationId.toString())
                .setRoleType(roleTypeCode)
                .setProperty("role-type-term", roleTypeTerm);

        ObjectMapper om = new ObjectMapper();

        // Serialization test
        String json = om.writeValueAsString(pract);
        return json;
    }
}
