package org.endeavourhealth.imapi.controllers;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.service.ConceptService;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

public class ConceptServiceStepDefs {
  private final String entityIri = "http://snomed.info/sct#44054006";
  private List<SearchTermCode> entityTermCodes;

  @InjectMocks
  private ConceptService conceptService = new ConceptService();
  @Mock
  private EntityRepository entityRepository = new EntityRepository();

  @Given("a Diabetes entity with out-of-order term codes")
  public void anEntityWithTermCodes() {
    TTArray termsArray = new TTArray();
    termsArray
      .add(new TTNode().set(RDFS.LABEL, literal("Type 2 diabetes")).set(IM.CODE, literal(197761014)).set(IM.HAS_STATUS, iri(IM.INACTIVE)))
      .add(new TTNode().set(RDFS.LABEL, literal("T2DM - diabetes mellitus type 2")).set(IM.CODE, literal(4571144010L)).set(IM.HAS_STATUS, iri(IM.ACTIVE)))
      .add(new TTNode().set(RDFS.LABEL, literal("Diabetes mellitus type 2")).set(IM.CODE, literal(197763012)).set(IM.HAS_STATUS, iri(IM.INACTIVE)))
      .add(new TTNode().set(RDFS.LABEL, literal("Diabetes mellitus type II")).set(IM.CODE, literal(73465010)).set(IM.HAS_STATUS, iri(IM.ACTIVE)));

    TTEntity entity = new TTEntity(entityIri);
    entity.set(iri(IM.HAS_TERM_CODE), termsArray);

    TTBundle termsBundle = new TTBundle();
    termsBundle.setEntity(entity);

    MockitoAnnotations.initMocks(this);
    when(entityRepository.getBundle(termsBundle.getEntity().getIri(), Stream.of(IM.HAS_TERM_CODE).collect(Collectors.toSet()))).thenReturn(termsBundle);
  }


  @When("the entity term codes are retrieved")
  public void getEntityTermCodes() {
    entityTermCodes = conceptService.getEntityTermCodes(entityIri, true);
  }

  @When("the entity term codes are retrieved (excluding inactive)")
  public void getEntityTermCodesExcludingInactive() {
    entityTermCodes = conceptService.getEntityTermCodes(entityIri, false);
  }

  @Then("they should all be received in status, then alphabetic, order")
  public void statusAlphabeticOrder() {
    assertFalse(entityTermCodes.isEmpty());
    assertEquals(4, entityTermCodes.size());
    assertEquals("Diabetes mellitus type II", entityTermCodes.get(0).getTerm());
    assertEquals("T2DM - diabetes mellitus type 2", entityTermCodes.get(1).getTerm());
    assertEquals("Diabetes mellitus type 2", entityTermCodes.get(2).getTerm());
    assertEquals("Type 2 diabetes", entityTermCodes.get(3).getTerm());
  }

  @Then("2 should be received in alphabetic, order")
  public void alphabeticOrder() {
    assertFalse(entityTermCodes.isEmpty());
    assertEquals(2, entityTermCodes.size());
    assertEquals("Diabetes mellitus type II", entityTermCodes.get(0).getTerm());
    assertEquals("T2DM - diabetes mellitus type 2", entityTermCodes.get(1).getTerm());
  }
}
