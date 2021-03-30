package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class ConceptType {

    @Id()
    private Integer dbid;
    @OneToOne()
    @JoinColumn(name = "concept", referencedColumnName = "dbid")
    private Concept concept;
    private String type;
    private LocalDateTime updated;

    public Integer getDbid() {
        return dbid;
    }

    public ConceptType setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getConcept() {
        return concept;
    }

    public ConceptType setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public String getType() {
        return type;
    }

    public ConceptType setType(String type) {
        this.type = type;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public ConceptType setUpdated(LocalDateTime updated) {
        this.updated = updated;
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
        ConceptType other = (ConceptType) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
