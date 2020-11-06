package com.endavourhealth.models.objectModel;

public class ConceptReference {
    
	private String iri;
    private String name;
    private long id;
    
	public ConceptReference(long id) {
		super();
		this.id = id;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
