package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.ArgumentExtended;
import org.endeavourhealth.interfacemanager.model.GraphVocab;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FunctionRequest {
  private String functionIri;
  private List<ArgumentExtended> arguments;
  private Page page;
  private GraphVocab graph;

  public FunctionRequest setFunctionIri(String functionIri) {
    this.functionIri = functionIri;
    return this;
  }

  public FunctionRequest setArguments(List<ArgumentExtended> arguments) {
    this.arguments = arguments;
    return this;
  }

  public FunctionRequest addArgument(ArgumentExtended argument) {
    if (null == argument) this.arguments = new ArrayList<>();
    this.arguments.add(argument);
    return this;
  }

  public FunctionRequest setPage(Page page) {
    this.page = page;
    return this;
  }

  public FunctionRequest setGraph(GraphVocab graph) {
    this.graph = graph;
    return this;
  }
}
