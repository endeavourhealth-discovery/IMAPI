package org.endeavourhealth.imapi.model.imq;

import java.util.function.Consumer;

import org.endeavourhealth.imapi.model.imq.ValueSource;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Compare {

  private ValueSource left;
  private ValueSource right;

  public ValueSource getLeft() {
    return left;
  }

  public Compare setLeft(ValueSource left) {
    this.left = left;
    return this;
  }

  public Compare left(Consumer<ValueSource> builder) {
    this.left = new ValueSource();
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
    this.right = new ValueSource();
    builder.accept(this.right);
    return this;
  }

}
