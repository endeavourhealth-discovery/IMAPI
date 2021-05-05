package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Tpl {

    @Id()
    private Integer dbid;

    @OneToOne()
    @JoinColumn(name="subject", referencedColumnName="dbid")
    private Concept subject;

    @OneToOne()
    @JoinColumn(name="predicate", referencedColumnName="dbid")
    private Concept predicate;

    @OneToOne()
    @JoinColumn(name="object", referencedColumnName="dbid")
    private Concept object;

    private Integer groupNumber;

    @OneToOne()
    @JoinColumn(name="graph", referencedColumnName="dbid")
    private Concept graph;

    public Integer getDbid() {
        return dbid;
    }

    public Tpl setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getSubject() {
        return subject;
    }

    public Tpl setSubject(Concept subject) {
        this.subject = subject;
        return this;
    }

    public Concept getPredicate() {
        return predicate;
    }

    public Tpl setPredicate(Concept predicate) {
        this.predicate = predicate;
        return this;
    }

    public Concept getObject() {
    	if(null == object) {
    		return new Concept();
    	}
        return object;
    }

    public Tpl setObject(Concept object) {
        this.object = object;
        return this;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public Tpl setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
        return this;
    }

    public Concept getGraph() {
        return graph;
    }

    public Tpl setGraph(Concept graph) {
        this.graph = graph;
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
        Tpl other = (Tpl) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
