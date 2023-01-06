package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Case {
	private Range range;
	private String value;
	private String outputData;
	private TTIriRef outputIri;

	public Range getRange() {
		return range;
	}

	public Case setRange(Range range) {
		this.range = range;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Case setValue(String value) {
		this.value = value;
		return this;
	}

	public String getOutputData() {
		return outputData;
	}

	public Case setOutputData(String outputData) {
		this.outputData = outputData;
		return this;
	}

	public TTIriRef getOutputIri() {
		return outputIri;
	}

	public Case setOutputIri(TTIriRef outputIri) {
		this.outputIri = outputIri;
		return this;
	}
}
