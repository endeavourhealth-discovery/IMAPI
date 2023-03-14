package org.endeavourhealth.zfhirmapper;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
// import org.eclipse.rdf4j.query.algebra.Add;
import org.endeavourhealth.imapi.logic.codegen.*;

import java.io.*;
import java.util.*;

public class ZFhirMapper {
    public static void main(String[] argv) throws Exception {

        String str = "";

        //str = "{\"active\":true,\"address\":[{\"city\":\"BEVERLEY\",\"district\":\"\",\"line\":[\"\",\"\",\"\"],\"postalCode\":\"HU17 8GW\",\"text\":\",,,,BEVERLEY,HU17 8GW\",\"use\":\"home\"}],\"birthDate\":\"1999-11-30\",\"careProvider\":[{\"reference\":\"Organization\\/203\"},{\"reference\":\"Practitioner\\/2026\"}],\"extension\":[{\"url\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-ethnic-category-extension\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"\",\"display\":\"\",\"system\":\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-ethnic-category-extension\"}]}}],\"gender\":\"female\",\"id\":1998,\"identifier\":[{\"system\":\"http:\\/\\/fhir.nhs.net\\/Id\\/nhs-number\",\"use\":\"official\",\"value\":8456666211},{\"system\":\"http:\\/\\/endeavourhealth.org\\/identifier\\/patient-number\",\"use\":\"secondary\",\"value\":1998}],\"managingOrganization\":{\"reference\":\"Organization\\/203\"},\"meta\":{\"profile\":[\"http:\\/\\/endeavourhealth.org\\/fhir\\/StructureDefinition\\/primarycare-patient\"]},\"name\":[{\"family\":[\"Zavorohin\"],\"given\":[\"Bridget\"],\"prefix\":[\"Mrs\"],\"text\":\"Zavorohin, Bridget (Mrs)\",\"use\":\"official\"}],\"resourceType\":\"Patient\",\"telecom\":[{\"system\":\"phone\",\"use\":\"work\",\"value\":\"0657-322-6430\"},{\"system\":\"email\",\"use\":\"\",\"value\":\"x9erhfytv@yahoo.com\"}]}";
        //String test = RunMapper(str);

        String pathToCsv = "/media/sf_in/patient.txt";
        String outFile = "/media/sf_in/patient_out.txt";
        FileWriter csvWriter = new FileWriter(outFile);

        int c = 1;

        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newJsonParser();

        File file = new File(pathToCsv);
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        while (it.hasNext()) {
            String line = it.nextLine();
            String pojo = RunMapper(line, parser);
            csvWriter.append(c + "\t" + pojo + "\n");
            if (c>2000000) break;
            if (c%100 == 0) csvWriter.flush();
            System.out.println(c);
            c++;
        }

        csvWriter.close();
    }

