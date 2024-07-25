package org.endeavourhealth.imapi.model.imq;

import java.util.function.Consumer;

public class When {
  private Where property;
  private ReturnProperty then;

  public Where getProperty() {
    return property;
  }

  public When setProperty(Where property) {
    this.property = property;
    return this;
  }

  public When property(Consumer<Where> builder) {
    this.property = new Where();
    builder.accept(this.property);
    return this;
  }

  public ReturnProperty getThen() {
    return then;
  }

  public When setThen(ReturnProperty then) {
    this.then = then;
    return this;
  }

  public When then(Consumer<ReturnProperty> builder) {
    this.then = new ReturnProperty();
    builder.accept(this.then);
    return this;
  }


}
