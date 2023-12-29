package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

public class PathQuery extends TTIriRef {
	private TTIriRef source;
	private TTIriRef target;
	private Integer depth;

	public TTIriRef getSource() {
		return source;
	}

	@JsonSetter
	public PathQuery setSource(TTIriRef source) {
		this.source = source;
		return this;
	}

	public PathQuery setSource(String source) {
		this.source = new TTIriRef().setIri(source);
		return this;
	}
	public PathQuery setSource(Vocabulary source) {
		return setSource(source.asTTIriRef());
	}

	public TTIriRef getTarget() {
		return target;
	}

 @JsonSetter
	public PathQuery setTarget(TTIriRef target) {
		this.target = target;
		return this;
	}

	public PathQuery setTarget(String target) {
		this.target = new TTIriRef().setIri(target);
		return this;
	}
	public PathQuery setTarget(Vocabulary target) {
		return setTarget(target.asTTIriRef());
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
