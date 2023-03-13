package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.Organisation;
import org.endeavourhealth.imapi.model.imq.Bool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.ref.Reference;
import java.util.List;

public class ZFhirMapperOrganization {
    public static void main(String[] argv) throws Exception {
        String str = ""; String pathToCsv = "C:\\Users\\Paul\\Desktop\\pojo\\in\\organizations.txt";
        String outFile = "C:\\Users\\Paul\\Desktop\\pojo\\out\\organizations.txt";
        FileWriter csvWriter = new FileWriter(outFile);
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        while ((str = csvReader.readLine()) != null) {
            //String str = "{\"active\":true,\"id\":21,\"identifier\":[{\"system\":\"http:\\/\\/fhir.nhs.net\\/Id\\/ods-organization-code\",\"use\":\"official\",\"value\":\"X5124\"}],\"meta\":{\"profile\":[\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-organization\"]},\"name\":\"PRACTICE 21\",\"partOf\":{\"reference\":\"Organization\\/5\"},\"resourceType\":\"Organization\",\"type\":{\"coding\":[{\"code\":\"PR\",\"display\":\"GP PRACTICE\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/ValueSet\\/primarycare-organization-type\"}]}}";
            String pojo = RunMapper(str);
            csvWriter.append(pojo + "\n");
            System.out.println(pojo);
        }
        csvWriter.close();
    }

    public static String RunMapper(String str) throws Exception {
        FhirContext ctx = FhirContext.forDstu2();

        IParser parser = ctx.newJsonParser();

        //String str = "{\"active\":true,\"id\":12,\"identifier\":[{\"system\":\"http:\\/\\/fhir.nhs.net\\/Id\\/ods-organization-code\",\"use\":\"official\",\"value\":\"U0300\"}],\"meta\":{\"profile\":[\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-organization\"]},\"name\":\"PRACTICE 12\",\"partOf\":{\"reference\":\"Organization\\/4\"},\"resourceType\":\"Organization\",\"type\":{\"coding\":[{\"code\":\"PR\",\"display\":\"GP PRACTICE\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/ValueSet\\/primarycare-organization-type\"}]}}";
        //str = "{\"active\":true,\"id\":21,\"identifier\":[{\"system\":\"http:\\/\\/fhir.nhs.net\\/Id\\/ods-organization-code\",\"use\":\"official\",\"value\":\"X5124\"}],\"meta\":{\"profile\":[\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-organization\"]},\"name\":\"PRACTICE 21\",\"partOf\":{\"reference\":\"Organization\\/5\"},\"resourceType\":\"Organization\",\"type\":{\"coding\":[{\"code\":\"PR\",\"display\":\"GP PRACTICE\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/ValueSet\\/primarycare-organization-type\"}]}}";

        ca.uhn.fhir.model.dstu2.resource.Organization parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Organization.class, str);

        String name = parsed.getName();
        ResourceReferenceDt partOfReference = parsed.getPartOf();

        System.out.println(partOfReference.getReference().toString());
        String partOf = partOfReference.getReferenceElement().getIdPart();
        String id = parsed.getId().getIdPart();
        Boolean active = parsed.getActive();

        // change this to a helper class
        String ods_code = "";
        List<IdentifierDt> fhirIdentifiers = parsed.getIdentifier();
        Integer s = fhirIdentifiers.size()-1;
        for ( int i=0 ; i<=s; i++) {
            String system = fhirIdentifiers.get(i).getSystem();
            if (system.contains("ods-organization-code")) {
                ods_code = fhirIdentifiers.get(i).getValue();
            }
        }

        System.out.println(ods_code);

        CodeableConceptDt codeable = parsed.getType();
        s= codeable.getCoding().size();
        String orgtype = ""; String orgTerm = "";
        if (s>0) {
            CodingDt code = codeable.getCoding().get(0);
            orgtype = code.getCode();
            orgTerm = code.getDisplay();
        }

        Organisation ccg = new Organisation(partOf)
                .setName("CCG "+partOf);

        Organisation organization = new Organisation(id)
                .setName(name)
                .setIsCommissionedBy(ccg)
                .setOdsCode(ods_code)
                .setSpeciality(orgtype)
                .setProperty("speciality-term", orgTerm);

        ObjectMapper om = new ObjectMapper();
        //String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(organization);
        String json = om.writeValueAsString(organization);
        // missing location reference

        // call ods web site to get the address details
        // discovery:  OdsOrganisation odsOrg = OdsWebService.lookupOrganisationViaRest(odsCode);
        // odsCode = odsCode.replace(" ", "%20");
        // String urlStr = "https://directory.spineservices.nhs.uk/ORD/2-0-0/organisations/" + odsCode;

        return json;
    }
}
