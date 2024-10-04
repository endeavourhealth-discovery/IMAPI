package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ConceptModelServiceTest {
  @InjectMocks
  ConceptService conceptService = new ConceptService();

  @Mock
  EntityRepository entityRepository;

  @Test
  void getEntityTermCodes_NullIri() {
    List<SearchTermCode> actual = conceptService.getEntityTermCodes(null, false);
    assertNotNull(actual);
  }

  @Test
  void getEntityTermCodes_EmptyIri() {
    List<SearchTermCode> actual = conceptService.getEntityTermCodes("", false);
    assertNotNull(actual);
  }

  @Test
  void getEntityTermCodes_NotNullIri() {
    SearchTermCode termCode = new SearchTermCode()
      .setCode("24951000252112")
      .setTerm("Adverse reaction to Testogel")
      .setStatus(new TTIriRef().setIri(IM.ACTIVE).setName(TTIriRef.iri(IM.ACTIVE).getName()));
    when(entityRepository.getBundle("http://endhealth.info/im#25451000252115", Stream.of(IM.HAS_TERM_CODE).collect(Collectors.toSet()))).thenReturn(new TTBundle().setEntity(new TTEntity().set(TTIriRef.iri(IM.HAS_TERM_CODE), new TTArray().add(new TTNode().set(TTIriRef.iri(IM.CODE), new TTLiteral(termCode.getCode())).set(TTIriRef.iri(RDFS.LABEL), new TTLiteral(termCode.getTerm())).set(TTIriRef.iri(IM.HAS_STATUS), new TTArray().add(termCode.getStatus()))))));
    List<SearchTermCode> actual = conceptService.getEntityTermCodes("http://endhealth.info/im#25451000252115", false);
    assertNotNull(actual);
  }

  @Test
  void getSimpleMaps_NullIri() {
    List<SimpleMap> actual = conceptService.getMatchedFrom(null);
    assertNotNull(actual);
  }

  @Test
  void getSimpleMaps_EmptyIri() {
    Collection<SimpleMap> actual = conceptService.getMatchedFrom("");
    assertNotNull(actual);
  }

  @Test
  void getSimpleMaps_NotNullIri() {
    Collection<SimpleMap> actual = conceptService.getMatchedFrom("http://endhealth.info/im#25451000252115");
    assertNotNull(actual);
  }
}
