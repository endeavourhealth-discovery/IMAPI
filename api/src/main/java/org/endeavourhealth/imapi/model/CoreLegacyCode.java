package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Objects;

public class CoreLegacyCode {
	private String iri;
	private String code;
	private String term;
	private TTIriRef scheme;
	private String legacyIri;
	private String legacyCode;
	private String legacyTerm;
	private TTIriRef legacyScheme;

	public String getCode() {
		return code;
	}

	public CoreLegacyCode setCode(String code) {
		this.code = code;
		return this;
	}

	public String getTerm() {
		return term;
	}

	public CoreLegacyCode setTerm(String term) {
		this.term = term;
		return this;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public CoreLegacyCode setScheme(TTIriRef scheme) {
		this.scheme = scheme;
		return this;
	}

	public String getLegacyCode() {
		return legacyCode;
	}

	public CoreLegacyCode setLegacyCode(String legacyCode) {
		this.legacyCode = legacyCode;
		return this;
	}

	public String getLegacyTerm() {
		return legacyTerm;
	}

	public CoreLegacyCode setLegacyTerm(String legacyTerm) {
		this.legacyTerm = legacyTerm;
		return this;
	}

	public TTIriRef getLegacyScheme() {
		return legacyScheme;
	}

	public CoreLegacyCode setLegacyScheme(TTIriRef legacyScheme) {
		this.legacyScheme = legacyScheme;
		return this;
	}

	public String getIri() {
		return iri;
	}

	public CoreLegacyCode setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getLegacyIri() {
		return legacyIri;
	}

	public CoreLegacyCode setLegacyIri(String legacyIri) {
		this.legacyIri = legacyIri;
		return this;
	}
}
