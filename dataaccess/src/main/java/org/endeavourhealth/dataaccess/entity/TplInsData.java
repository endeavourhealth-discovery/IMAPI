package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;

@Entity
public class TplInsData {

    @Id()
    private Integer dbid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject")
    private Instance subject;

    @OneToOne()
    @JoinColumn(name="predicate", referencedColumnName="dbid")
    private Concept predicate;

    @OneToOne()
    @JoinColumn(name="data_type", referencedColumnName="dbid")
    private Concept dataType;

    private String literal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blank_node", referencedColumnName="dbid")
    private TplInsData blankNode;

    public Integer getDbid() {
        return dbid;
    }

    public TplInsData setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Instance getSubject() {
        return subject;
    }

    public TplInsData setSubject(Instance subject) {
        this.subject = subject;
        return this;
    }

    public Concept getPredicate() {
        return predicate;
    }

    public TplInsData setPredicate(Concept predicate) {
        this.predicate = predicate;
        return this;
    }

    public Concept getDataType() {
        return dataType;
    }

    public TplInsData setDataType(Concept dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    public TplInsData setLiteral(String literal) {
        this.literal = literal;
        return this;
    }

    public TplInsData getBlankNode() {
        return blankNode;
    }

    public TplInsData setBlankNode(TplInsData blankNode) {
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
        TplInsData other = (TplInsData) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
