package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FunctionRequest {
  private String functionIri;
  private List<Argument> arguments;
  private Page page;

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
}
