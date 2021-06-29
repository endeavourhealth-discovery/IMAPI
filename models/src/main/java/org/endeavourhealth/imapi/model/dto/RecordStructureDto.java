package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class RecordStructureDto {

	private TTIriRef property;
	private TTIriRef type;
	private TTIriRef inherited;
	private Cardinality cardinality;

	public RecordStructureDto() {

	}

	public TTIriRef getProperty() {
		return property;
	}

	public RecordStructureDto setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public TTIriRef getType() {
		return type;
	}

	public RecordStructureDto setType(TTIriRef type) {
		this.type = type;
		return this;
	}

	public TTIriRef getInherited() {
		if(null == inherited) return new TTIriRef();
		return inherited;
	}

	public RecordStructureDto setInherited(TTIriRef inherited) {
		this.inherited = inherited;
		return this;
	}

	public Cardinality getCardinality() {
		return cardinality;
	}

	public RecordStructureDto setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
		return this;
	}

	public static class Cardinality {
		private String maxExclusive;
		private String maxInclusive;
		private String minExclusive;
		private String minInclusive;

		public Cardinality(String maxExclusive, String maxInclusive, String minExclusive, String minInclusive) {
			super();
			this.maxExclusive = maxExclusive;
			this.maxInclusive = maxInclusive;
			this.minExclusive = minExclusive;
			this.minInclusive = minInclusive;
		}

		public String getMaxExclusive() {
			return maxExclusive;
		}

		public void setMaxExclusive(String maxExclusive) {
			this.maxExclusive = maxExclusive;
		}

		public String getMaxInclusive() {
			return maxInclusive;
		}

		public void setMaxInclusive(String maxInclusive) {
			this.maxInclusive = maxInclusive;
		}

		public String getMinExclusive() {
			return minExclusive;
		}

		public void setMinExclusive(String minExclusive) {
			this.minExclusive = minExclusive;
		}

		public String getMinInclusive() {
			return minInclusive;
		}

		public void setMinInclusive(String minInclusive) {
			this.minInclusive = minInclusive;
		}

	}
}
