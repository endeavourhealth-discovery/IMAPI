package org.endeavourhealth.imapi.logic.service;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevenshteinStepDefs {
  private String start;
  private String updated;

  @Given("start value is {string}")
  public void startValueIs(String arg0) {
    this.start = arg0;
  }

  @Given("start value is null")
  public void startValueIsNull() {
    this.start = null;
  }

  @When("the start value is not changed")
  public void theStartValueIsNotChanged() {
    this.updated = this.start;
  }

  @When("the start value changed to {string}")
  public void theStartValueChangedToEdited(String arg0) {
    this.updated = arg0;
  }

  @When("the start value changed to null")
  public void theStartValueChangedToNull() {
    this.updated = null;
  }

  @Then("the Levenshtein value should be {int}")
  public void theLevenshteinValueShouldBe(int arg0) {
    int actual = Levenshtein.calculate(start, updated);
    assertEquals(arg0, actual);
  }
}
