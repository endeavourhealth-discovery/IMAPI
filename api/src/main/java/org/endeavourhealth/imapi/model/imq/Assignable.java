package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public interface Assignable {
  public Operator getOperator();

  public Assignable setOperator(Operator operator);

  public String getValue();

  public Assignable setValue(String value);
  List<Argument> getArgument();
  Assignable setArgument(List<Argument> arguments);
  Assignable setQualifier(String qualifier);
  String getQualifier();
  String getValueLabel();
  Assignable setValueLabel(String label);

  String getUnit();

  Assignable setUnit(String unit);

}
