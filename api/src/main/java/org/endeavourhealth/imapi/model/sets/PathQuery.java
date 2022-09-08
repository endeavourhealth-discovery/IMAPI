package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PathQuery extends ConceptRef {
	private ConceptRef source;
	private ConceptRef target;
	private Integer depth;

	public ConceptRef getSource() {
		return source;
	}

	public PathQuery setSource(ConceptRef source) {
		this.source = source;
		return this;
	}
	@JsonIgnore
	public PathQuery setSource(String source) {
		this.source = ConceptRef.iri(source);
		return this;
	}

	public ConceptRef getTarget() {
		return target;
	}

	public PathQuery setTarget(ConceptRef target) {
		this.target = target;
		return this;
	}
	@JsonIgnore
	public PathQuery setTarget(String target) {
		this.target = ConceptRef.iri(target);
		return this;
	}

	public Integer getDepth() {
		return depth;
	}

	public PathQuery setDepth(Integer depth) {
		this.depth = depth;
		return this;
	}

	public PathQuery setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public PathQuery setName(String name){
		super.setName(name);
		return this;
	}

	public PathQuery setAlias(String alias){
		super.setAlias(alias);
		return this;
	}
}