    public static String RunMapper(String str, IParser parser) throws Exception {

        //FhirContext ctx = FhirContext.forDstu2();
        //Parser parser = ctx.newJsonParser();

        //String str = "{\"active\":true,\"address\":[{\"city\":\"BEVERLEY\",\"district\":\"\",\"line\":[19,\"\",\"BRAMBLE HILL\"],\"postalCode\":\"HU17 8UZ\",\"text\":\"19,,BRAMBLE HILL,,BEVERLEY,HU17 8UZ\",\"use\":\"home\"}],\"birthDate\":\"1935-04-17\",\"deceasedDateTime\":\"2013-02-25T00:00:00+00:00\",\"careProvider\":[{\"reference\":\"Organization/28\"},{\"reference\":\"Practitioner/4655\"}],\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"H\",\"display\":\"Indian\",\"system\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\"}]}}],\"gender\":\"female\",\"id\":1,\"identifier\":[{\"system\":\"http://fhir.nhs.net/Id/nhs-number\",\"use\":\"official\",\"value\":7858951564},{\"system\":\"http://endeavourhealth.org/identifier/patient-number\",\"use\":\"secondary\",\"value\":1}],\"managingOrganization\":{\"reference\":\"Organization/28\"},\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-patient\"]},\"name\":[{\"family\":[\"Zelinka\"],\"given\":[\"Josephine\"],\"text\":\"Zelinka, Josephine (Mrs)\",\"use\":\"official\"}],\"resourceType\":\"Patient\",\"telecom\":[{\"system\":\"phone\",\"use\":\"temp\",\"value\":\"0442-590-6506\"},{\"system\":\"phone\",\"use\":\"work\",\"value\":\"1224-352-6280\"},{\"system\":\"phone\",\"use\":\"old\",\"value\":\"0675-260-6652\"}]}";
        //str = "{\"active\":true,\"address\":[{\"city\":\"HULL\",\"district\":\"\",\"line\":[26,\"\",\"DRESSAY GROVE\"],\"postalCode\":\"HU8 9JJ\",\"text\":\"26,,DRESSAY GROVE,,HULL,HU8 9JJ\",\"use\":\"home\"}],\"birthDate\":\"1996-11-14\",\"careProvider\":[{\"reference\":\"Organization/155\"},{\"reference\":\"Practitioner/41\"}],\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"M\",\"display\":\"Caribbean\",\"system\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\"}]}}],\"gender\":\"male\",\"id\":8143,\"identifier\":[{\"system\":\"http://fhir.nhs.net/Id/nhs-number\",\"use\":\"official\",\"value\":7255204155},{\"system\":\"http://endeavourhealth.org/identifier/patient-number\",\"use\":\"secondary\",\"value\":8143}],\"managingOrganization\":{\"reference\":\"Organization/155\"},\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-patient\"]},\"name\":[{\"family\":[\"Franceschetti\"],\"given\":[\"Boyd\"],\"text\":\"Franceschetti, Boyd (Mr)\",\"use\":\"official\"}],\"resourceType\":\"Patient\",\"telecom\":[{\"system\":\"phone\",\"use\":\"home\",\"value\":\"0440-604-3637\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"a05346ghp@yahoo.com\"},{\"system\":\"phone\",\"use\":\"work\",\"value\":\"0542-643-5883\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"qqe7o2fmh@gmail.com\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"xhvauoex6@yahoo.com\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"1105-139-4203\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"1273-641-9437\"},{\"system\":\"email\",\"use\":\"\",\"value\":\"e7kxgnueu@gmail.com\"},{\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"1012-643-7388\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"lg1ypfo84@supanet.com\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"0777-513-5491\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"ta1krw6zu@supanet.com\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"1177-235-1529\"}]}";
        //str = "{\"active\":true,\"address\":[{\"city\":\"HULL\",\"district\":\"\",\"line\":[26,\"\",\"DRESSAY GROVE\"],\"postalCode\":\"HU8 9JJ\",\"text\":\"26,,DRESSAY GROVE,,HULL,HU8 9JJ\",\"use\":\"home\"}],\"birthDate\":\"1996-11-14\",\"careProvider\":[{\"reference\":\"Organization/155\"},{\"reference\":\"Practitioner/41\"}],\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"M\",\"display\":\"Caribbean\",\"system\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\"}]}}],\"gender\":\"male\",\"id\":8143,\"identifier\":[{\"system\":\"http://fhir.nhs.net/Id/nhs-number\",\"use\":\"official\",\"value\":7255204155},{\"system\":\"http://endeavourhealth.org/identifier/patient-number\",\"use\":\"secondary\",\"value\":8143}],\"managingOrganization\":{\"reference\":\"Organization/155\"},\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-patient\"]},\"name\":[{\"family\":[\"Franceschetti\"],\"given\":[\"Boyd\"],\"text\":\"Franceschetti, Boyd (Mr)\",\"use\":\"official\"}],\"resourceType\":\"Patient\",\"telecom\":[{\"system\":\"phone\",\"use\":\"home\",\"value\":\"0440-604-3637\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"a05346ghp@yahoo.com\"},{\"system\":\"phone\",\"use\":\"work\",\"value\":\"0542-643-5883\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"qqe7o2fmh@gmail.com\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"xhvauoex6@yahoo.com\",\"period\":{\"end\":\"2021-11-27T00:18:34+00:00\"}},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"1105-139-4203\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"1273-641-9437\"},{\"system\":\"email\",\"use\":\"\",\"value\":\"e7kxgnueu@gmail.com\"},{\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"1012-643-7388\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"lg1ypfo84@supanet.com\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"0777-513-5491\"},{\"system\":\"email\",\"use\":\"home\",\"value\":\"ta1krw6zu@supanet.com\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"1177-235-1529\"}]}";

        //str = "{\"active\":true,\"address\":[{\"city\":\"HULL\",\"district\":\"\",\"line\":[2,\"CLOVELLY AVENUE\",\"EDGECUMBE STREET\"],\"postalCode\":\"HU5 2EZ\",\"text\":\"2,CLOVELLY AVENUE,EDGECUMBE STREET,,HULL,HU5 2EZ\",\"use\":\"home\"}],\"birthDate\":\"1935-08-13\",\"careProvider\":[{\"reference\":\"Organization/316\"},{\"reference\":\"Practitioner/1642\"}],\"extension\":[{\"url\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\",\"valueCodeableConcept\":{\"coding\":[{\"code\":\"G\",\"display\":\"Any other mixed background\",\"system\":\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\"}]}}],\"gender\":\"female\",\"id\":1,\"identifier\":[{\"system\":\"http://fhir.nhs.net/Id/nhs-number\",\"use\":\"official\",\"value\":2821126119},{\"system\":\"http://endeavourhealth.org/identifier/patient-number\",\"use\":\"secondary\",\"value\":1}],\"managingOrganization\":{\"reference\":\"Organization/316\"},\"meta\":{\"profile\":[\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-patient\"]},\"name\":[{\"family\":[\"Heinig\"],\"given\":[\"Liesl\"],\"prefix\":[\"Mrs\"],\"text\":\"Heinig, Liesl (Mrs)\",\"use\":\"official\"}],\"resourceType\":\"Patient\",\"telecom\":[{\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"0774-683-1583\"},{\"system\":\"phone\",\"use\":\"home\",\"value\":\"0330-395-1380\"},{\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"0525-702-3739\"},{\"system\":\"phone\",\"use\":\"work\",\"value\":\"0604-256-9727\"}]}";

        ca.uhn.fhir.model.dstu2.resource.Patient parsed = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Patient.class, str);

