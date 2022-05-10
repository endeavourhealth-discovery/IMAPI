package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ConceptRef extends TTIriRef{



	private boolean includeSubtypes;
	private boolean includeSupertypes;



	public static ConceptRef iri(String iri) {
		return new ConceptRef(iri);
	}

	public static ConceptRef iri(String iri, String name) {
		return new ConceptRef(iri, name);
	}




	public ConceptRef setIncludeSubtypes(boolean subtypes) {
		this.includeSubtypes = subtypes;
		return this;
	}

	public boolean isIncludeSubtypes() {
		return includeSubtypes;
	}

	public boolean isIncludeSupertypes() {
		return includeSupertypes;
	}

	public ConceptRef setIncludeSupertypes(boolean supertypes) {
		this.includeSupertypes = supertypes;
		return this;
	}


	public ConceptRef() {
	}
	public ConceptRef(String iri) {
		super.setIri(iri);
	}
	public ConceptRef(String iri, String name) {
		super.setIri(iri);
		super.setName(name);
	}




}
