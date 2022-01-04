package org.endeavourhealth.imapi.model.openSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "match_phrase")
public class TypeId implements MatchPhraseId {
    private String id;

    public TypeId(String id) {
        this.id = id;
    }

    @JsonProperty("entityType.@id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
