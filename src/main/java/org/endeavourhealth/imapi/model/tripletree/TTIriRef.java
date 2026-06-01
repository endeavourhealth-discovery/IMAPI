package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.ITTIriRef;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "TTIriRef", description = "Class representing an IRI")
public class TTIriRef extends ITTIriRef implements TTValue, Serializable {
  private static final Pattern iriPattern = Pattern.compile("([a-z]+)?[:].*");

  public TTIriRef() {
  }

  public TTIriRef(String iri) {
    setIri(iri);
  }

  public TTIriRef(Enum<?> vocabEnum) {
    setIri(EnumUtils.asIri(vocabEnum).getIri());
  }

  public TTIriRef(String iri, String name) {
    setIri(iri);
    setName(name);
  }

  public static TTIriRef iri(String iri, String name) {
    return new TTIriRef(iri, name);
  }

  @JsonProperty(value = "iri", required = true)
  public String getIri() {
    return this.getIri();
  }

  @Override
  public TTIriRef iri(String iri) {
    this.setIri(iri);
    if (iri != null && !iri.isEmpty() && !iriPattern.matcher(iri).matches()) {
      iri = NAMESPACE.IM + iri;
      if (!iriPattern.matcher(iri).matches())
        Thread.dumpStack();
    }
    return this;
  }

  @JsonIgnore
  public TTIriRef iri(Enum<?> iri) {
    return iri(EnumUtils.asIri(iri).getIri());
  }

  @Override
  public TTIriRef name(String name) {
    if (name != null && name.startsWith("null"))
      System.err.println("Its here!!!!");
    this.setName(name);
    return this;
  }

  @JsonIgnore
  public boolean hasName() {
    return this.getName() != null && !this.getName().isEmpty();
  }

  @Override
  public TTIriRef asIriRef() {
    return this;
  }

  @Override
  @JsonIgnore
  public boolean isIriRef() {
    return true;
  }

  @Override
  public TTIriRef description(String description) {
    this.setDescription(description);
    return this;
  }

  @JsonIgnore
  public boolean hasDescription() {
    return this.getDescription() != null && !this.getDescription().isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TTIriRef ttIriRef)) return false;
    return this.getIri().equals(ttIriRef.getIri());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getIri());
  }
}
