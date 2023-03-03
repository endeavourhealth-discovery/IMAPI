package org.endeavourhealth.imapi.logic.codegen;

public class Patient extends IMDMBase<Patient> {
    Patient() {
        super("Patient");
    }
    Patient(String id) {
        super("Patient", id);
    }

    public String getName() {
        return (String) properties.get("name");
    }

    public Patient setName(String name) {
        properties.put("name", name);
        return this;
    }

    public PartialDateTime getDateOfBirth() {
        return (PartialDateTime) properties.get("dob");
    }

    public Patient setDateOfBirth(PartialDateTime dateOfBirth) {
        properties.put("dob", dateOfBirth);
        return this;
    }
}
