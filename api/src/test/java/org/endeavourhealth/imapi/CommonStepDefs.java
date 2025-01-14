package org.endeavourhealth.imapi;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class CommonStepDefs {

  @SystemStub
  public static EnvironmentVariables environmentVariables;

  @Before
  public void setup() throws Exception {
    environmentVariables = new EnvironmentVariables();
    environmentVariables.setup();
  }

  @When("the environment variable {string} is set to {string}")
  public void setEnvironmentVariable(String key, String value) {
    environmentVariables.set(key, value);
  }

  @Given("the environment variable {string} is unset")
  public void unsetEnvironmentVariable(String key) {
    environmentVariables.remove(key);
  }
}
