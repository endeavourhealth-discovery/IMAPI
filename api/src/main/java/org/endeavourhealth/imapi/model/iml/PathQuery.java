package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class PathQuery extends TTIriRef {
	private TTIriRef source;
	private TTIriRef target;
	private Integer depth;

	public TTIriRef getSource() {
		return source;
	}

	public PathQuery setSource(TTIriRef source) {
		this.source = source;
		return this;
	}
	@JsonIgnore
	public PathQuery setSource(String source) {
		this.source = TTIriRef.iri(source);
		return this;
	}

	public TTIriRef getTarget() {
		return target;
	}

	public PathQuery setTarget(TTIriRef target) {
		this.target = target;
		return this;
	}
	@JsonIgnore
	public PathQuery setTarget(String target) {
		this.target = TTIriRef.iri(target);
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

}
