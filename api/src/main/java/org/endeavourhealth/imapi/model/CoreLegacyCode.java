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

    private String im1Id;

    private String legacyIm1Id;

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

    public String getIm1Id() {
        return im1Id;
    }

    public CoreLegacyCode setIm1Id(String im1Id) {
        this.im1Id = im1Id;
        return this;
    }

    public String getLegacyIm1Id() {
        return legacyIm1Id;
    }

    public CoreLegacyCode setLegacyIm1Id(String legacyIm1Id) {
        this.legacyIm1Id = legacyIm1Id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoreLegacyCode that = (CoreLegacyCode) o;
        return Objects.equals(iri, that.iri) && Objects.equals(legacyIri, that.legacyIri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iri, legacyIri);
    }
}
