package org.endeavourhealth.imapi.model.iml;

public class ArrayButtons {
    private Boolean plus;
    private Boolean minus;
    private Boolean up;
    private Boolean down;
    private Boolean addOnlyIfLast;

    public Boolean getPlus() {
        return plus;
    }

    public ArrayButtons setPlus(Boolean plus) {
        this.plus = plus;
        return this;
    }

    public Boolean getMinus() {
        return minus;
    }

    public ArrayButtons setMinus(Boolean minus) {
        this.minus = minus;
        return this;
    }

    public Boolean getUp() {
        return up;
    }

    public ArrayButtons setUp(Boolean up) {
        this.up = up;
        return this;
    }

    public Boolean getDown() {
        return down;
    }

    public ArrayButtons setDown(Boolean down) {
        this.down = down;
        return this;
    }

    public Boolean getAddOnlyIfLast() {
        return addOnlyIfLast;
    }

    public ArrayButtons setAddOnlyIfLast(Boolean addOnlyIfLast) {
        this.addOnlyIfLast = addOnlyIfLast;
        return this;
    }
}
