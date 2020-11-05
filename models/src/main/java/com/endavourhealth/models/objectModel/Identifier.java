package com.endavourhealth.models.objectModel;

public class Identifier {
    private String value;
    private String display;
    private String codeScheme;

    public String getValue() {
        return value;
    }

    public Identifier setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDisplay() {
        return display;
    }

    public Identifier setDisplay(String display) {
        this.display = display;
        return this;
    }

    public String getCodeScheme() {
        return codeScheme;
    }

    public Identifier setCodeScheme(String codeScheme) {
        this.codeScheme = codeScheme;
        return this;
    }
}
