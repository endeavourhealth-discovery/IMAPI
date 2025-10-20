package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface Assignable {
  Operator getOperator();

  Assignable setOperator(Operator operator);

  String getValue();

  Assignable setValue(String value);

  Assignable setQualifier(TTIriRef qualifier);

  TTIriRef getQualifier();

  String getValueLabel();

  Assignable setValueLabel(String label);
  Assignable setDescription(String description);
  String getDescription();
  FunctionClause getFunction();
  Assignable setFunction(FunctionClause function);
  TTIriRef getUnits();
  Assignable setUnits(TTIriRef unit);



}
