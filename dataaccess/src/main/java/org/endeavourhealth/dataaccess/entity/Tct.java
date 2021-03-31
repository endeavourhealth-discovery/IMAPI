package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Tct {

    @Id()
    private Integer dbid;

    @OneToOne()
    @JoinColumn(name="ancestor", referencedColumnName="dbid")
    private Concept ancestor;

    @OneToOne()
    @JoinColumn(name="descendant", referencedColumnName="dbid")
    private Concept descendant;

    @OneToOne()
    @JoinColumn(name="type", referencedColumnName="dbid")
    private Concept type;

    private Integer level;

    public Integer getDbid() {
        return dbid;
    }

    public Tct setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getAncestor() {
        return ancestor;
    }

    public Tct setAncestor(Concept ancestor) {
        this.ancestor = ancestor;
        return this;
    }

    public Concept getDescendant() {
        return descendant;
    }

    public Tct setDescendant(Concept descendent) {
        this.descendant = descendent;
        return this;
    }

    public Concept getType() {
        return type;
    }

    public Tct setType(Concept type) {
        this.type = type;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public Tct setLevel(Integer level) {
        this.level = level;
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
        Tct other = (Tct) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
