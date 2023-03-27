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
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.imapi.logic.codegen.Organisation;
import org.endeavourhealth.imapi.logic.codegen.PractitionerInRole;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ZFhirMapperPractitioner {
    public static void main(String[] argv) throws Exception {

        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        //String pathToCsv = "/media/sf_in/practitioner.txt";
        //String outFile = "/media/sf_in/practitioner_out.txt";

        int c = 1;

        String pathToCsv = "d:\\pojo\\in\\Ten_rows\\practitioner.txt";
        File file = new File(pathToCsv);
        try (IMPFiler filer = new IMPFilerCSV("d:\\pojo\\out\\Ten_rows\\practitioner_")) {
            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
            while (it.hasNext()) {

                String line = it.nextLine();
                Collection<IMDMBase>  pojos = RunMapper(line, parser);
                filer.fileIMPs(pojos);

                System.out.println(c);
                c++;
            }
        }
    }
    public static Collection<IMDMBase> RunMapper(String str, IParser parser) throws Exception {

        List<IMDMBase> result = new ArrayList<>();

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

        Organisation serviceOrganisation = new Organisation(fhirOrganizationId);

        UUID fhirId = UUID.fromString(parsed.getId().getIdPart());
        PractitionerInRole pract = new PractitionerInRole(fhirId)
                .setFamilyName(fhirFamily)
                .setCallingName(fhirGiven)
                .setRoleType(roleTypeCode)
                .setProperty("role-type-term", roleTypeTerm);

        pract.setServiceOrOrganisation(serviceOrganisation.getId().toString());

        result.add(pract);

        //serviceOrganisation.setIsCommissionedBy(serviceOrganisation.getId());
        //result.add(serviceOrganisation);

        return result;
    }
}
