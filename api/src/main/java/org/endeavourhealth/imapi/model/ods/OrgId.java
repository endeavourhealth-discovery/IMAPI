package org.endeavourhealth.imapi.model.ods;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgId {
    private String extension;

    public String getExtension() {
        return extension;
    }

    public OrgId setExtension(String extension) {
        this.extension = extension;
        return this;
    }
}
