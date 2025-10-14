package org.endeavourhealth.imapi.model.imq;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FunctionClause extends IriLD {
  private List<Argument> argument;




  public FunctionClause setName(String name) {
    super.setName(name);
    return this;
  }

  public List<Argument> getArgument() {
    return argument;
  }

  public FunctionClause setArgument(List<Argument> argument) {
    this.argument = argument;
    return this;
  }


  public FunctionClause addArgument(Argument argument) {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    this.argument.add(argument);
    return this;
  }

  public FunctionClause argument(Consumer<Argument> builder) {
    Argument argument = new Argument();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }



}
