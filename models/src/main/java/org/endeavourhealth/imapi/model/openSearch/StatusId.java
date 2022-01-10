package org.endeavourhealth.imapi.model.openSearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusId implements MatchPhraseId {
    private String id;

    public StatusId(String id) {
        this.id = id;
    }

    @JsonProperty("status.@id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
