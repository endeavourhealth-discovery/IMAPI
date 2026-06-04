package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.Operator;

public interface Assignable {
  Operator getOperator();

  Assignable setOperator(Operator operator);

  String getValue();

  Assignable setValue(String value);

  String getValueLabel();

  Assignable setValueLabel(String label);

  String getDescription();

  Assignable setDescription(String description);

  CompareExtended getCompare();

  Assignable setCompare(CompareExtended compareExtended);

  boolean isInvalid();

  Assignable setIsInvalid(boolean invalid);

  String getValueTerm();

  Assignable setValueTerm(String valueTerm);

  TTIriRefExtended getUnits();

  Assignable setUnits(TTIriRefExtended units);

}
