package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class PathTarget extends ConceptRef {
	private Integer depth;



	public Integer getDepth() {
		return depth;
	}

	public PathTarget setDepth(Integer depth) {
		this.depth = depth;
		return this;
	}

	public PathTarget setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public PathTarget setName(String name){
		super.setName(name);
		return this;
	}

	public PathTarget setAlias(String alias){
		super.setAlias(alias);
		return this;
	}
}
