package org.endeavourhealth.dataaccess.entity;

public class Tct {

    private Integer dbid;

    private Entity ancestor;

    private Entity descendant;

    private Entity type;

    private Integer level;

    public Integer getDbid() {
        return dbid;
    }

    public Tct setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Entity getAncestor() {
        return ancestor;
    }

    public Tct setAncestor(Entity ancestor) {
        this.ancestor = ancestor;
        return this;
    }

    public Entity getDescendant() {
        return descendant;
    }

    public Tct setDescendant(Entity descendent) {
        this.descendant = descendent;
        return this;
    }

    public Entity getType() {
        return type;
    }

    public Tct setType(Entity type) {
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
