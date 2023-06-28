package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class PathQuery extends TTIriRef {
	private IriLD source;
	private IriLD target;
	private Integer depth;

	public IriLD getSource() {
		return source;
	}

	@JsonSetter
	public PathQuery setSource(IriLD source) {
		this.source = source;
		return this;
	}

	public PathQuery setSource(String source) {
		this.source = new IriLD().setIri(source);
		return this;
	}

	public IriLD getTarget() {
		return target;
	}

 @JsonSetter
	public PathQuery setTarget(IriLD target) {
		this.target = target;
		return this;
	}

	public PathQuery setTarget(String target) {
		this.target = new IriLD().setIri(target);
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
