package org.endeavourhealth.imapi.utility;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnvHelperStepDefs {


  @Then("the server runs in \"private\" mode")
  public void theServerRunsInPrivateMode() {
    Assertions.assertFalse(EnvHelper.isPublicMode());
  }

  @Then("the server runs in \"public\" mode")
  public void theServerRunsInPublicMode() {
    assertTrue(EnvHelper.isPublicMode());
  }

}
