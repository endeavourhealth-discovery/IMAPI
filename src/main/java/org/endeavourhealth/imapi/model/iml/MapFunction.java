package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.imq.ArgumentExtended;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@JsonPropertyOrder({"iri", "name", "argument"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapFunction extends TTIriRefExtended {
  List<ArgumentExtended> argument;
  private Map<String, String> conceptMap;
  private TTIriRefExtended defaultValue;

  public TTIriRefExtended getDefaultValue() {
    return defaultValue;
  }

  @JsonSetter
  public MapFunction setDefaultValue(TTIriRefExtended defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  @Override
  public MapFunction iri(String iri) {
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
  public MapFunction name(String name) {
    super.setName(name);
    return this;
  }

  public List<ArgumentExtended> getArgument() {
    return argument;
  }

  public MapFunction setArgument(List<ArgumentExtended> argument) {
    this.argument = argument;
    return this;
  }

  public MapFunction addArgument(ArgumentExtended argument) {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    this.argument.add(argument);
    return this;
  }

  public MapFunction argument(Consumer<ArgumentExtended> builder) {
    ArgumentExtended argument = new ArgumentExtended();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }


  public ArgumentExtended addArgument() {
    if (this.argument == null)
      this.argument = new ArrayList<>();
    ArgumentExtended newArg = new ArgumentExtended();
    this.argument.add(newArg);
    return newArg;
  }


  @JsonSetter
  public MapFunction setIri(TTIriRefExtended iri) {
    super.setIri(iri.getIri());
    return this;
  }
}
