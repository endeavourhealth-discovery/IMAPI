package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.vocabulary.types.Graph;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FunctionRequest {
  private String functionIri;
  private List<Argument> arguments;
  private Page page;
  private Graph graph;

  public FunctionRequest setFunctionIri(String functionIri) {
    this.functionIri = functionIri;
    return this;
  }

  public FunctionRequest setArguments(List<Argument> arguments) {
    this.arguments = arguments;
    return this;
  }

  public FunctionRequest addArgument(Argument argument) {
    if (null == argument) this.arguments = new ArrayList<>();
    this.arguments.add(argument);
    return this;
  }

  public FunctionRequest setPage(Page page) {
    this.page = page;
    return this;
  }

  public FunctionRequest setGraph(Graph graph) {
    this.graph = graph;
    return this;
  }
}
