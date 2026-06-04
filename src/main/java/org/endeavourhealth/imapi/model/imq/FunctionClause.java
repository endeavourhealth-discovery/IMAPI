package org.endeavourhealth.imapi.model.imq;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FunctionClause extends IriLD {
  private List<ArgumentExtended> argument;


  public FunctionClause setIri(String iri) {
    super.setIri(iri);
    return this;
  }


  public FunctionClause setName(String name) {
    super.setName(name);
    return this;
  }

  public List<ArgumentExtended> getArgument() {
    return argument;
  }

  public FunctionClause setArgument(List<ArgumentExtended> argument) {
    this.argument = argument;
    return this;
  }


  public FunctionClause addArgument(ArgumentExtended argument) {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    this.argument.add(argument);
    return this;
  }

  public FunctionClause argument(Consumer<ArgumentExtended> builder) {
    ArgumentExtended argument = new ArgumentExtended();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }


}
