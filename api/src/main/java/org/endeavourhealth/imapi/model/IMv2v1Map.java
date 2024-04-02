package org.endeavourhealth.imapi.model;

import java.util.Objects;

public class IMv2v1Map {
    private String v2Code;
    private String v2Scheme;
    private Integer v1Dbid;

    public String getV2Code() {
        return v2Code;
    }

    public IMv2v1Map setV2Code(String v2Code) {
        this.v2Code = v2Code;
        return this;
    }

    public String getV2Scheme() {
        return v2Scheme;
    }

    public IMv2v1Map setV2Scheme(String v2Scheme) {
        this.v2Scheme = v2Scheme;
        return this;
    }

    public Integer getV1Dbid() {
        return v1Dbid;
    }

    public IMv2v1Map setV1Dbid(Integer v1Dbid) {
        this.v1Dbid = v1Dbid;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IMv2v1Map other = (IMv2v1Map) o;
        return v1Dbid.equals(other.v1Dbid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1Dbid);
    }
}
