package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import org.endeavourhealth.imapi.logic.codegen.*;

import java.util.*;

public class ZFhirMapperPatient extends ZFhirMapperBase<ZFhirMapperPatient> {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 2) {
            System.err.println("Usage: ZFhirMapperPatient <in folder> <out folder>");
            System.exit(-1);
        } else
            new ZFhirMapperPatient().execute(argv[0], argv[1]);
    }
    public Collection<IMDMBase> RunMapper(String str, IParser parser) throws Exception {
        List<IMDMBase> result = new ArrayList<>();

        ca.uhn.fhir.model.dstu2.resource.Patient parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Patient.class, str);

        String fhirId = parsed.getId().getIdPart();
        Patient patient = new Patient(UUID.fromString(fhirId));
        result.add(patient);

        /*
        System.out.println(parsed.getAddress().get(0).getLine().get(0));
        List<AddressDt> fhirAddresses = parsed.getAddress();
        Integer s = fhirAddresses.size()-1;
        for ( int i=0 ; i<=s; i++) {
            System.out.println(fhirAddresses.get(i).getLine());
        }
        */

        ResourceReferenceDt organizationReference = parsed.getManagingOrganization();
        String zOrgId = organizationReference.getReference().getIdPart();
        Organisation zOrganisation = new Organisation(UUID.fromString(zOrgId));
        // duplicate key value violates unique constraint "main_pkey"
        //result.add(zOrganisation);

        String fhirPostalCode = parsed.getAddress().get(0).getPostalCode();
        String fhirCity = parsed.getAddress().get(0).getCity();
        String fhirCountry = parsed.getAddress().get(0).getCountry();
        String fhirAddTxt = parsed.getAddress().get(0).getText();
        String fhirLocality = parsed.getAddress().get(0).getDistrict();

        StringDt fhirFamily = parsed.getName().get(0).getFamily().get(0);
        StringDt fhirGiven = parsed.getName().get(0).getGiven().get(0);
        StringDt fhirPrefix = parsed.getName().get(0).getPrefixFirstRep();

        StringDt fhirTitle = null;
        if (parsed.getName().get(0).getPrefix().size() > 0) {
            fhirTitle = parsed.getName().get(0).getPrefix().get(0);
        }

        String fhirNhsNumber = "";
        List<IdentifierDt> fhirIdentifiers = parsed.getIdentifier();
        Integer s = fhirIdentifiers.size()-1;
        for ( int i=0 ; i<=s; i++) {
            String system = fhirIdentifiers.get(i).getSystem();
            if (system.contains("nhs-number")) {
                fhirNhsNumber = fhirIdentifiers.get(i).getValue();
            }
        }

        List<ExtensionDt> fhirExtension = parsed.getUndeclaredExtensions();
        s = fhirExtension.size()-1;
        String fhirEthnicCode = "";
        String fhirEthnicDisplay = "";
        for ( int i=0 ; i<=s; i++) {
            CodeableConceptDt codeableConcept = (CodeableConceptDt) fhirExtension.get(i).getValue();
            fhirEthnicCode = codeableConcept.getCoding().get(0).getCode();
            fhirEthnicDisplay = codeableConcept.getCoding().get(0).getDisplay();
        }

        List<ResourceReferenceDt> fhirProvider = parsed.getCareProvider();
        s = fhirProvider.size()-1;

        UUID fhirGp = null;
        for ( int i=0 ; i<=s; i++) {
            IdDt ref = fhirProvider.get(i).getReference();
            if (ref.getResourceType().equals("Practitioner")) {
                fhirGp = UUID.fromString(ref.getIdPart());
            }
        }

        s = parsed.getAddress().get(0).getLine().size();
        String fhirLines = "";
        s--;
        for (int i = 0; i <= s; i++) {
            StringDt line = parsed.getAddress().get(0).getLine().get(i);
            if (!line.isEmpty()) {
                fhirLines = fhirLines + line + " ";
            }
        }

        int l = fhirLines.length();
        if (l>0) fhirLines = fhirLines.substring(0, l - 1);

        java.util.List<ContactPointDt> con = parsed.getTelecom();
        s = con.size();
        s--;
        for (int i = 0; i <= s; i++) {
            // java.util.List<ContactPointDt> conList = con.get(i).;
            ContactPointDt z = con.get(i);

            PeriodDt period = z.getPeriod();


            String telend = "";
            String telstart = "";
            if (!period.isEmpty()) {
                if (period.getEnd() != null) {
                    telend = ZMapperCommon.FormatDate(period.getEnd());
                }
                if (period.getStart()!=null) {telstart = ZMapperCommon.FormatDate(period.getStart());}
            }

            String use = z.getUse(); String sys = z.getSystem(); String value = z.getValue();

            if (telend.isEmpty()) {
                if (use != null && sys != null && value != null) {
                    if (sys.equals("phone") && use.equals("home")) {
                        patient.setHomeTelephoneNumber(value);
                    }
                    if (sys.equals("phone") && use.equals("work")) {
                        patient.setWorkTelephoneNumber(value);
                    }
                    if (sys.equals("phone") && use.equals("mobile")) {
                        patient.setMobileTelephoneNumber(value);
                    }

                    if (sys.equals("email") && use.equals("home")) {
                        patient.setPrimaryEmail(value);
                    }
                    if (sys.equals("email") && use.equals("work")) {
                        patient.setSecondaryEmail(value);
                    }
                }
            }
        }

        String fhirAddUse = parsed.getAddress().get(0).getUse();

        Address address = new Address(UUID.randomUUID())
                .setPostCode(fhirPostalCode)
                .setCity(fhirCity);

        result.add(address);

        if (fhirLines != null)  { address.setAddressLine(fhirLines); }

        if (fhirLocality != null) { address.setLocality(fhirLocality); }

        Date fhirDateOfBirth = null;
        fhirDateOfBirth = parsed.getBirthDate();

        String formatedDOB = ZMapperCommon.FormatDate(fhirDateOfBirth);

        Date fhirDateOfDeath = null;
        IDatatype fhirDeath = parsed.getDeceased();
        String formatedDOD = "";
        if (fhirDeath instanceof DateTimeDt) {
            fhirDateOfDeath = (Date) ((DateTimeDt) fhirDeath).getValue();
            formatedDOD = ZMapperCommon.FormatDate(fhirDateOfDeath);
        }

        String fhirGender = parsed.getGender();

        patient .setDateOfBirth(formatedDOB)
                .setFamilyName(fhirFamily.toString())
                .setForenames(fhirGiven.toString())
                .setStatedGender(fhirGender)
                .setNhsNumber(fhirNhsNumber);

        if (fhirEthnicCode != null) {
            patient.setEthnicity(fhirEthnicCode);
            patient.setProperty("custom_ethnic_term", fhirEthnicDisplay);
        }
        if (fhirTitle != null) patient.setTitle(fhirTitle.toString());
        if (!formatedDOD.isEmpty()) patient.setDateOfDeath(formatedDOD);

        patient.setProperty("custom_text", fhirAddTxt);

        if (fhirAddUse.equals("home")) {
            patient.setHomeAddress(address.getId());
        }

        if (fhirAddUse.equals("temp")) {
            patient.setTemporaryAddress(address.getId());
        }

        if (fhirAddUse.equals("work")) {
            patient.setWorkAddress(address.getId());
        }

        GpCurrentRegistration gpCurrReg = new GpCurrentRegistration(UUID.randomUUID());
        //result.add(gpCurrReg);
        gpCurrReg.setRecordOwner(zOrganisation.getId());

        PractitionerInRole gpInRole = new PractitionerInRole(fhirGp);
        //result.add(gpInRole);
        gpCurrReg.setUsualGp(gpInRole.getId());

        patient.setGpCurrentRegistration(gpCurrReg.getId());

        patient.setTitle(fhirPrefix.toString());

        return result;
    }
}
