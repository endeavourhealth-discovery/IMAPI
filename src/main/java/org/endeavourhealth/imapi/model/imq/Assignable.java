package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.interfacemanager.model.Compare;
import org.endeavourhealth.interfacemanager.model.Operator;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

public interface Assignable {
  Operator getOperator();

  Assignable setOperator(Operator operator);

  String getValue();

  Assignable setValue(String value);

  String getValueLabel();

  Assignable setValueLabel(String label);

  String getDescription();

  Assignable setDescription(String description);

  Compare getCompare();

  Assignable setCompare(Compare compare);

  boolean isInvalid();

  Assignable setIsInvalid(boolean invalid);

  String getValueTerm();

  Assignable setValueTerm(String valueTerm);

  TTIriRef getUnits();

  Assignable setUnits(TTIriRef units);

}
