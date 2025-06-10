package org.endeavourhealth.imapi.model.imq;

public interface Entailment {
  boolean isAncestorsOf();

  Entailment setAncestorsOf(boolean ancestorsOf);

  boolean isDescendantsOrSelfOf();

  Entailment setDescendantsOrSelfOf(boolean descendantsOrSelfOf);

  boolean isDescendantsOf();

  Entailment setDescendantsOf(boolean descendantsOf);

  boolean isMemberOf();

  Entailment setMemberOf(boolean descendantsOf);

}
