package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.logic.service.EntityService.filterOutInactiveTermCodes;
import static org.endeavourhealth.imapi.logic.service.EntityService.iriRefPageableToEntityReferenceNodePageable;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class ConceptService {

  private EntityRepository entityRepository = new EntityRepository();
  private ConceptRepository conceptRepository = new ConceptRepository();

  public List<SimpleMap> getMatchedFrom(String iri) {
    if (iri == null || iri.isEmpty()) return new ArrayList<>();
    String scheme = iri.substring(0, iri.indexOf("#") + 1);
    List<Namespace> namespaces = entityRepository.findNamespaces();
    List<String> schemes = namespaces.stream().map(Namespace::getIri).collect(Collectors.toList());
    schemes.remove(scheme);
    return conceptRepository.getMatchedFrom(iri, schemes);
  }

  public List<SimpleMap> getMatchedTo(String iri) {
    if (iri == null || iri.isEmpty()) return new ArrayList<>();
    String scheme = iri.substring(0, iri.indexOf("#") + 1);
    List<Namespace> namespaces = entityRepository.findNamespaces();
    List<String> schemes = namespaces.stream().map(Namespace::getIri).collect(Collectors.toList());
    schemes.remove(scheme);
    return conceptRepository.getMatchedTo(iri, schemes);
  }

  public List<SearchTermCode> getEntityTermCodes(String iri, boolean includeInactive) {
    if (iri == null || iri.isEmpty())
      return Collections.emptyList();
    TTBundle termsBundle = entityRepository.getBundle(iri, Stream.of(IM.HAS_TERM_CODE).collect(Collectors.toSet()));
    if (!includeInactive) filterOutInactiveTermCodes(termsBundle);
    TTArray terms = termsBundle.getEntity().get(iri(IM.HAS_TERM_CODE));
    if (null == terms) return Collections.emptyList();
    List<SearchTermCode> termsSummary = new ArrayList<>();
    for (TTValue term : terms.getElements()) {
      processTerm(term, termsSummary);
    }
    return termsSummary;
  }

  public Pageable<EntityReferenceNode> getSuperiorPropertiesPaged(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (null == iri || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (null != page && null != size) rowNumber = (page - 1) * size;

    Pageable<TTIriRef> propertiesAndCount = conceptRepository.getSuperiorPropertiesByConceptPagedWithTotalCount(iri, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount, schemeIris, inactive);
  }

  public Pageable<EntityReferenceNode> getSuperiorPropertiesBoolFocusPaged(List<String> conceptIris, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (null == conceptIris || conceptIris.isEmpty()) return null;

    int rowNumber = 0;
    if (null != page && null != size) rowNumber = (page - 1) * size;

    Pageable<TTIriRef> propertiesAndCount = conceptRepository.getSuperiorPropertiesByConceptBoolFocusPagedWithTotalCount(conceptIris, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount, schemeIris, inactive);
  }

  public Pageable<EntityReferenceNode> getSuperiorPropertyValuesPaged(String iri, List<String> schemeIris, Integer page, Integer size, boolean inactive) {
    if (null == iri || iri.isEmpty()) return null;

    int rowNumber = 0;
    if (null != page && null != size) rowNumber = (page - 1) * size;


    Pageable<TTIriRef> propertiesAndCount = conceptRepository.getSuperiorPropertyValuesByPropertyPagedWithTotalCount(iri, rowNumber, size, inactive);
    return iriRefPageableToEntityReferenceNodePageable(propertiesAndCount, schemeIris, inactive);
  }

  public List<ConceptContextMap> getConceptContextMaps(String iri) {
    return conceptRepository.getConceptContextMaps(iri);
  }

  private void processTerm(TTValue term, List<SearchTermCode> termsSummary) {
    if (null != term.asNode().get(iri(IM.CODE)) && null == termsSummary.stream().filter(t -> term.asNode().get(iri(IM.CODE)).get(0).asLiteral().getValue().equals(t.getCode())).findAny().orElse(null)) {
      SearchTermCode newTerm = new SearchTermCode();
      if (term.asNode().has(iri(IM.CODE)))
        newTerm.setCode(term.asNode().get(iri(IM.CODE)).get(0).asLiteral().getValue());
      if (term.asNode().has(iri(RDFS.LABEL)))
        newTerm.setTerm(term.asNode().get(iri(RDFS.LABEL)).get(0).asLiteral().getValue());
      if (term.asNode().has(iri(IM.HAS_STATUS)))
        newTerm.setStatus(term.asNode().get(iri(IM.HAS_STATUS)).get(0).asIriRef());
      termsSummary.add(
        newTerm
      );
    }
  }

}
