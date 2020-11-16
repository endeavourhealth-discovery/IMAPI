package com.endavourhealth.dataaccess.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Classification {

	@Id()
	private Integer dbid;
    @OneToOne()
    @JoinColumn(name="parent", referencedColumnName="dbid")
    private Concept parent;
    @OneToOne()
    @JoinColumn(name="child", referencedColumnName="dbid")
    private Concept child;
    @OneToOne()
    @JoinColumn(name="module", referencedColumnName="dbid")
    private Module module;

    public Classification() {
		super();
	}

	public Classification(Integer dbid, Concept parent, Concept child, Module module) {
		super();
		this.dbid = dbid;
        this.parent = parent;
        this.child = child;
        this.module = module;
	}

    public Integer getDbid() {
        return dbid;
    }

    public Classification setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public Concept getParent() {
        return parent;
    }

    public Classification setParent(Concept parent) {
        this.parent = parent;
        return this;
    }

    public Concept getChild() {
        return child;
    }

    public Classification setChild(Concept child) {
        this.child = child;
        return this;
    }

    public Module getModule() {
        return module;
    }

    public Classification setModule(Module module) {
        this.module = module;
        return this;
    }
}
