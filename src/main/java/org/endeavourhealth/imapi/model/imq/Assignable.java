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

  Assignable setArgument(List<Argument> arguments);
  List<Argument> getArgument();

  TTIriRef getUnit();
  Assignable setUnit(TTIriRef units);

  Assignable addArgument(Argument argument);
  Assignable argument(Consumer<Argument> builder);



}
