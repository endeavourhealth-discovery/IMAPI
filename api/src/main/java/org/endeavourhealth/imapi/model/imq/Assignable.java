package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.function.Consumer;

public interface Assignable {
  public Operator getOperator();

  public Assignable setOperator(Operator operator);

  public String getValue();

  public Assignable setValue(String value);

  Assignable setQualifier(String qualifier);
  String getQualifier();
  String getValueLabel();
  Assignable setValueLabel(String label);

  TTIriRef getUnit();

  Assignable setUnit(TTIriRef unit);


  Assignable setValueParameter(String parameter);

  String getValueParameter();

}
