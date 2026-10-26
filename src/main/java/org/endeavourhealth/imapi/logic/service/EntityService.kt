package org.endeavourhealth.imapi.logic.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.xml.bind.ValidationException
import org.endeavourhealth.imapi.dataaccess.EntityRepository
import org.endeavourhealth.imapi.errorhandling.DataMissingException
import org.endeavourhealth.imapi.logic.cache.ReferenceDataCache
import org.endeavourhealth.imapi.logic.validator.EntityValidator
import org.endeavourhealth.imapi.model.EntityReferenceNode
import org.endeavourhealth.imapi.model.Namespace
import org.endeavourhealth.imapi.model.Pageable
import org.endeavourhealth.imapi.model.ValidatedEntity
import org.endeavourhealth.imapi.model.dto.FilterOptionsDto
import org.endeavourhealth.imapi.model.dto.ParentDto
import org.endeavourhealth.imapi.model.iml.Entity
import org.endeavourhealth.imapi.model.imq.DisplayMode
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.QueryException
import org.endeavourhealth.imapi.model.requests.EntityValidationRequest
import org.endeavourhealth.imapi.model.responses.EntityValidationResponse
import org.endeavourhealth.imapi.model.search.EntityDocument
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.model.tripletree.*
import org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri
import org.endeavourhealth.imapi.queryengine.LogicOptimizer
import org.endeavourhealth.imapi.vocabulary.*
import org.springframework.stereotype.Component

@Component
class EntityService {
  private val entityRepository: EntityRepository
  private val validator = EntityValidator()
  private val mapper = ObjectMapper()

  constructor() {
    entityRepository = EntityRepository()
  }

  constructor(entityRepository: EntityRepository) {
    this.entityRepository = entityRepository
  }

