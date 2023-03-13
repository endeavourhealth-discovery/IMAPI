package org.endeavourhealth.zfhirmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.codegen.Address;
import org.endeavourhealth.imapi.logic.codegen.IMDMBase;
import org.endeavourhealth.imapi.logic.codegen.Patient;

import java.util.Arrays;
import java.util.List;

public class ZFhirMapperRich {
    public static void main(String[] argv) throws Exception {
        System.out.println("test");
        Address home = new Address("a8039ce3-46da-4756-8a61-da26f8e8af21"); // .setPostcode("NE1").setProperty("city", "London");
        //Address work = new Address("ca948dc1-80df-41fd-9911-bde9dcfce65c"); // .setPostcode("LS1").setProperty("city", "Leeds");

        Address work = new Address("ca948dc1-80df-41fd-9911-bde9dcfce65c").setPostCode("LS1").setProperty("city", "Leeds");

        home.setPostCode("NE1");
        home.setProperty("city", "Leeds");

        Patient patient = new Patient("964fa5a1-aca8-4fd7-b2de-2aa4bbd005b1")
                //.setName("Fred Bloggs")
                //.setDateOfBirth(PartialDateTime.parse("1973-09-26"))
                .setProperty("age", 21)
                .setProperty("address", Arrays.asList(
                        new Address("a8039ce3-46da-4756-8a61-da26f8e8af21"),
                        new Address("ca948dc1-80df-41fd-9911-bde9dcfce65c")
                ));

        //patient.setDateOfBirth(PartialDateTime.parse("1969-07-29"));

        List<IMDMBase> resources = Arrays.asList(
                patient,
                home,
                work
        );

        ObjectMapper om = new ObjectMapper();
        // Serialization test
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(resources);
        System.out.println(json);
    }
}
