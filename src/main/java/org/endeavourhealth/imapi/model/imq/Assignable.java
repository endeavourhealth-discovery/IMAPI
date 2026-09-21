package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface Assignable {
  Operator getOperator();

  Assignable setOperator(Operator operator);

  String getValue();

  Assignable setValue(String value);

  String getValueLabel();

  Assignable setValueLabel(String label);

  String getDescription();

  Assignable setDescription(String description);


  boolean isInvalid();

  Assignable setIsInvalid(boolean invalid);

  String getValueTerm();

  Assignable setValueTerm(String valueTerm);

  TTIriRef getUnits();

  Assignable setUnits(TTIriRef units);
}
