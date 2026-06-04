package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.NamespaceVocab;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "TTIriRef", description = "Class representing an IRI")
public class TTIriRefExtended extends TTIriRef implements TTValue, Serializable {
  private static final Pattern iriPattern = Pattern.compile("([a-z]+)?[:].*");

  public TTIriRefExtended() {
  }

  public TTIriRefExtended(String iri) {
    setIri(iri);
  }

  public TTIriRefExtended(Enum<?> vocabEnum) {
    setIri(EnumUtils.asIri(vocabEnum).getIri());
  }

  public TTIriRefExtended(String iri, String name) {
    setIri(iri);
    setName(name);
  }

  public static TTIriRefExtended iri(String iri, String name) {
    return new TTIriRefExtended(iri, name);
  }

  @JsonProperty(value = "iri", required = true)
  public String getIri() {
    return this.getIri();
  }

  public TTIriRefExtended iri(String iri) {
    this.setIri(iri);
    if (iri != null && !iri.isEmpty() && !iriPattern.matcher(iri).matches()) {
      iri = NamespaceVocab.IM + iri;
      if (!iriPattern.matcher(iri).matches())
        Thread.dumpStack();
    }
    return this;
  }

  @JsonIgnore
  public TTIriRefExtended iri(Enum<?> iri) {
    return iri(EnumUtils.asIri(iri).getIri());
  }

  public TTIriRefExtended name(String name) {
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
  public TTIriRefExtended asIriRef() {
    return this;
  }

  @Override
  @JsonIgnore
  public boolean isIriRef() {
    return true;
  }

  public TTIriRefExtended description(String description) {
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
    if (!(o instanceof TTIriRefExtended ttIriRef)) return false;
    return this.getIri().equals(ttIriRef.getIri());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getIri());
  }
}
