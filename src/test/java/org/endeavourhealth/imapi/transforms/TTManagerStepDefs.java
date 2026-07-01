package org.endeavourhealth.imapi.transforms;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteralJava.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TTManagerStepDefs {
  private TTEntityJava entity;

  @Given("an entity")
  public void anEntity() {
    entity = new TTEntityJava();
  }

  @And("it has a list of term codes")
  public void itHasAListOfTermCodes() {
    entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE), new TTArrayJava());
  }

  @And("there is a term code with label {string}")
  public void thereIsATermCodeWithLabel(String arg0) {
    TTNodeJava node = new TTNodeJava();
    node.set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), literal(arg0));
    entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)).add(node);
  }

  @And("there is a term code with no label")
  public void thereIsATermCodeWithNoLabel() {
    TTNodeJava node = new TTNodeJava();
    entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_TERM_CODE)).add(node);
  }

  @Then("termUsed\\({string}) should return {bool}")
  public void termUsedShouldReturnTrue(String arg0, boolean arg1) {
    assertEquals(TTManager.termUsed(entity, arg0), arg1);
  }
}
