package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Axiom {

	@Id()
	private Integer dbid;
    @OneToOne()
    @JoinColumn(name="module", referencedColumnName="dbid")
    private Module module;
	@OneToOne()
	@JoinColumn(name="concept", referencedColumnName="dbid")
	private Concept concept;
    private Byte type;
	private Integer version;

	public Axiom() {
		super();
	}

	public Axiom(Integer dbid, Module module, Concept concept, Byte type, Integer version) {
		super();
		this.dbid = dbid;
		this.module = module;
		this.concept = concept;
		this.type = type;
		this.version = version;
	}

    public Integer getDbid() {
        return dbid;
    }

    public Axiom setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Module getModule() {
        return module;
    }

    public Axiom setModule(Module module) {
        this.module = module;
        return this;
    }

    public Concept getConcept() {
        return concept;
    }

    public Axiom setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public Byte getType() {
        return type;
    }

    public Axiom setType(Byte type) {
        this.type = type;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public Axiom setVersion(Integer version) {
        this.version = version;
        return this;
    }
}
