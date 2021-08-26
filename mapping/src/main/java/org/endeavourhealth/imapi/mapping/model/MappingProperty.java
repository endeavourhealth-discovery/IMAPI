package org.endeavourhealth.imapi.mapping.model;

public class MappingProperty {

	private String predicate;
	private String nestedPredicate;
	private boolean isBnode;

	public MappingProperty() {

	}

	public MappingProperty(String predicate) {
		super();
		this.predicate = predicate;
	}

	public MappingProperty(String predicate, String nestedPredicate, boolean isBnode) {
		super();
		this.predicate = predicate;
		this.nestedPredicate = nestedPredicate;
		this.isBnode = isBnode;
	}

	public boolean isNested() {
		return this.nestedPredicate != null;
	}

	public String getPredicate() {
		return predicate;
	}

	public MappingProperty setPredicate(String predicate) {
		this.predicate = predicate;
		return this;
	}

	public String getNestedPredicate() {
		return nestedPredicate;
	}

	public MappingProperty setNestedPredicate(String nestedPredicate) {
		this.nestedPredicate = nestedPredicate;
		return this;
	}

	public boolean isBnode() {
		return isBnode;
	}

	public MappingProperty setBnode(boolean isBnode) {
		this.isBnode = isBnode;
		return this;
	}

}
