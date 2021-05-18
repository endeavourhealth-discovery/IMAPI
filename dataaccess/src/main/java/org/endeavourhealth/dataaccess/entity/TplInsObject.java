package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;

@Entity
public class TplInsObject {

    @Id()
    private Integer dbid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject")
    private Instance subject;

    @OneToOne()
    @JoinColumn(name="predicate", referencedColumnName="dbid")
    private Concept predicate;

    @OneToOne()
    @JoinColumn(name="object", referencedColumnName="dbid")
    private Concept object;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blank_node", referencedColumnName="dbid")
    private TplInsObject blankNode;

    public Integer getDbid() {
        return dbid;
    }

    public TplInsObject setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Instance getSubject() {
        return subject;
    }

    public TplInsObject setSubject(Instance subject) {
        this.subject = subject;
        return this;
    }

    public Concept getPredicate() {
        return predicate;
    }

    public TplInsObject setPredicate(Concept predicate) {
        this.predicate = predicate;
        return this;
    }

    public Concept getObject() {
        return object;
    }

    public TplInsObject setObject(Concept object) {
        this.object = object;
        return this;
    }

    public TplInsObject getBlankNode() {
        return blankNode;
    }

    public TplInsObject setBlankNode(TplInsObject blankNode) {
        this.blankNode = blankNode;
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
        TplInsObject other = (TplInsObject) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
