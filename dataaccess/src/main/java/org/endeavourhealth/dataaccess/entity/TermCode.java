package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class TermCode {

    @Id()
    private Integer dbid;
    @OneToOne()
    @JoinColumn(name = "concept", referencedColumnName = "dbid")
    private Concept concept;
    private String term;
    private String code;
    private LocalDateTime updated;

    public Integer getDbid() {
        return dbid;
    }

    public TermCode setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getConcept() {
        return concept;
    }

    public TermCode setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public String getTerm() {
        return term;
    }

    public TermCode setTerm(String term) {
        this.term = term;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TermCode setCode(String code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public TermCode setUpdated(LocalDateTime updated) {
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
        TermCode other = (TermCode) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
