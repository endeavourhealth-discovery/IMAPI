package org.endeavourhealth.imapi.model.iml;

import lombok.Getter;

@Getter
public class ArrayButtons {
    Boolean up;
    Boolean down;
    Boolean plus;
    Boolean minus;
    Boolean addOnlyIfLast;

    public ArrayButtons setUp(Boolean up) {
        this.up = up;
        return this;
    }

    public ArrayButtons setDown(Boolean down) {
        this.down = down;
        return this;
    }

    public ArrayButtons setPlus(Boolean plus) {
        this.plus = plus;
        return this;
    }

    public ArrayButtons setMinus(Boolean minus) {
        this.minus = minus;
        return this;
    }

    public ArrayButtons setAddOnlyIfLast(Boolean addOnlyIfLast) {
        this.addOnlyIfLast = addOnlyIfLast;
        return this;
    }
}
