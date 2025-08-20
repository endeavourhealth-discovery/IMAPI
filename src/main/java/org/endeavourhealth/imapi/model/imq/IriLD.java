package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.util.Objects;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({"iri", "qualifier", "name", "description"})
public class IriLD {
  private String iri;
  private String name;
  private String qualifier;
  private String description;
  @Getter
  @Setter
  private String uuid;



  public String getDescription() {
    return description;
  }

  public IriLD setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getQualifier() {
    return qualifier;
  }

  public IriLD setQualifier(String qualifier) {
    this.qualifier = qualifier;
    return this;
  }

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
    if (!(o instanceof IriLD ttIriRef)) return false;
    if (ttIriRef.iri == null || iri == null)
      return false;
    return iri.equals(ttIriRef.iri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iri);
  }

  public String assignIri(String iri) {
    if (iri != null && !iri.isEmpty()) {
      if (!iri.matches("([a-z]+)?[:].*")) {
        return Namespace.IM + iri;
      }
    }
    return iri;
  }
}
