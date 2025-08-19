package org.endeavourhealth.imapi.transforms;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.model.imq.ECLQueryRequest;
import org.endeavourhealth.imapi.model.imq.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECLToIMQStepDefs {
  private ECLToIMQ eclToIMQ = new ECLToIMQ();
  private IMQToECL imqToECL = new IMQToECL();
  private Query query;

  @Then("getEclFromQuery should equal original ecl {string}")
  public void GetEclFromQueryShouldEqualEcl(String ecl) {
    ECLQueryRequest eclQuery = new ECLQueryRequest();
    eclQuery.setQuery(query);
    imqToECL.getECLFromQuery(eclQuery);
    assertEquals(ecl, eclQuery.getEcl().replaceAll("\n", "")
      .replaceAll("\t", "")
      .replaceAll("or ", "OR "));
  }

  @When("getQueryFromEcl is called with ecl {string}")
  public void getQueryFromECLIsCalledWithEclEcl(String ecl) {
    ECLQueryRequest eclQueryRequest = new ECLQueryRequest();
    eclQueryRequest.setEcl(ecl);
    eclToIMQ.getQueryFromECL(eclQueryRequest);
    query = eclQueryRequest.getQuery();
  }
}
