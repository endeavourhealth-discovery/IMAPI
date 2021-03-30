package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Concept implements Serializable {
    @Id()
    private Integer dbid;

    @Column(unique = true)
    private String iri;

    private String name;
    private String description;
    private String code;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="scheme", referencedColumnName = "iri", nullable = true)
    private Concept scheme;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="status", referencedColumnName = "iri", nullable = true)
    private Concept status;
    private String definition;
    private LocalDateTime updated;

    @OneToMany()
    private List<ConceptType> types;

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

    public String getDefinition() {
        return definition;
    }

    public Concept setDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Concept setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public List<ConceptType> getTypes() {
        return types;
    }

    public Concept setTypes(List<ConceptType> types) {
        this.types = types;
        return this;
    }

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