  @Throws(JsonProcessingException::class, QueryException::class)
  fun convertRuleToLogicalJson(bundle: TTBundle) {
    if (bundle.entity == null || bundle.entity.get(iri(IM.DEFINITION)) == null) return
    val query = bundle.entity.get(TTIriRef.iri(IM.DEFINITION)).asLiteral().objectValue(Query::class.java)
    LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL)
    bundle.entity.set(IM.DEFINITION.asIri(), mapper.writeValueAsString(query))
  }

  fun getBundle(iri: String?, predicates: MutableSet<String>?): TTBundle {
    if (iri.isNullOrEmpty()) return TTBundle().setEntity(TTEntity())
    return entityRepository.getBundle(iri, predicates)
  }

  fun getPartialEntity(iri: String, predicates: MutableSet<String>?): TTEntity? {
    val bundle = getBundle(iri, predicates)
    return bundle.entity
  }

  @Throws(JsonProcessingException::class)
  fun getBundleByPredicateExclusions(iri: String, excludePredicates: MutableSet<String>?): TTBundle {
    val bundle = entityRepository.getBundle(iri, excludePredicates, true)
    filterOutSpecifiedPredicates(excludePredicates, bundle)
    filterOutInactiveTermCodes(bundle)
    return bundle
  }

  fun getPartialEntities(iris: MutableSet<String>, predicates: MutableSet<String>?): MutableList<TTEntity> {
    val entities: MutableList<TTEntity> = ArrayList<TTEntity>()
    require(!iris.any { str -> str.isEmpty() }) { "Iri list contains an empty or null string" }
    iris.forEach {
      val entity = getBundle(it, predicates).entity
      entities.add(entity)
    }
    return entities
  }

  fun getEntityReference(iri: String?): TTIriRef? {
    if (iri.isNullOrEmpty()) return null
    return entityRepository.getEntityReferenceByIri(iri)
  }


  fun getEntityChildrenPagedWithTotalCount(
    iri: String,
    schemeIris: MutableList<String>?,
    page: Int?,
    size: Int?,
    inactive: Boolean,
    entityTypes: MutableList<String>?
  ): Pageable<EntityReferenceNode>? {
    if (iri.isEmpty()) return null

    var rowNumber = 0
    if (page != null && size != null) rowNumber = (page - 1) * size

    val childrenAndTotalCount = entityRepository.findImmediateChildrenPagedByIriWithTotalCount(
      iri,
      schemeIris,
      rowNumber,
      size,
      inactive,
      entityTypes
    )
    return iriRefPageableToEntityReferenceNodePageable(childrenAndTotalCount, schemeIris, inactive, iri)
  }


  fun getEntityChildrenPagedWithTotalCount(
    iri: String,
    schemeIris: MutableList<String>?,
    page: Int?,
    size: Int?,
    inactive: Boolean
  ): Pageable<EntityReferenceNode>? {
    return getEntityChildrenPagedWithTotalCount(iri, schemeIris, page, size, inactive, null)
  }

  fun getPartialWithTotalCount(
    iri: String,
    predicateList: String,
    schemeIris: MutableList<String>?,
    page: Int?,
    size: Int?,
    inactive: Boolean
  ): Pageable<TTIriRef>? {
    if (iri.isEmpty()) return null

    var rowNumber = 0
    if (page != null && size != null) rowNumber = (page - 1) * size

    return entityRepository.findPartialWithTotalCount(iri, predicateList, schemeIris, rowNumber, size, inactive)
  }


  fun getImmediateParents(
    iri: String?,
    schemeIris: MutableList<String>?,
    pageIndex: Int?,
    pageSize: Int?,
    inactive: Boolean
  ): MutableList<EntityReferenceNode> {
    if (iri.isNullOrEmpty()) return mutableListOf()

    var rowNumber = 0
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize

    val parentsInt = getParents(iri, schemeIris, rowNumber, pageSize, inactive)
    val parents = parentsInt.map { p -> EntityReferenceNode(p.iri, p.name) }

    for (parent in parents) parent.setType(entityRepository.getEntityTypes(parent.iri))

    return parents.toMutableList()
  }

  private fun getParents(
    iri: String,
    schemeIris: MutableList<String>?,
    rowNumber: Int,
    pageSize: Int?,
    inactive: Boolean
  ): MutableList<TTIriRef> {
    return entityRepository.findImmediateParentsByIri(iri, schemeIris, rowNumber, pageSize, inactive)
  }

  fun isWhichType(iri: String?, candidates: MutableList<String>?): MutableList<TTIriRef> {
    if (iri.isNullOrEmpty() || candidates.isNullOrEmpty()) return mutableListOf()
    return entityRepository.findAncestorsByType(iri, RDFS.SUBCLASS_OF, candidates).sortedBy { it.name }.toMutableList()
  }

  fun usages(iri: String?, pageIndex: Int?, pageSize: Int?): MutableList<TTEntity> {
    val usageEntities = mutableListOf<TTEntity>()
    if (iri.isNullOrEmpty()) return mutableListOf()

    val xmlDataTypes: MutableSet<String> = entityRepository.getByNamespace(NAMESPACE.XSD)
    if (xmlDataTypes.contains(iri)) return mutableListOf()

    var rowNumber = 0
    if (pageIndex != null && pageSize != null) rowNumber = pageIndex * pageSize

    // an absent page size used to mean "every usage"; cap it so one request cannot load them all
    val limit = pageSize ?: MAX_USAGES
    var usageRefs =
      entityRepository.getConceptUsages(iri, rowNumber, limit)
        .distinct()
        .sortedWith(compareBy(nullsLast()) { it.iri })

    usageRefs = usageRefs.filter { it.iri != iri }
    val typesByIri =
      entityRepository.getTypesForEntities(usageRefs.map { it.iri }.toMutableList())
    for (usage in usageRefs) {
      usageEntities.add(
        TTEntity().setIri(usage.iri).setName(usage.name).setType(typesByIri[usage.iri])
      )
    }

    return usageEntities
  }

  fun totalRecords(iri: String?): Int {
    if (iri.isNullOrEmpty()) return 0

    val xmlDataTypes = entityRepository.getByNamespace(NAMESPACE.XSD)
    if (xmlDataTypes.contains(iri)) return 0

    return entityRepository.getConceptUsagesCount(iri)
  }

  fun getSummaryFromConfig(iri: String?, configs: MutableList<String>?): TTEntity {
    if (iri.isNullOrEmpty() || configs.isNullOrEmpty()) {
      return TTEntity()
    }
    val excludedForSummary = mutableListOf(
      "None",
      RDFS.SUBCLASS_OF.toString(),
      "subtypes",
      IM.IS_CHILD_OF.toString(),
      IM.HAS_CHILDREN.toString(),
      "termCodes",
      "semanticProperties",
      "dataModelProperties"
    )
    val predicates = configs.filter { !excludedForSummary.contains(it) }
    return getBundle(iri, predicates.toMutableSet()).entity
  }

  @Throws(DataMissingException::class)
  fun getSummary(iri: String?): SearchResultSummary? {
    if (iri.isNullOrEmpty()) return null
    return entityRepository.getEntitySummaryByIri(iri)
  }

  fun getConceptShape(iri: String?): TTEntity? {
    if (iri.isNullOrEmpty()) return null
    val entity =
      getBundle(iri, mutableSetOf(SHACL.PROPERTY.toString(), SHACL.OR.toString(), RDF.TYPE.toString())).entity
    val value = entity.get(RDF.TYPE.asIri())
    if (!value.elements.contains(SHACL.NODESHAPE.asIri())) {
      return null
    }
    return entity
  }

  fun getConceptList(iris: MutableList<String>?): TTDocument? {
    if (iris.isNullOrEmpty()) {
      return null
    }
    val document = TTDocument()
    val namespaces = getNamespaces()
    val context = TTContext()
    for (namespace in namespaces) {
      context.add(namespace.iri, namespace.prefix, namespace.name)
    }
    document.setContext(context)
    for (iri in iris) {
      val bundle = getBundle(iri, null)
      document.addEntity(bundle.entity)
    }
    return document
  }

  fun getParentPath(iri: String): MutableList<TTIriRef> {
    val entity = getBundle(iri, mutableSetOf(RDFS.LABEL.toString())).entity
    val parents: MutableList<TTIriRef> = mutableListOf()
    getParentPathRecursive(iri, parents)
    parents.reverse()
    parents.add(TTIriRef(iri, entity.getName()))
    return parents
  }

  private fun getParentPathRecursive(iri: String, parents: MutableList<TTIriRef>) {
    val parent = entityRepository.findParentFolderRef(iri)
    if (parent != null) {
      parents.add(parent)
      getParentPathRecursive(parent.iri, parents)
    }
  }

  fun getNames(iris: MutableSet<String>): MutableSet<TTIriRef> {
    val result = iris.map { TTIriRef(it) }.toMutableSet()
    entityRepository.getNames(result)
    return result
  }

  fun getParentHierarchies(iri: String): MutableList<MutableList<TTIriRef>> {
    val parentHierarchy = ParentDto(iri, null, null)
    addParentHierarchiesRecursively(parentHierarchy, mutableSetOf())
    return getParentHierarchiesFlatLists(parentHierarchy)
  }

  fun getParentHierarchiesFlatLists(parent: ParentDto?): MutableList<MutableList<TTIriRef>> {
    val parentHierarchies: MutableList<MutableList<TTIriRef>> = mutableListOf()
    parentHierarchies.add(mutableListOf())
    addParentHierarchiesRecursively(parentHierarchies, parentHierarchies.first(), parent)
    return parentHierarchies
  }

  private fun addParentHierarchiesRecursively(
    parentHierarchies: MutableList<MutableList<TTIriRef>>,
    currentPath: MutableList<TTIriRef>,
    parent: ParentDto?
  ) {
    if (parent != null && parent.hasMultipleParents()) {
      parentHierarchies.remove(currentPath)
      parent.parents?.forEach {
        val path: MutableList<TTIriRef> = currentPath.toMutableList()
        path.add(TTIriRef(it.iri, it.name))
        parentHierarchies.add(path)
        addParentHierarchiesRecursively(parentHierarchies, path, it)
      }
    } else if (parent != null && parent.hasSingleParent()) {
      parent.parents?.forEach {
        currentPath.add(TTIriRef(it.iri, it.name))
        addParentHierarchiesRecursively(parentHierarchies, currentPath, it)
      }
    }
  }

  private fun addParentHierarchiesRecursively(parent: ParentDto, done: MutableSet<String>) {
    val parents: MutableList<ParentDto> = entityRepository.findParentHierarchies(parent.iri)
    if (parents.isNotEmpty()) {
      parent.setParents(parents)
      for (parentsParent in parents) {
        if (!done.contains(parentsParent.iri)) {
          done.add(parentsParent.iri)
          addParentHierarchiesRecursively(parentsParent, done)
        }
      }
    }
  }

  fun getShortestPathBetweenNodes(ancestor: String, descendant: String): MutableList<TTIriRef> {
    var shortestPath: MutableList<TTIriRef> = mutableListOf()
    var paths = getParentHierarchies(descendant)
    paths = paths.filter { path -> path.any { it.iri === ancestor } }.toMutableList()

    paths.sortByDescending { it.size }

    if (paths.isNotEmpty()) {
      shortestPath = paths.last()
      val index = indexOf(shortestPath, ancestor)
      shortestPath = shortestPath.subList(0, if (index == shortestPath.size) index else index + 1)
    }
    return shortestPath
  }

  private fun indexOf(iriRefs: MutableList<TTIriRef>, iri: String): Int {
    return iriRefs.indexOfFirst { it.iri == iri }
  }

  fun iriExists(iri: String): Boolean {
    return entityRepository.iriExists(iri)
  }

  fun getName(iri: String): String? {
    return entityRepository.getEntityReferenceByIri(iri).name
  }

  fun isLinked(subject: String, predicate: TTIriRef, `object`: String): Boolean {
    return entityRepository.predicatePathExists(subject, predicate, `object`)
  }

  fun getOSDocument(iri: String): EntityDocument {
    return entityRepository.getOSDocument(iri)
  }

  fun getPredicates(iri: String): MutableSet<String> {
    return entityRepository.getPredicates(iri)
  }

  val iM1SchemeOptions: MutableList<String>
    get() = entityRepository.iM1SchemeOptions

  protected fun iriRefPageableToEntityReferenceNodePageable(
    iriRefPageable: Pageable<TTIriRef>,
    schemeIris: MutableList<String>?,
    inactive: Boolean,
    parentContext: String?
  ): Pageable<EntityReferenceNode> {
    val result = Pageable<EntityReferenceNode>()
    val totalCount = iriRefPageable.totalCount

    if (totalCount != null && totalCount > 0 && iriRefPageable.result != null) {
      result.setTotalCount(totalCount)
      val iris: MutableSet<String> = hashSetOf()
      for (entity in iriRefPageable.result) {
        iris.add(entity.iri)
      }
      val nodes: MutableList<EntityReferenceNode> =
        entityRepository.getEntityReferenceNodes(iris, schemeIris, inactive, parentContext)
      nodes.sortWith(
        compareBy<EntityReferenceNode> { it.orderNumber }
          .thenBy { it.name }
      )

      result.setResult(nodes)
    } else {
      result.setResult(mutableListOf())
    }

    return result
  }

  fun getImmediateChildren(
    iri: String?,
    schemeIris: MutableList<String>?,
    pageIndex: Int?,
    pageSize: Int?,
    inactive: Boolean
  ): MutableList<EntityReferenceNode> {
    if (iri.isNullOrEmpty()) return mutableListOf()

    val result: MutableList<EntityReferenceNode> = mutableListOf()
    var rowNumber = 0
    if (pageIndex != null && pageSize != null) rowNumber = (pageIndex - 1) * pageSize

    for (c in getChildren(iri, schemeIris, rowNumber, pageSize, inactive)) {
      result.add(getEntityAsEntityReferenceNode(c.getIri()))
    }

    result.sortWith(
      compareBy<EntityReferenceNode> { it.orderNumber }
        .thenBy { it.name }
    )

    return result
  }

  fun getChildren(
    iri: String,
    schemeIris: MutableList<String>?,
    rowNumber: Int?,
    pageSize: Int?,
    inactive: Boolean
  ): MutableList<TTIriRef> {
    return entityRepository.findImmediateChildrenByIri(iri, schemeIris, rowNumber, pageSize, inactive)
  }


  fun getEntityAsEntityReferenceNode(iri: String): EntityReferenceNode {
    return getEntityAsEntityReferenceNode(iri, null, false)
  }

  fun getAsEntityReferenceNodes(iris: MutableList<String>): MutableList<EntityReferenceNode> {
    return entityRepository.getAsEntityReferenceNodes(iris)
  }

  fun getEntityAsEntityReferenceNode(
    iri: String,
    schemeIris: MutableList<String>?,
    inactive: Boolean
  ): EntityReferenceNode {

    return entityRepository.getEntityReferenceNode(iri, schemeIris, inactive)
  }

  fun getValidatedEntitiesBySnomedCodes(codes: MutableList<String>): MutableList<ValidatedEntity> {
    val snomedCodes = codes.map { NAMESPACE.SNOMED.toString() + it }
    val entities =
      getPartialEntities(snomedCodes.toMutableSet(), mutableSetOf(RDFS.LABEL.toString(), IM.CODE.toString()))
    val setService = SetService()
    val needed =
      setService.getDistillation(entities.map { iri(it.iri) }
        .toList())
    val validatedEntities: MutableList<ValidatedEntity> = mutableListOf()
    for (entity in entities) {
      val validatedEntity = validateEntity(entity, needed)
      if (validatedEntities.any { it.iri == validatedEntity.iri }) {
        validatedEntity.setCode("Duplicate")
      }
      validatedEntities.add(validateEntity(entity, needed))
    }
    return validatedEntities
  }

  private fun validateEntity(entity: TTEntity, needed: MutableList<TTIriRef>): ValidatedEntity {
    val validatedEntity = ValidatedEntity()
    validatedEntity
      .setIri(entity.iri)
      .setName(entity.getName())
      .setCode(entity.getCode())
    val isInvalid = entity.iri.isNotEmpty() && entity.getName().isNotEmpty() && entity.getCode().isNotEmpty()
    val found =
      needed.firstOrNull { it.iri == validatedEntity.iri }
    if (isInvalid) {
      validatedEntity.setValidationCode("Invalid")
      validatedEntity.setValidationLabel("Not an entity")
    } else if (null != found) {
      needed.remove(found)
      validatedEntity.setValidationCode("Valid")
    } else {
      validatedEntity.setValidationCode("Child")
    }
    if (validatedEntity.getCode().isEmpty() && validatedEntity.iri.isNotEmpty() && validatedEntity.iri.contains("#")
    ) {
      validatedEntity.code = validatedEntity.iri.split("#".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()[1]
    }
    return validatedEntity
  }

  @Throws(JsonProcessingException::class)
  fun getDetailsDisplay(iri: String): TTBundle {
    val excludedPredicates: MutableSet<String> = VocabUtils.asHashSet(IM.CODE, RDFS.LABEL, IM.HAS_STATUS, RDFS.COMMENT)
    val entityPredicates = getPredicates(iri)
    val response: TTBundle
    if (entityPredicates.contains(IM.HAS_MEMBER.toString())) {
      response = getBundleByPredicateExclusions(iri, excludedPredicates)
      excludedPredicates.add(IM.HAS_MEMBER.toString())
      val partialAndCount = getPartialWithTotalCount(iri, IM.HAS_MEMBER.toString(), null, 1, 10, false)
      val partialAsTTArray = TTArray()
      partialAndCount?.result?.let { it.forEach { item -> partialAsTTArray.add(item) } }
      val loadMoreNode = TTNode()
        .setIri(IM.LOAD_MORE.toString())
        .set(iri(RDFS.LABEL), "Load more")
        .set(iri("${NAMESPACE.IM}totalCount"), partialAndCount?.totalCount)
      partialAsTTArray.add(loadMoreNode)
      response.addPredicate(iri(IM.HAS_MEMBER))
      response.entity.set(iri(IM.HAS_MEMBER), partialAsTTArray)
    } else {
      response = getBundleByPredicateExclusions(iri, excludedPredicates)
    }
    response.entity.removeObject(iri(RDF.TYPE))
    return response
  }

  fun loadMoreDetailsDisplay(iri: String, predicate: String, pageIndex: Int, pageSize: Int): TTBundle {
    val response = getPartialWithTotalCount(iri, predicate, null, pageIndex, pageSize, false)
    val entity = TTEntity()
    entity.addObject(iri(predicate), response?.totalCount)
    val bundle = TTBundle()
    bundle.setEntity(entity)
    return bundle
  }

  @Throws(ValidationException::class)
  fun validate(request: EntityValidationRequest): EntityValidationResponse? {
    require(!request.validationIri.isNullOrEmpty()) { "Missing validation iri" }
    return validator.validate(request, this)
  }

  fun getEntitiesByType(typeIri: EntityType): MutableList<TTIriRef> {
    return entityRepository.findEntitiesByType(typeIri)
  }

  fun getNamespaces(): MutableList<Namespace> {
    return ReferenceDataCache.getNamespaces(entityRepository::findNamespaces)
  }


  fun getIsas(iri: String): MutableList<TTIriRef> {
    return entityRepository.findInvertedIsas(iri)
  }


  fun getOperatorOptions(iri: String): MutableList<String> {
    return entityRepository.findOperatorOptions(iri)
  }

  fun getXmlSchemaDataTypes(): MutableSet<String> {
    return entityRepository.getByNamespace(NAMESPACE.XSD)
  }

  fun getEntitiesByType(type: String, offset: Int, limit: Int, vararg predicates: String): MutableList<TTEntity> {
    return entityRepository.getEntitiesByType(type, offset, limit, *predicates)
  }

  fun getFilterOptions(): FilterOptionsDto {
    return ReferenceDataCache.getFilterOptions { this.loadFilterOptions() }
  }

  private fun loadFilterOptions(): FilterOptionsDto {
    val filterOptions = FilterOptionsDto()
    filterOptions.setSchemes(getAllChildren(IM.ROOT_NAMESPACE))
    filterOptions.setStatus(getAllChildren(IM.STATUS))
    filterOptions.setTypes(getAllChildren(IM.TYPE_FILTER_OPTIONS))
    filterOptions.setSortFields(getAllChildren(IM.SORT_FIELD_FILTER_OPTIONS))
    filterOptions.setSortDirections(getAllChildren(IM.SORT_DIRECTION_FILTER_OPTIONS))
    return filterOptions
  }

  fun getFilterDefaults(): FilterOptionsDto {
    return ReferenceDataCache.getFilterDefaults { this.loadFilterDefaults() }
  }

  private fun loadFilterDefaults(): FilterOptionsDto {
    val filterOptions = FilterOptionsDto()
    filterOptions.setStatus(getAllChildren(IM.STATUS_FILTER_DEFAULTS))
    filterOptions.setTypes(getAllChildren(IM.TYPE_FILTER_DEFAULTS))
    filterOptions.setTypeSchemes(getAllTypeSchemes())
    filterOptions.setSchemes(getDefaultSchemes(filterOptions))
    filterOptions.setSortFields(getAllChildren(IM.SORT_FIELD_FILTER_DEFAULTS))
    filterOptions.setSortDirections(getAllChildren(IM.SORT_DIRECTION_FILTER_DEFAULTS))
    return filterOptions
  }

  private fun getDefaultSchemes(filterOptions: FilterOptionsDto): MutableList<TTIriRef> {
    val schemes: MutableList<TTIriRef> = mutableListOf()
    filterOptions.types?.forEach { type ->
      filterOptions.typeSchemes?.get(type.iri)?.filterNot { it in schemes }?.let { schemes.addAll(it) }
    }
    return schemes
  }

  private fun getAllTypeSchemes(): MutableMap<String, MutableList<TTIriRef>> {
    return entityRepository.getTypeSchemeDefaults()
  }

  private fun getAllChildren(iri: VocabEnum): MutableList<TTIriRef> {
    return getChildren(iri.toString(), null, null, null, false)
  }

  fun getAllowableChildTypes(iri: String): MutableList<TTEntity> {
    return entityRepository.getAllowableChildTypes(iri)
  }

  fun entityExists(iri: String): Boolean {
    return entityRepository.hasPredicates(iri, VocabUtils.asHashSet(RDF.TYPE))
  }

  fun getChildIris(iri: String): MutableList<String> {
    return entityRepository.getChildIris(iri)
  }

  fun getEntityFromTerm(term: String, schemes: MutableSet<String>): MutableList<TTBundle> {
    return entityRepository.getEntityFromTerm(term, schemes)
  }

  fun getIriDetails(iris: MutableSet<String>): MutableMap<String, Entity> {
    return entityRepository.getIriDetails(iris)
  }

  fun getMatchedTo(iri: String, namespace: NAMESPACE): MutableSet<String> {
    return entityRepository.getMatchedTo(iri, namespace)
  }

  companion object {
    const val MAX_CHILDREN: Int = 200
    const val MAX_USAGES: Int = 200
    fun filterOutSpecifiedPredicates(excludePredicates: MutableSet<String>?, bundle: TTBundle) {
      if (excludePredicates != null) {
        val filtered = bundle.predicates
          .filter { entry -> entry.key != RDFS.LABEL.toString() && entry.value != null }
        bundle.setPredicates(filtered)
        if (excludePredicates.contains(RDFS.LABEL.toString())) {
          bundle.entity.set(RDFS.LABEL.asIri(), null as TTValue?)
        }
      }
    }

    fun filterOutInactiveTermCodes(bundle: TTBundle) {
      if (bundle.entity.get(iri(IM.HAS_TERM_CODE)) != null) {
        val termCodes = bundle.entity.get(iri(IM.HAS_TERM_CODE)).elements
        val activeTermCodes = TTArray()
        for (value in termCodes) {
          val status = value.asNode().get(iri(IM.HAS_STATUS))
          if (status == null || status.asIriRef().iri == IM.ACTIVE.toString()) {
            activeTermCodes.add(value)
          }
        }
        bundle.entity.set(iri(IM.HAS_TERM_CODE), activeTermCodes)
      }
    }
  }
}


