package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class TplData {

    @Id()
    private Integer dbid;

    @OneToOne()
    @JoinColumn(name="subject", referencedColumnName="dbid")
    private Concept subject;

    @OneToOne()
    @JoinColumn(name="predicate", referencedColumnName="dbid")
    private Concept predicate;

    private Integer groupNumber;

    @OneToOne()
    @JoinColumn(name="graph", referencedColumnName="dbid")
    private Concept graph;

    private String literal;

    @OneToOne()
    @JoinColumn(name="data_type", referencedColumnName="dbid")
    private Concept dataType;

    public Integer getDbid() {
        return dbid;
    }

    public TplData setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getSubject() {
        return subject;
    }

    public TplData setSubject(Concept subject) {
        this.subject = subject;
        return this;
    }

    public Concept getPredicate() {
        return predicate;
    }

    public TplData setPredicate(Concept predicate) {
        this.predicate = predicate;
        return this;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public TplData setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
        return this;
    }

    public Concept getGraph() {
        return graph;
    }

    public TplData setGraph(Concept graph) {
        this.graph = graph;
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    public TplData setLiteral(String literal) {
        this.literal = literal;
        return this;
    }

    public Concept getDataType() {
        return dataType;
    }

    public TplData setDataType(Concept dataType) {
        this.dataType = dataType;
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
        TplData other = (TplData) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
