package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.Organisation;
import org.endeavourhealth.imapi.logic.codegen.PractitionerInRole;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ZFhirMapperPractitioner {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");
        String str = "{\"active\":true,\"id\":468,\"meta\":{\"profile\":[\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-practitioner\"]},\"name\":{\"family\":[\"Rumayor\"],\"given\":[\"Alfred\"],\"text\":\"Rumayor, Alfred\",\"use\":\"official\"},\"practitionerRole\":[{\"managingOrganization\":{\"reference\":\"Organization\\/156\"},\"role\":{\"coding\":[{\"code\":\"R0260\",\"display\":\"General Medical Practitioner\",\"system\":\"http:\\/\\/fhir.nhs.net\\/ValueSet\\/sds-job-role-name-1-0\"}]}}],\"resourceType\":\"Practitioner\"}";

        FhirContext ctx = FhirContext.forDstu2();

        IParser parser = ctx.newJsonParser();
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
        String fhirOrganizationId = ""; String roleTypeCode = ""; String roleTypeTerm = "";
        for ( int i=0 ; i<=s; i++) {

            if (fhirRolesList.get(i).getManagingOrganization() !=null) {
                ResourceReferenceDt managingOrg = parsed.getPractitionerRole().get(i).getManagingOrganization();
                System.out.println(managingOrg.getReference().getIdPart());
                fhirOrganizationId = managingOrg.getReference().getIdPart();
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

        String fhirId = parsed.getId().getIdPart();
        PractitionerInRole pract = new PractitionerInRole(fhirId)
                .setFamilyName(fhirFamily)
                .setCallingName(fhirGiven)
                .setServiceOrOrganisation(fhirOrganizationId)
                .setRoleType(roleTypeCode)
                .setProperty("role-type-term", roleTypeTerm);

        ObjectMapper om = new ObjectMapper();

        // Serialization test
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(pract);
        System.out.println(json);
    }
}
