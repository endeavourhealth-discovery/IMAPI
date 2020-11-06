package org.endeavourhealth.informationmanager.model;

public class ConceptReference {
    private Integer dbid;
    private String iri;
    private String name;

    public ConceptReference() {}

    public ConceptReference(String iri) {
        this.iri = iri;
    }

    public Integer getDbid() {
        return dbid;
    }

    public ConceptReference setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public ConceptReference setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConceptReference setName(String name) {
        this.name = name;
        return this;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iri == null) ? 0 : iri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConceptReference other = (ConceptReference) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}
    
    
}
