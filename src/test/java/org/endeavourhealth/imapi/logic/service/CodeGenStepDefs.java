package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGenStepDefs {
  private CodeGenDto template = new CodeGenDto();
  private TTIriRef model;
  private List<DataModelProperty> properties = new ArrayList<>();
  private String namespace;
  private String actual;

  @Given("a model with iri {string} and name {string} and description {string}")
  public void setModel(String arg0, String arg1, String arg2) {
    this.model = new TTIriRef(arg0, arg1).setDescription(arg2);
  }

  @Given("a template of")
  public void setTemplate(String arg0) {
    this.template.setTemplate(arg0);
  }

  @Given("a namespace {string}")
  public void setNamespace(String arg0) {
    this.namespace = arg0;
  }

  @Given("a file extension {string}")
  public void setFileExtension(String arg0) {
    this.template.setExtension(arg0);
  }

  @Given("a collection wrapper {string}")
  public void setCollectionWrapper(String arg0) {
    this.template.setCollectionWrapper(arg0);
  }

  @Given("a datatype map")
  public void setDataTypeMap(String arg0) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      this.template.setDatatypeMap(om.readValue(arg0, new TypeReference<>() {}));
    }
  }

  @Given("properties")
  public void setProperties(String arg0) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      this.properties = om.readValue(arg0, new TypeReference<>() {
      });
    }
  }

  @When("the code is generated")
  public void generateCode() {
    CodeGenService cg = new CodeGenService();
    this.actual = cg.generateCodeForModel(template, model, properties, namespace);
  }

  @Then("the output should be")
  public void checkResult(String expected) {
    assertEquals(expected, actual);
  }
}
