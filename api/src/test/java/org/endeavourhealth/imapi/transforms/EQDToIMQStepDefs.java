package org.endeavourhealth.imapi.transforms;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EQDToIMQStepDefs {
  private EqdToIMQ eqdToIMQ = new EqdToIMQ();

  @When("EDQ document {string} is passed into transformEqd")
  public void edq_document_is_passed_into_transform_eqd(String eqdIn) {
    assertEquals("a", eqdIn);
  }

  @Then("the output should be {string}")
  public void the_output_should_be_b(String eqdOut) {
    assertEquals("a", eqdOut);
  }

}
