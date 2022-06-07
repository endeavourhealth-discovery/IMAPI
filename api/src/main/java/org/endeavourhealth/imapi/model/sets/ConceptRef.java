package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ConceptRef extends Alias{

	private boolean includeSubtypes;
	private boolean includeSupertypes;
	private boolean includeValueSets;


	public ConceptRef setName(String name){
		super.setName(name);
		return this;
	}

	public ConceptRef setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public boolean isIncludeValueSets() {
		return includeValueSets;
	}

	public ConceptRef setIncludeValueSets(boolean includeValueSets) {
		this.includeValueSets = includeValueSets;
		return this;
	}

	public static ConceptRef iri(String iri) {
		return new ConceptRef(iri);
	}

	public static ConceptRef iri(TTIriRef iri) {
		ConceptRef ref= new ConceptRef();
		ref.setIri(iri.getIri());
		if (iri.getName()!=null)
			ref.setName(iri.getName());
		return ref;
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
		if (name!=null)
		 super.setName(name);
	}
	public ConceptRef(TTIriRef iriRef){
		super.setIri(iriRef.getIri());
		if (iriRef.getName()!=null)
			super.setName(iriRef.getName());
	}




}
