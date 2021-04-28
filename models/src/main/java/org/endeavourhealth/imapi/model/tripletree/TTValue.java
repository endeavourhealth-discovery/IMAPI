package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public abstract class TTValue {
    @JsonIgnore
    public boolean isLiteral() { return false; }

    @JsonIgnore
    public boolean isIriRef() {return false; }

    @JsonIgnore
    public boolean isList() { return false; }

    @JsonIgnore
    public boolean isNode() { return false; }
    public boolean isTypedIri() { return false;}

    public TTLiteral asLiteral() {return null; }
    public TTIriRef asIriRef() {return null; }
    public TTArray asArray() {return null; }
    public List<TTValue> asArrayElements() {return null; }
    public TTNode asNode() {return null; }
    public TTIriRef asTypedIri() {return null;}
}
