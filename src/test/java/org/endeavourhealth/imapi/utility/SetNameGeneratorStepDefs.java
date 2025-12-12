package org.endeavourhealth.imapi.utility;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.imq.Node;
import org.junit.jupiter.api.Assertions;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetNameGeneratorStepDefs {
  private final SetRepository repo = new SetRepository();
  private final SetNameGenerator nameGen = new SetNameGenerator();

  private String setIri;
  private Set<String> members;
  private String setName;

  @When("I have a concept set {string}")
  public void setIri(String iri) {
    setIri = iri;
  }

  @When("I get its members")
  public void getSetMembers() {
    Pageable<Node> result = repo.getMembers(setIri, false, 1, 50);
    members = result.getResult().stream().map(Node::getName).collect(java.util.stream.Collectors.toSet());
  }

  @When("I call AI")
  public void iCallAI() {
    // Write code here that turns the phrase above into concrete actions
    setName = nameGen.generateSetName(members);
  }

  @Then("I should get a sensible name")
  public void iShouldGetASensibleName() {
    // Write code here that turns the phrase above into concrete actions
    System.out.println("======================================");
    System.out.println(setName);
    System.out.println("======================================");
  }
}
