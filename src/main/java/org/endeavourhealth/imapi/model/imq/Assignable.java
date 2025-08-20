package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.function.Consumer;

public interface Assignable {
  Operator getOperator();

  Assignable setOperator(Operator operator);

  String getValue();

  Assignable setValue(String value);

  Assignable setQualifier(String qualifier);

  String getQualifier();

  String getValueLabel();

  Assignable setValueLabel(String label);



  FunctionClause getFunction();
  Assignable setFunction(FunctionClause function);



}
