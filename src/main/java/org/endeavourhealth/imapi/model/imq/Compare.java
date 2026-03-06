package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.function.Consumer;

public class Compare {
  private ValueSource left;
  private ValueSource right;
  private TTIriRef units;

  public ValueSource getLeft() {
    return left;
  }

  public Compare setLeft(ValueSource left) {
    this.left = left;
    return this;
  }

  public Compare left(Consumer<ValueSource> builder) {
    this.left= new ValueSource();
    builder.accept(this.left);
    return this;
  }

  public ValueSource getRight() {
    return right;
  }

  public Compare setRight(ValueSource right) {
    this.right = right;
    return this;
  }

  public Compare right(Consumer<ValueSource> builder) {
    this.right= new ValueSource();
    builder.accept(this.right);
    return this;
  }

  public TTIriRef getUnits() {
    return units;
  }

  public Compare setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }
}
