package org.endeavourhealth.imapi.model.dto;

public class RecordStructureDto {

	private ConceptReference property;
	private ConceptReference type;
	private ConceptReference inherited;
	private Cardinality cardinality;

	public RecordStructureDto() {

	}

	public ConceptReference getProperty() {
		return property;
	}

	public RecordStructureDto setProperty(ConceptReference property) {
		this.property = property;
		return this;
	}

	public ConceptReference getType() {
		return type;
	}

	public RecordStructureDto setType(ConceptReference type) {
		this.type = type;
		return this;
	}

	public ConceptReference getInherited() {
		if(null == inherited) return new ConceptReference();
		return inherited;
	}

	public RecordStructureDto setInherited(ConceptReference inherited) {
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

	public static class ConceptReference {
		private String iri;
		private String name;
		

		public ConceptReference(String iri, String name) {
			super();
			this.iri = iri;
			this.name = name;
		}

		public ConceptReference() {
			// TODO Auto-generated constructor stub
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIri() {
			return iri;
		}

		public void setIri(String iri) {
			this.iri = iri;
		}

	}

}
