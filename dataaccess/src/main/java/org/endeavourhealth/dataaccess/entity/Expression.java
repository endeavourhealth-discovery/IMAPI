package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Expression {

	@Id()
	private Integer dbid;
    private Byte type;
    @ManyToOne()
    @JoinColumn(name="axiom", referencedColumnName="dbid")
    private Axiom axiom;
    private Integer parent;
	@OneToOne()
	@JoinColumn(name="targetConcept", referencedColumnName="dbid")
	private Concept targetConcept;
    @OneToMany(mappedBy = "expression")
    private List<PropertyValue> propertyValue;
    private Boolean exclude;

    public Expression() {
		super();
	}

	public Expression(Integer dbid, Byte type, Axiom axiom, Integer parent, Concept targetConcept) {
		super();
		this.dbid = dbid;
        this.type = type;
        this.axiom = axiom;
        this.parent = parent;
        this.targetConcept = targetConcept;
	}

    public Integer getDbid() {
        return dbid;
    }

    public Expression setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Byte getType() {
        return type;
    }

    public Expression setType(Byte type) {
        this.type = type;
        return this;
    }

    public Axiom getAxiom() {
        return axiom;
    }

    public Expression setAxiom(Axiom axiom) {
        this.axiom = axiom;
        return this;
    }

    public Integer getParent() {
        return parent;
    }

    public Expression setParent(Integer parent) {
        this.parent = parent;
        return this;
    }

    public Concept getTargetConcept() {
        return targetConcept;
    }

    public Expression setTargetConcept(Concept targetConcept) {
        this.targetConcept = targetConcept;
        return this;
    }

    public List<PropertyValue> getPropertyValue() {
        return propertyValue;
    }

    public Expression setPropertyValue(List<PropertyValue> propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    public Boolean getExclude() {
        return exclude;
    }

    public Expression setExclude(Boolean exclude) {
        this.exclude = exclude;
        return this;
    }
}
