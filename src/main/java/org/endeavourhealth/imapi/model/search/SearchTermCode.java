package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Comparator;
import java.util.Objects;

public class SearchTermCode implements Comparable<SearchTermCode> {
  String term;
  String code;
  TTIriRef status;
  Integer length;
  String keyTerm;

  public Integer getLength() {
    return length;
  }

  public SearchTermCode setLength(Integer length) {
    this.length = length;
    return this;
  }

  public String getKeyTerm() {
    return keyTerm;
  }

  public SearchTermCode setKeyTerm(String keyTerm) {
    this.keyTerm = keyTerm;
    return this;
  }

  public String getTerm() {
    return term;
  }

  public SearchTermCode setTerm(String term) {
    this.term = term;
    return this;
  }

  public String getCode() {
    return code;
  }

  public SearchTermCode setCode(String code) {
    this.code = code;
    return this;
  }

  public TTIriRef getStatus() {
    return status;
  }

  @JsonSetter
  public SearchTermCode setStatus(TTIriRef status) {
    this.status = status;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SearchTermCode that)) return false;
    return Objects.equals(getTerm(), that.getTerm()) && Objects.equals(getCode(), that.getCode()) && Objects.equals(getStatus(), that.getStatus());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTerm(), getCode(), getStatus());
  }

  @Override
  public int compareTo(SearchTermCode o) {
    return Comparator.<SearchTermCode, String>
        comparing(ts -> ts.getStatus() == null ? null : ts.getStatus().getIri(), Comparator.nullsLast(Comparator.naturalOrder()))
      .thenComparing(ts -> (ts.getTerm() == null || ts.getTerm().isEmpty()) ? null : ts.getTerm(), Comparator.nullsLast(Comparator.naturalOrder()))
      .compare(this, o);
  }
}
