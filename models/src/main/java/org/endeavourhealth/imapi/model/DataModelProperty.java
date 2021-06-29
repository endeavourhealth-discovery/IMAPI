package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class DataModelProperty {

	private TTIriRef property;
	private TTIriRef type;
	private String minInclusive;
	private String minExclusive;
	private String maxInclusive;
	private String maxExclusive;
	private String pattern;
	private TTIriRef inheritedFrom;

	public TTIriRef getProperty() {
		return property;
	}

	public DataModelProperty setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public String getPattern() {
		return pattern;
	}

	public DataModelProperty setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public TTIriRef getType() {
		return type;
	}

	public DataModelProperty setType(TTIriRef objectType) {
		this.type = objectType;
		return this;
	}

	public String getMinInclusive() {
		return minInclusive;
	}

	public DataModelProperty setMinInclusive(String minInclusive) {
		this.minInclusive = minInclusive;
		return this;
	}

	public String getMinExclusive() {
		return minExclusive;
	}

	public DataModelProperty setMinExclusive(String minExclusive) {
		this.minExclusive = minExclusive;
		return this;
	}

	public String getMaxInclusive() {
		return maxInclusive;
	}

	public DataModelProperty setMaxInclusive(String maxInclusive) {
		this.maxInclusive = maxInclusive;
		return this;
	}

	public String getMaxExclusive() {
		return maxExclusive;
	}

	public DataModelProperty setMaxExclusive(String maxExclusive) {
		this.maxExclusive = maxExclusive;
		return this;
	}

	public TTIriRef getInheritedFrom() {
		if(null == inheritedFrom) return new TTIriRef();
		return inheritedFrom;
	}

	public DataModelProperty setInheritedFrom(TTIriRef inheritedFrom) {
		this.inheritedFrom = inheritedFrom;
		return this;
	}
}
