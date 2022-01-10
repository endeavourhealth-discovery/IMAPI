package org.endeavourhealth.imapi.model.openSearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemeId implements MatchPhraseId {
    private String id;

    public SchemeId(String id) {
        this.id = id;
    }

    @JsonProperty("scheme.@id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
