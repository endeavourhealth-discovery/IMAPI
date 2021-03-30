package org.endeavourhealth.imapi.model.tripletree;

import java.util.List;

public abstract class TTValue {
    public boolean isLiteral() { return false; }
    public boolean isIriRef() {return false; }
    public boolean isList() { return false; }
    public boolean isNode() { return false; }

    public TTLiteral asLiteral() {return null; }
    public TTIriRef asIriRef() {return null; }
    public TTArray asArray() {return null; }
    public List<TTValue> asArrayElements() {return null; }
    public TTNode asNode() {return null; }
}
