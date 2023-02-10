package org.endeavourhealth.imapi.logic.codegen;

public class IMDMAddress extends IMDMBase<IMDMAddress> {

    IMDMAddress() {
        super("IMDMAddress");
    }

    public String getPostcode() {
        return (String) properties.get("postcode");
    }

    public IMDMAddress setPostcode(String postcode) {
        properties.put("postcode", postcode);
        return this;
    }
}