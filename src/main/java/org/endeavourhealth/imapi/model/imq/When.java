package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.imq.Expression;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Where;

import java.util.List;
import java.util.function.Consumer;

public class
When extends Where {

  private String value;
  private Expression then;

  private boolean exists;

  public boolean isExists() {
    return exists;
  }

  public When setExists(boolean exists) {
    this.exists = exists;
    return this;
  }

  public String getValue() {
    return value;
  }

  public When setValue(String value) {
    this.value = value;
    return this;
  }

  public When setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public When setIsNotNull(boolean isNotNull) {
    super.setIsNotNull(isNotNull);
    return this;
  }

  public When setIsNull(boolean isNull) {
    super.setIsNull(isNull);
    return this;
  }

  public When setNodeRef(String nodeRef) {
    super.setNodeRef(nodeRef);
    return this;
  }

  public When setIs(List<Node> is) {
    super.setIs(is);
    return this;
  }

  public When or(Consumer<Where> builder) {
    super.or(builder);
    return this;
  }

  public When and(Consumer<Where> builder) {
    super.and(builder);
    return this;
  }

  public Expression getThen() {
    return then;
  }

  public When setThen(Expression then) {
    this.then = then;
    return this;
  }

  public When then(Consumer<Expression> builder) {
    this.then = new Expression();
    builder.accept(this.then);
    return this;
  }
}
