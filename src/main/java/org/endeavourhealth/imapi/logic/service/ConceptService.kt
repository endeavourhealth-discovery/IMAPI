package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.dataaccess.ConceptRepository
import org.endeavourhealth.imapi.dataaccess.EntityRepository
import org.endeavourhealth.imapi.model.ConceptContextMap
import org.endeavourhealth.imapi.model.Namespace
import org.endeavourhealth.imapi.model.dto.SimpleMap
import org.endeavourhealth.imapi.model.search.SearchTermCode
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.tripletree.TTValue
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.RDFS
import org.endeavourhealth.imapi.vocabulary.VocabUtils
import org.springframework.stereotype.Component

@Component
class ConceptService(
  private val entityRepository: EntityRepository = EntityRepository(),
  private val conceptRepository: ConceptRepository = ConceptRepository()
) {
  fun getMatchedFrom(iri: String?): MutableList<SimpleMap> {
    if (iri.isNullOrEmpty()) return mutableListOf()
    val scheme = iri.substring(0, iri.indexOf("#") + 1)
    val namespaces: MutableList<Namespace> = entityRepository.findNamespaces()
    val schemes = namespaces.mapNotNull(Namespace::iri).toMutableList()
    schemes.remove(scheme)
    return conceptRepository.getMatchedFrom(iri, schemes)
  }

  fun getMatchedTo(iri: String?): MutableList<SimpleMap> {
    if (iri.isNullOrEmpty()) return mutableListOf()
    val scheme = iri.substring(0, iri.indexOf("#") + 1)
    val namespaces: MutableList<Namespace> = entityRepository.findNamespaces()
    val schemes = namespaces.mapNotNull(Namespace::iri).toMutableList()
    schemes.remove(scheme)
    return conceptRepository.getMatchedTo(iri, schemes)
  }

  fun getEntityTermCodes(iri: String?, includeInactive: Boolean): MutableList<SearchTermCode> {
    if (iri.isNullOrEmpty()) return mutableListOf()
    val termsBundle = entityRepository.getBundle(iri, VocabUtils.asHashSet(IM.HAS_TERM_CODE))
    if (!includeInactive) EntityService.filterOutInactiveTermCodes(termsBundle)
    val terms = termsBundle.entity.get(TTIriRef.iri(IM.HAS_TERM_CODE)) ?: return mutableListOf()
    val termsSummary: MutableList<SearchTermCode> = mutableListOf()
    for (term in terms.elements) {
      processTerm(term, termsSummary)
    }
    return termsSummary.sorted().toMutableList()
  }

  fun getPropertiesForDomains(iris: MutableSet<String>?): MutableSet<String>? {
    if (iris.isNullOrEmpty()) return null
    return conceptRepository.getPropertiesForDomains(iris)
  }

  fun isValidPropertyForDomains(propertyIri: String?, iris: MutableSet<String>?): Boolean {
    if (iris.isNullOrEmpty()) return false
    val properties: MutableSet<String> = conceptRepository.getPropertiesForDomains(iris)
    return properties.contains(propertyIri)
  }

  fun getRangesForProperty(iri: String?): MutableSet<String>? {
    if (iri.isNullOrEmpty()) return null
    return conceptRepository.getRangesForProperty(iri)
  }

  fun getConceptContextMaps(iri: String): MutableList<ConceptContextMap> {
    return conceptRepository.getConceptContextMaps(iri)
  }

  fun getShortestTerm(iri: String): String? {
    return conceptRepository.getShortestTerm(iri)
  }


  private fun processTerm(term: TTValue, termsSummary: MutableList<SearchTermCode>) {
    val node = term.asNode()
    val codeRef = TTIriRef.iri(IM.CODE)
    val code = node.get(codeRef).get(0).asLiteral().value
    val ts = termsSummary.none { it.code == code }
    if (node.has(codeRef) && ts) {
      val newTerm = SearchTermCode()
      if (node.has(codeRef)) newTerm.setCode(code)
      if (node.has(TTIriRef.iri(RDFS.LABEL))) newTerm.setTerm(
        node.get(TTIriRef.iri(RDFS.LABEL)).get(0).asLiteral().value
      )
      if (node.has(TTIriRef.iri(IM.HAS_STATUS))) newTerm.setStatus(
        node.get(TTIriRef.iri(IM.HAS_STATUS)).get(0).asIriRef()
      )
      termsSummary.add(newTerm)
    }
  }
}
