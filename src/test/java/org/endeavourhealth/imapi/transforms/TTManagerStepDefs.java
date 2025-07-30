package org.endeavourhealth.imapi.transforms;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TTManagerStepDefs {
  private TTEntity entity;

  @Given("an entity")
  public void anEntity() {
    entity = new TTEntity();
  }

  @And("it has a list of term codes")
  public void itHasAListOfTermCodes() {
    entity.set(iri(IM.HAS_TERM_CODE), new TTArray());
  }

  @And("there is a term code with label {string}")
  public void thereIsATermCodeWithLabel(String arg0) {
    TTNode node = new TTNode();
    node.set(iri(RDFS.LABEL), literal(arg0));
    entity.get(iri(IM.HAS_TERM_CODE)).add(node);
  }

  @And("there is a term code with no label")
  public void thereIsATermCodeWithNoLabel() {
    TTNode node = new TTNode();
    entity.get(iri(IM.HAS_TERM_CODE)).add(node);
  }

  @Then("termUsed\\({string}) should return {bool}")
  public void termUsedShouldReturnTrue(String arg0, boolean arg1) {
    assertEquals(TTManager.termUsed(entity, arg0), arg1);
  }
}