        String fhirId = parsed.getId().getIdPart();
        Patient patient = new Patient(fhirId);

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
        Organisation zOrganisation = new Organisation(zOrgId);

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
        String fhirGp = "";
        for ( int i=0 ; i<=s; i++) {
            IdDt ref = fhirProvider.get(i).getReference();
            if (ref.getResourceType().equals("Practitioner")) {
                fhirGp = ref.getIdPart();
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


        //List<ca.uhn.fhir.model.dstu2.resource.Patient.Contact> contacts = parsed.getContact();
        //s = contacts.size();
        //s--;

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

        /*
        Address address = new Address()
                .setAddressLine("21 Bank Top")
                .setLocality("Slaithwaite")
                .setCity("Huddersfield")
                .setPostCode("HD7 5EF")
                .setCountry("United Kingdom")
                .setProperty("planet", "Earth");
         */

        String fhirAddUse = parsed.getAddress().get(0).getUse();

        /*
        String imAddUse = "";
        switch (fhirAddUse) {
            case "home":
                imAddUse = "http://endhealth.info/im#homeAddress";
                break;
            case "temp":
                imAddUse = "http://endhealth.info/im#temporaryAddress";
                break;
            case "work":
                imAddUse = "http://endhealth.info/im#workAddress";
                break;
            case "old":
                imAddUse = "http://endhealth.info/im#oldAddress";
                break;
        }
         */

        Address address = new Address()
                //.setAddressLine(fhirLines)
                .setPostCode(fhirPostalCode)
                .setCity(fhirCity);

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

        //GpCurrentRegistration currentGP = new GpCurrentRegistration()
        //        .setRecordOwner(Organisation)

        // http://hl7.org/fhir/administrative-gender/female
        // http://hl7.org/fhir/administrative-gender/male
        // http://hl7.org/fhir/administrative-gender/other

        String fhirGender = parsed.getGender();

        /*
        String imGender = "http://hl7.org/fhir/administrative-gender/other";
        switch (fhirGender) {
            case "female":
                imGender = "http://hl7.org/fhir/administrative-gender/female";
                break;
            case "male":
                imGender = "http://hl7.org/fhir/administrative-gender/male";
                break;
        }
         */

        patient .setDateOfBirth(formatedDOB)
                .setFamilyName(fhirFamily.toString())
                .setForenames(fhirGiven.toString())
                .setStatedGender(fhirGender)
                .setNhsNumber(fhirNhsNumber);

        if (fhirEthnicCode != null) {
            patient.setEthnicity(fhirEthnicCode);
            patient.setProperty("custom-ethnic-term", fhirEthnicDisplay);
        }
        if (fhirTitle != null) patient.setTitle(fhirTitle.toString());
        if (!formatedDOD.isEmpty()) patient.setDateOfDeath(formatedDOD);

        patient.setProperty("text", fhirAddTxt);

        if (fhirAddUse.equals("home")) {
            patient.setHomeAddress(address);
        }

        if (fhirAddUse.equals("temp")) {
            patient.setTemporaryAddress(address);
        }

        if (fhirAddUse.equals("work")) {
            patient.setWorkAddress(address);
        }

        GpCurrentRegistration gpCurrReg = new GpCurrentRegistration();
        gpCurrReg.setRecordOwner(zOrganisation);
        //patient.setGpCurrentRegistration(gpCurrReg);

        PractitionerInRole gpInRole = new PractitionerInRole(fhirGp);
        gpCurrReg.setUsualGp(gpInRole);

        patient.setGpCurrentRegistration(gpCurrReg);

        patient.setTitle(fhirPrefix.toString());

        ObjectMapper om = new ObjectMapper();

        // Serialization test
        //String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(patient);
        String json = om.writeValueAsString(patient);

        // Deserialization test
        /*
        Patient des = om.readValue(json, Patient.class);
        System.out.println("First name: " + des.getCallingName());
        System.out.println("Age: " + des.getAge());
        if (des.getProperty("middleName") != null) System.out.println("Middle name: " + des.getProperty("middleName"));
        System.out.println("Post code: " + des.getHomeAddress().getPostCode());
        System.out.println("Planet: " + des.getHomeAddress().getProperty("planet"));
        System.out.println("text: " + des.getProperty("text"));

        System.out.println("Gender: " + des.getStatedGender());
         */

        return json;
    }
}
