package org.endeavourhealth.imapi.utility;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SystemStubsExtension.class)
public class EnvHelperStepDefs {

  @SystemStub
  private EnvironmentVariables environmentVariables = new EnvironmentVariables();

  @Before
  public void setup() throws Exception {
    environmentVariables.setup();
  }

  @Given("the environment variable \"HOSTING_MODE\" is unset")
  public void hostingModeUnset() {
    environmentVariables.remove("HOSTING_MODE");
  }

  @Given("the environment variable \"HOSTING_MODE\" is set to \"public\"")
  public void hostingModePublic() {
    environmentVariables.set("HOSTING_MODE", "public");
    assert EnvHelper.isPublicMode();
  }

  @Given("the environment variable \"HOSTING_MODE\" is set to anything except \"public\"")
  public void hostingModePrivate() {
    environmentVariables.set("HOSTING_MODE", "private");
  }

  @Then("the server runs in \"private\" mode")
  public void theServerRunsInPrivateMode() {
    assertFalse(EnvHelper.isPublicMode());
  }

  @Then("the server runs in \"public\" mode")
  public void theServerRunsInPublicMode() {
    assertTrue(EnvHelper.isPublicMode());
  }

}
