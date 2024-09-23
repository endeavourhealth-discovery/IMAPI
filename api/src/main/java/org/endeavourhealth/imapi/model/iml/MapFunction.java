package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri", "name", "argument"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapFunction extends TTIriRef {
  List<Argument> argument;
  private Map<String, String> conceptMap;
  private TTIriRef defaultValue;

  public TTIriRef getDefaultValue() {
    return defaultValue;
  }

  @JsonSetter
  public MapFunction setDefaultValue(TTIriRef defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  public MapFunction setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  public Map<String, String> getConceptMap() {
    return conceptMap;
  }

  public MapFunction setConceptMap(Map<String, String> conceptMap) {
    this.conceptMap = conceptMap;
    return this;
  }

  public MapFunction addToConceptMap(String from, String to) {
    if (this.conceptMap == null)
      this.conceptMap = new HashMap<>();
    this.conceptMap.put(from, to);
    return this;
  }

  @Override
  public String getName() {
    return super.getName();
  }

  @Override
  public MapFunction setName(String name) {
    super.setName(name);
    return this;
  }

  public List<Argument> getArgument() {
    return argument;
  }

  public MapFunction setArgument(List<Argument> argument) {
    this.argument = argument;
    return this;
  }

  public MapFunction addArgument(Argument argument) {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    this.argument.add(argument);
    return this;
  }

  public MapFunction argument(Consumer<Argument> builder) {
    Argument argument = new Argument();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }


  public Argument addArgument() {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    Argument newArg = new Argument();
    this.argument.add(newArg);
    return newArg;
  }


  @JsonSetter
  public MapFunction setIri(TTIriRef iri) {
    super.setIri(iri.getIri());
    return this;
  }
}
