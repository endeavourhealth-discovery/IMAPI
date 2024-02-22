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
	private String name;

	@JsonProperty("@id")
	public String getIri() {
		return iri;
	}

	public IriLD setIri(String iri) {
		this.iri = assignIri(iri);
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
		if (ttIriRef.iri==null||iri==null)
			return false;
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
