package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "TTIriRef", description = "Class representing an IRI")
public class TTIriRef implements TTValue, Serializable {
  private static Pattern iriPattern = Pattern.compile("([a-z]+)?[:].*");

  @Schema(description = "The actual iri")
  private String iri;

  @Getter
  @JsonProperty(defaultValue = "")
  @Schema(description = "The name of the concept the IRI represents")
  private String name;

  @Getter
  @JsonProperty(defaultValue = "")
  @Schema(description = "Longer/fuller description of the represented concept")
  private String description;

  public TTIriRef() {
  }

  public TTIriRef(String iri) {
    setIri(iri);
  }


  public TTIriRef(String iri, String name) {
    setIri(iri);
    setName(name);
  }

  public static TTIriRef iri(String iri) {
    return new TTIriRef(iri);
  }

  public static TTIriRef iri(VocabEnum vocabEnum) {
    return vocabEnum.asIri();
  }

  public static TTIriRef iri(String iri, String name) {
    return new TTIriRef(iri, name);
  }

  @JsonProperty(value = "iri", required = true)
  public String getIri() {
    return this.iri;
  }

  public TTIriRef setIri(String iri) {
    this.iri = iri;
    if (iri != null && !iri.isEmpty() && !iriPattern.matcher(iri).matches()) {
      iri = Namespace.IM + iri;
      if (!iriPattern.matcher(iri).matches())
        Thread.dumpStack();
    }
    return this;
  }

  @JsonIgnore
  public TTIriRef setIri(VocabEnum iri) {
    return setIri(iri.toString());
  }

  public TTIriRef setName(String name) {
    if (name != null && name.startsWith("null"))
      System.err.println("Its here!!!!");
    this.name = name;
    return this;
  }

  @JsonIgnore
  public boolean hasName() {
    return name != null && !name.isEmpty();
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


  public TTIriRef setDescription(String description) {
    this.description = description;
    return this;
  }

  @JsonIgnore
  public boolean hasDescription() {
    return description != null && !description.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TTIriRef ttIriRef)) return false;
    return iri.equals(ttIriRef.iri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iri);
  }
}
