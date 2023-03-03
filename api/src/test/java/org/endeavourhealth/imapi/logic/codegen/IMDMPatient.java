package org.endeavourhealth.imapi.logic.codegen;

public class IMDMPatient extends IMDMBase<IMDMPatient> {
    IMDMPatient() {
        super("IMDMPatient");
    }

    public String getName() {
        return (String) properties.get("name");
    }

    public IMDMPatient setName(String name) {
        properties.put("name", name);
        return this;
    }

    public PartialDateTime getDateOfBirth() {
        return (PartialDateTime) properties.get("dob");
    }

    public IMDMPatient setDateOfBirth(PartialDateTime dateOfBirth) {
        properties.put("dob", dateOfBirth);
        return this;
    }
}
