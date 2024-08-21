package org.endeavourhealth.imapi.model.search;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class SearchTermCode {
  String term;
  String code;
  TTIriRef status;

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
}
