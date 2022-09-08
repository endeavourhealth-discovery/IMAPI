package org.endeavourhealth.imapi.model.iml;

public class Compare {
	private String source;
	private String comparison;
	private String target;

	public String getSource() {
		return source;
	}

	public Compare setSource(String source) {
		this.source = source;
		return this;
	}

	public String getComparison() {
		return comparison;
	}

	public Compare setComparison(String comparison) {
		this.comparison = comparison;
		return this;
	}

	public String getTarget() {
		return target;
	}

	public Compare setTarget(String target) {
		this.target = target;
		return this;
	}
}
