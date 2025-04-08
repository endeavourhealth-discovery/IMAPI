package org.endeavourhealth.imapi.transforms;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EQDToIMQStepDefs {
  private EqdToIMQ eqdToIMQ = new EqdToIMQ();

  @When("EDQ document <EqdIn> is passed into transformEdq")
  public void edqDocumentEqdInIsPassedIntoTransformEdq() {
  }

  @Then("the output should be <EdqOut>")
  public void theOutputShouldBeEdqOut() {
  }
}
