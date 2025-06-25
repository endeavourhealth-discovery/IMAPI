package org.endeavourhealth.imapi.transforms;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECLToIMQStepDefs {
  private ECLToIMQ eclToIMQ = new ECLToIMQ();
  private IMQToECL imqToECL = new IMQToECL();
  private Query query;

  @Then("getEclFromQuery should equal original ecl {string}")
  public void GetEclFromQueryShouldEqualEcl(String ecl) throws Exception {

    assertEquals(ecl, imqToECL.getECLFromQuery(query, null).replaceAll("\n", "")
      .replaceAll("\t", "")
      .replaceAll("or ", "OR "));
  }

  @When("getQueryFromEcl is called with ecl {string}")
  public void getQueryFromECLIsCalledWithEclEcl(String ecl) throws EclFormatException {
    query = eclToIMQ.getQueryFromECL(ecl);
  }
}
