package org.endeavourhealth.imapi.model.openSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "match_phrase")
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
