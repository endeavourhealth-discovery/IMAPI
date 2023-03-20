package org.endeavourhealth.zfhirmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.Address;
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.imapi.logic.codegen.PartialDateTime;
import org.endeavourhealth.imapi.logic.codegen.Patient;
import org.endeavourhealth.persistence.IMPFiler;
import org.endeavourhealth.persistence.IMPFilerCSV;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ZFhirMapperRich {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");
        Address home = new Address(UUID.fromString("a8039ce3-46da-4756-8a61-da26f8e8af21"))
            .setPostCode("NE1")
            .setProperty("city", "London");
        Address work = new Address(UUID.fromString("ca948dc1-80df-41fd-9911-bde9dcfce65c"))
            .setPostCode("LS1")
            .setProperty("city", "Leeds");

        Patient patient = new Patient(UUID.fromString("964fa5a1-aca8-4fd7-b2de-2aa4bbd005b1"))
                .setForenames("Fred Bloggs")
                .setDateOfBirth("1973-09-26")
                .setProperty("age", 21)
                .setProperty("homeAddress", home.getId())
                .setProperty("workAddress", work.getId());


        List<IMDMBase> resources = Arrays.asList(
                patient,
                home,
                work
        );

        ObjectMapper om = new ObjectMapper();
        // Serialization test
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(resources);
        System.out.println(json);

        new IMPFilerCSV("Z:\\pojo\\out\\").fileIMPs(resources);
    }
}
