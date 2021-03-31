package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import java.util.HashSet;
import java.util.Set;

public class PropertyValue {

	private TTIriRef property;
	private TTIriRef valueType;
	private TTIriRef inverseOf;
	private Set<String> oneOf;
	private String minInclusive;
	private String minExclusive;
	private String maxInclusive;
	private String maxExclusive;
	private String pattern;
	private String valueData;
	private String individual;
	private TTNode expression;
	private int group;
	private TTIriRef inheritedFrom;

	public TTIriRef getProperty() {
		return property;
	}

	public PropertyValue setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public String getValueData() {
		return valueData;
	}

	public PropertyValue setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

	public Set<String> getOneOf() {
		return oneOf;
	}

	public PropertyValue setOneOf(Set<String> oneOf) {
		this.oneOf = oneOf;
		return this;
	}

	public PropertyValue addOneOf(String oneOf) {
		if (this.oneOf == null)
			this.oneOf = new HashSet<>();
		this.oneOf.add(oneOf);
		return this;
	}

	public String getPattern() {
		return pattern;
	}

	public PropertyValue setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public TTIriRef getValueType() {
		return valueType;
	}

	public PropertyValue setValueType(TTIriRef objectType) {
		this.valueType = objectType;
		return this;
	}

	public TTIriRef getInverseOf() {
		return inverseOf;
	}

	public PropertyValue setInverseOf(TTIriRef inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public String getMinInclusive() {
		return minInclusive;
	}

	public PropertyValue setMinInclusive(String minInclusive) {
		this.minInclusive = minInclusive;
		return this;
	}

	public String getMinExclusive() {
		return minExclusive;
	}

	public PropertyValue setMinExclusive(String minExclusive) {
		this.minExclusive = minExclusive;
		return this;
	}

	public String getMaxInclusive() {
		return maxInclusive;
	}

	public PropertyValue setMaxInclusive(String maxInclusive) {
		this.maxInclusive = maxInclusive;
		return this;
	}

	public String getMaxExclusive() {
		return maxExclusive;
	}

	public PropertyValue setMaxExclusive(String maxExclusive) {
		this.maxExclusive = maxExclusive;
		return this;
	}

	public String getIndividual() {
		return individual;
	}

	public PropertyValue setIndividual(String individual) {
		this.individual = individual;
		return this;
	}

	public TTNode getExpression() {
		return expression;
	}

	public PropertyValue setExpression(TTNode expression) {
		this.expression = expression;
		return this;
	}

	public int getGroup() {
		return group;
	}

	public PropertyValue setGroup(int group) {
		this.group = group;
		return this;
	}

	public TTIriRef getInheritedFrom() {
		return inheritedFrom;
	}

	public PropertyValue setInheritedFrom(TTIriRef inheritedFrom) {
		this.inheritedFrom = inheritedFrom;
		return this;
	}
}
