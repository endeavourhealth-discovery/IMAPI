package org.endeavourhealth.dataaccess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Concept implements Serializable {
    private Integer dbid;

    private String iri;

    private String name;
    private String description;
    private String code;
    private Concept scheme;
    private Concept status;
    private String json;
    private LocalDateTime updated;

    public Integer getDbid() {
        return dbid;
    }

    public Concept setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Concept setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public Concept setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Concept setCode(String code) {
        this.code = code;
        return this;
    }
    public Concept getScheme() {
        return scheme;
    }

    public Concept setScheme(Concept scheme) {
        this.scheme = scheme;
        return this;
    }

    public Concept getStatus() {
        return status;
    }

    public Concept setStatus(Concept status) {
        this.status = status;
        return this;
    }

    public String getJson() {
        return json;
    }

    public Concept setJson(String definition) {
        this.json = definition;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Concept setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

//    public List<ConceptType> getType() {
//        return type;
//    }
//
//    public Concept setType(List<ConceptType> types) {
//        this.type = types;
//        return this;
//    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dbid == null) ? 0 : dbid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Concept other = (Concept) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
