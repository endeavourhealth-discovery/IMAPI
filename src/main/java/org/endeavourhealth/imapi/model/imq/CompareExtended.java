package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.Compare;
import org.endeavourhealth.interfacemanager.model.ValueSource;

import java.util.function.Consumer;

public class CompareExtended extends Compare {

  public CompareExtended left(ValueSource left) {
    this.setLeft(left);
    return this;
  }

  public CompareExtended left(Consumer<ValueSource> builder) {
    this.setLeft(new ValueSource());
    builder.accept(this.getLeft());
    return this;
  }

  public CompareExtended right(ValueSource right) {
    this.setRight(right);
    return this;
  }

  public CompareExtended right(Consumer<ValueSource> builder) {
    this.setRight(new ValueSource());
    builder.accept(this.getRight());
    return this;
  }

  public CompareExtended units(TTIriRefExtended units) {
    this.setUnits(units);
    return this;
  }
}
