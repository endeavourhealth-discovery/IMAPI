package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@JsonPropertyOrder({"path","name","aiias","inverse"})
public class Property {
	private String path;
	private String name;
	private String alias;
	private boolean inverse;

	public boolean isInverse() {
		return inverse;
	}

	public Property setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public Property(){}

	public Property setIri(TTIriRef iri){
		this.path= iri.getIri();
		return this;
	}

	public Property setIri(String iri){
		TTIriRef iriValid= TTIriRef.iri(iri);
		this.path= iriValid.getIri();
		return this;
	}

	public String getPath() {
		return path;
	}

	public Property setPath(String path) {
		this.path = path;
		return this;
	}

	public String getName() {
		return name;
	}

	public Property setName(String name) {
		this.name = name;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public Property setAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
