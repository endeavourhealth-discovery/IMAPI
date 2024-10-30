package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface Assignable {
  public Operator getOperator();

  public Assignable setOperator(Operator operator);

  public String getValue();

  public Assignable setValue(String value);

  public String getUnit();

  public Assignable setUnit(String unit);
  Assignable setQualifier(String qualifier);
  String getQualifier();
  String getValueLabel();
  Assignable setValueLabel(String label);

}
