package org.endeavourhealth.dataaccess.graph.tripletree;

import org.eclipse.rdf4j.model.Literal;

public class TTLiteral extends TTValue {
    Literal value;

    public TTLiteral() {}
    public TTLiteral(Literal value) {
        this.value = value;
    }

    public Literal getValue() {
        return value;
    }

    public TTLiteral setValue(Literal value) {
        this.value = value;
        return this;
    }

    @Override
    public TTLiteral asLiteral() {
        return this;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }
}
