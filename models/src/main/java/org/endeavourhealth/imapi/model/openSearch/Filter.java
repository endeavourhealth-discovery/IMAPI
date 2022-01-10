package org.endeavourhealth.imapi.model.openSearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "bool")
public class Filter {
    private List<MatchPhraseId> shoulds;
    private int minimum;

    public Filter(int minimum, List<MatchPhraseId> should) {
        this.shoulds = should;
        this.minimum = minimum;
    }

    public Filter(int minimum) {
        this.shoulds = new ArrayList<>();
        this.minimum = minimum;
    }

    @JsonProperty("should")
    public List<MatchPhraseId> getShoulds() {
        return shoulds;
    }

    public Filter setShoulds(List<MatchPhraseId> shoulds) {
        this.shoulds = shoulds;
        return this;
    }

    public Filter addShould(MatchPhraseId should) {
        this.shoulds.add(should);
        return this;
    }

    @JsonProperty("minimum_should_match")
    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
}
