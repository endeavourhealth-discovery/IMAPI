package org.endeavourhealth.imapi.model.tripletree;

public abstract class TTValue {
    public boolean isLiteral() { return false; }
    public boolean isIriRef() {return false; }
    public boolean isList() { return false; }
    public boolean isNode() { return false; }


    public TTLiteral asLiteral() {return null; }
    public TTIriRef asIriRef() {return null; }
    public TTArray asArray() {return null; }
    public TTNode asNode() {return null; }
}
