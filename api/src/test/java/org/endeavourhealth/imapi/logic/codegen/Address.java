package org.endeavourhealth.imapi.logic.codegen;

public class Address extends IMDMBase<Address> {

    Address() {
        super("Address");
    }
    Address(String id) {
        super("Address", id);
    }

    public String getPostcode() {
        return (String) properties.get("postcode");
    }

    public Address setPostcode(String postcode) {
        properties.put("postcode", postcode);
        return this;
    }
}
