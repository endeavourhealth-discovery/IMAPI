package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptTct {
	
	public static final Integer DIRECT_RELATION_LEVEL = 0;

	@Id()
	private Integer dbid;
	@OneToOne()
	@JoinColumn(name="source", referencedColumnName="dbid")
	private Concept source;
	private Integer level;
	@OneToOne()
	@JoinColumn(name="target", referencedColumnName="dbid")
	private Concept target;

	public ConceptTct() {
		super();
	}

	public ConceptTct(Integer dbid, Concept source, Integer level, Concept target) {
		super();
		this.dbid = dbid;
		this.source = source;
		this.level = level;
		this.target = target;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public Concept getSource() {
		return source;
	}

	public void setSource(Concept source) {
		this.source = source;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Concept getTarget() {
		return target;
	}

	public void setTarget(Concept target) {
		this.target = target;
	}

}
