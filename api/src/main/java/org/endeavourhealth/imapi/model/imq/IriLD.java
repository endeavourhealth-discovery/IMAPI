package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Objects;



@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({"iri","type","set","name"})
public class IriLD{
	private String iri;
	private String type;
	private String set;
	private String name;

	@JsonProperty("@id")
	public String getIri() {
		return iri;
	}

	public IriLD setIri(String iri) {
		this.iri = assignIri(iri);
		return this;
	}

	@JsonProperty("@type")
	public String getType() {
		return type;
	}

	public IriLD setType(String type) {
		this.type = assignIri(set);
		return this;
	}

	@JsonProperty("@set")
	public String getSet() {
		return set;
	}

	public IriLD setSet(String set) {
		this.set = assignIri(set);
		return this;
	}

	public String getName() {
		return name;
	}

	public IriLD setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IriLD)) return false;
		IriLD ttIriRef = (IriLD) o;
		return iri.equals(ttIriRef.iri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(iri);
	}

	public String assignIri(String iri){
		if (iri != null && !iri.isEmpty()) {
			if (!iri.matches("([a-z]+)?[:].*")) {
				return IM.NAMESPACE + iri;
			}
		}
		return iri;
	}
}
