package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.parser.IParser;
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.imapi.logic.codegen.Organisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ZFhirMapperOrganization extends ZFhirMapperBase<ZFhirMapperOrganization> {
    public static void main(String[] argv) throws Exception {
        if (argv.length == 2)
            new ZFhirMapperOrganization().execute(argv[0], argv[1]);
        else
            new ZFhirMapperOrganization().execute("d:\\pojo\\in\\Ten_rows\\organization.txt", "d:\\pojo\\out\\Ten_rows\\organization");
    }

    public Collection<IMDMBase>  RunMapper(String str, IParser parser) {
        List<IMDMBase> result = new ArrayList<>();

        ca.uhn.fhir.model.dstu2.resource.Organization parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Organization.class, str);

        String name = parsed.getName();
        ResourceReferenceDt partOfReference = parsed.getPartOf();

        System.out.println("[" + partOfReference.getReference().toString() + "]");

        UUID partOf = null;
        if (partOfReference.getReferenceElement().getIdPart() != null) {
            partOf = UUID.fromString(partOfReference.getReferenceElement().getIdPart());
        }

        UUID id = UUID.fromString(parsed.getId().getIdPart());
        Boolean active = parsed.getActive();

        String ods_code = "";
        List<IdentifierDt> fhirIdentifiers = parsed.getIdentifier();
        Integer s = fhirIdentifiers.size()-1;
        for ( int i=0 ; i<=s; i++) {
            String system = fhirIdentifiers.get(i).getSystem();
            if (system.contains("ods-organization-code")) {
                ods_code = fhirIdentifiers.get(i).getValue();
            }
        }

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
                .setOdsCode(ods_code)
                .setSpeciality(orgtype)
                .setProperty("custom_speciality_term", orgTerm);
        result.add(organization);

        if (partOf != null) {
            //Organisation ccg = new Organisation(partOf);
            //organization.setIsCommissionedBy(ccg.getId());
            //result.add(ccg);
        }

        if (ccg.getId() != null) {
            organization.setIsCommissionedBy(ccg.getId());
        }

        // missing location reference

        // call ods web site to get the address details
        // discovery:  OdsOrganisation odsOrg = OdsWebService.lookupOrganisationViaRest(odsCode);
        // odsCode = odsCode.replace(" ", "%20");
        // String urlStr = "https://directory.spineservices.nhs.uk/ORD/2-0-0/organisations/" + odsCode;

        return result;
    }
}
